package project.controllers;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import project.Cart;
import project.Order;
import project.Product;

public class CartController {

    @FXML
    private TableView<Map.Entry<Product, Integer>> cartTableView;

    @FXML
    private TableColumn<Map.Entry<Product, Integer>, String> nameColumn;

    @FXML
    private TableColumn<Map.Entry<Product, Integer>, Integer> quantityColumn;

    @FXML
    private TableColumn<Map.Entry<Product, Integer>, Double> priceColumn;

    @FXML
    private Label totalCostLabel;

    @FXML
    private CheckBox cashCheckBox;
    @FXML
    private CheckBox instapayCheckBox;
    @FXML
    private CheckBox cardCheckBox;

    @FXML
    private Button checkoutButton;

    private Cart cart;

    @FXML
    public void initialize() {
        setupTableColumns();

        cashCheckBox.setSelected(true); // Default selection
        instapayCheckBox.setSelected(false);
        cardCheckBox.setSelected(false);

        cashCheckBox.setOnAction(_ -> setExclusiveSelection(cashCheckBox));
        instapayCheckBox.setOnAction(_ -> setExclusiveSelection(instapayCheckBox));
        cardCheckBox.setOnAction(_ -> setExclusiveSelection(cardCheckBox));
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        updateCartView();
    }

    @FXML
    private void removeSelectedProduct() {
        Map.Entry<Product, Integer> selectedEntry = cartTableView.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            Product product = selectedEntry.getKey();
            cart.removeProduct(product);
            updateCartView();
        }
    }

    private void updateCartView() {
        if (cart == null) return;

        Map<Product, Integer> aggregator = new LinkedHashMap<>();
        for (Product prod : cart.getProducts()) {
            aggregator.put(prod, aggregator.getOrDefault(prod, 0) + 1);
        }

        ObservableList<Map.Entry<Product, Integer>> items =
                FXCollections.observableArrayList(aggregator.entrySet());

        cartTableView.setItems(items);

        totalCostLabel.setText(String.format("Total Cost: $%.2f", cart.getTotalCost()));
    }

    public void addProductToCart(Product product) {
        cart.addProduct(product);
        updateCartView();
    }

    @FXML
    private void openCheckout() {
        if (cart == null || cart.getProducts().isEmpty()) {
            showErrorAlert("Your cart is empty. Please add products before checking out.");
            return;
        }

        if (cashCheckBox.isSelected()) {
            try {
                Scanner scanner = new Scanner("Cash");
                Order.checkOut(cart, scanner);
                showSuccessAlert("Order placed successfully with Cash!");
                updateCartView();
            } catch (Exception e) {
                showErrorAlert("Failed to place order with Cash. " + e.getMessage());
            }
        } else if (instapayCheckBox.isSelected()) {
            try {
                Scanner scanner = new Scanner("Instapay");
                Order.checkOut(cart, scanner);
                showSuccessAlert("Order placed successfully with Instapay!");
                updateCartView();
            } catch (Exception e) {
                showErrorAlert("Failed to place order with Instapay. " + e.getMessage());
            }
        } else if (cardCheckBox.isSelected()) {
            openCardPayment();
        } else {
            showErrorAlert("Please select a payment method.");
        }
    }

    private void setExclusiveSelection(CheckBox selected) {
        cashCheckBox.setSelected(selected == cashCheckBox);
        instapayCheckBox.setSelected(selected == instapayCheckBox);
        cardCheckBox.setSelected(selected == cardCheckBox);
    }

    private void openCardPayment() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/customer/CardPaymentController.fxml"));
            Parent cardPaymentView = loader.load();
    
            CardPaymentController controller = loader.getController();
            controller.setCart(cart);
    
            Stage cardStage = new Stage();
            controller.setStage(cardStage);
    
            cardStage.setOnHidden(_ -> updateCartView());
    
            cardStage.setTitle("Card Payment");
            cardStage.setScene(new Scene(cardPaymentView));
            cardStage.show();
        } catch (IOException e) {
            showErrorAlert("Failed to open Card Payment window.");
        }
    }
    
    

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setStage(Stage stage) {
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(cellData -> {
            Product p = cellData.getValue().getKey();
            return new SimpleStringProperty(p.getName());
        });

        quantityColumn.setCellValueFactory(cellData -> {
            Integer qty = cellData.getValue().getValue();
            return new SimpleIntegerProperty(qty).asObject();
        });

        priceColumn.setCellValueFactory(cellData -> {
            Product p = cellData.getValue().getKey();
            return new SimpleDoubleProperty(p.getPrice()).asObject();
        });
    }

    @FXML
    private void confirmPayment() {
        if (cardCheckBox.isSelected()) {
            try {
                Scanner scanner = new Scanner("Card");
                Order.checkOut(cart, scanner);
                showSuccessAlert("Order placed successfully with Card!");
                updateCartView();
            } catch (Exception e) {
                showErrorAlert("Failed to place order with Card. " + e.getMessage());
            }
        } else {
            showErrorAlert("Please select a payment method.");
        }
    }
}
