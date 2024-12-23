package project.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import project.Database;
import project.Order;
import project.Product;

public class OrderManagementController {

    @FXML
    private TableView<Order> ordersTable;

    @FXML
    private TextArea orderDetailsArea;

    @FXML
    private Button cancelOrderButton;

    private ObservableList<Order> orders;

    @FXML
    public void initialize() {
        try {
            orders = FXCollections.observableArrayList(Database.orders);
            ordersTable.setItems(orders);

            TableColumn<Order, Integer> idColumn = new TableColumn<>("Order ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));

            TableColumn<Order, String> paymentColumn = new TableColumn<>("Payment Method");
            paymentColumn.setCellValueFactory(param -> 
                javafx.beans.property.SimpleStringProperty.stringExpression(
                    javafx.beans.binding.Bindings.createStringBinding(() -> 
                        param.getValue().getPaymentMethod().toString()
                    )
                )
            );

            ordersTable.getColumns().add(idColumn);
            ordersTable.getColumns().add(paymentColumn);
            
            ordersTable.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
                if (newSelection != null) {
                    displayOrderDetails(newSelection);
                    cancelOrderButton.setDisable(false);
                } else {
                    orderDetailsArea.clear();
                    cancelOrderButton.setDisable(true);
                }
            });

            cancelOrderButton.setDisable(true);

        } catch (Exception e) {
            showError("Error", "Failed to load orders.");
        }
    }

    private void displayOrderDetails(Order order) {
        StringBuilder details = new StringBuilder();
        details.append("Order ID: ").append(order.getOrderId()).append("\n")
               .append("Payment Method: ").append(order.getPaymentMethod()).append("\n")
               .append("Products:\n");

        List<Product> products = order.getOrderedProducts();
        Map<String, Integer> productCount = new HashMap<>();

        for (Product product : products) {
            productCount.put(product.getName(), productCount.getOrDefault(product.getName(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : productCount.entrySet()) {
            details.append("- ").append(entry.getKey())
                   .append(" | Quantity: ").append(entry.getValue()).append("\n");
        }

        double total = products.stream().mapToDouble(Product::getPrice).sum();
        details.append("Total: $").append(total);

        orderDetailsArea.setText(details.toString());
    }

    @FXML
    private void cancelOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            orders.remove(selectedOrder);
            Database.orders.remove(selectedOrder);
            Database.saveDatabaseToFile();
            orderDetailsArea.clear();
            cancelOrderButton.setDisable(true);
            showMessage("Success", "Order ID " + selectedOrder.getOrderId() + " has been canceled.");
        } else {
            showError("Error", "No order selected to cancel.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
