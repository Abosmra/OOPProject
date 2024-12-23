package project.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import project.Cart;
import project.Customer;

public class CustomerDashboardController {

    private Customer currentCustomer;

    @FXML
    private BorderPane mainPane;

    @FXML
    private HBox ProductsButton;

    @FXML
    private HBox CartButton;

    @FXML
    private HBox infoButton;

    @FXML
    private HBox logoutButton;

    private HBox currentActiveButton;

    private Cart sharedCart = new Cart();

    @FXML
    private void initialize() {
        showDashboardPage();
    }

    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
        if (mainPane != null) {
            showDashboardPage();
        } else {
            System.err.println("mainPane is not initialized. Check FXML bindings.");
        }
    }

    private void setActiveButton(HBox button) {
        resetButtonStyles();
        currentActiveButton = button;
        currentActiveButton.setStyle("-fx-background-color: #f4f4fc; -fx-background-radius: 50 0 0 50;");
    }

    private void resetButtonStyles() {
        ProductsButton.setStyle("-fx-background-color: #ffffff;");
        CartButton.setStyle("-fx-background-color: #ffffff;");
        infoButton.setStyle("-fx-background-color: #ffffff;");
        logoutButton.setStyle("-fx-background-color: #ffffff;");
    }

    @FXML
    public void showDashboardPage() {
        setActiveButton(ProductsButton);
        loadPage("/project/customer/ProductsView.fxml", "Products");
    }

    @FXML
    public void showCartPage() {
        setActiveButton(CartButton);
        loadPage("/project/customer/CartView.fxml", "Cart");
    }

    @FXML
    public void showInfoPage() {
        setActiveButton(infoButton);
        loadPage("/project/customer/ViewInfo.fxml", "View Info");
    }

    @FXML
    public void logout() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to log out?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            setActiveButton(logoutButton);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/MainMenu.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) mainPane.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

                currentCustomer = null;
            } catch (IOException e) {
                showMessage("Error", "Unable to log out. Please try again.");
            }
        }
    }

    private void loadPage(String fxmlPath, String pageTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();

            Object controller = loader.getController();

            switch (controller) {
                case ProductsController productsController -> productsController.setCart(sharedCart);
                case CartController cartController -> cartController.setCart(sharedCart);
                case CustomerViewInfoController viewInfoController -> viewInfoController.setCustomer(currentCustomer);
                default -> {
                }
            }

            mainPane.setCenter(page);

        } catch (IOException e) {
            System.err.println("Error loading page: " + fxmlPath);
            showMessage("Error", "Unable to load " + pageTitle + " page.");
        }
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
