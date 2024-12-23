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
import project.Admin;

public class AdminDashboardController {

    private Admin currentAdmin;

    @FXML
    private BorderPane mainPane;

    @FXML
    private HBox productManagementButton;

    @FXML
    private HBox userManagementButton;

    @FXML
    private HBox orderManagementButton;

    @FXML
    private HBox infoButton;

    @FXML
    private HBox logoutButton;

    private HBox currentActiveButton;

    @FXML
    public void initialize() {
        // Set default active button (e.g., Product Management)
        setActiveButton(productManagementButton);
    }

    public void setAdmin(Admin admin) {
        this.currentAdmin = admin;
        if (mainPane != null) {
            showDashboardPage(); // Load the default dashboard page
        } else {
            System.err.println("MainPane is not initialized. Check FXML bindings.");
        }
    }

    private void setActiveButton(HBox button) {
        // Reset styles for all buttons
        resetButtonStyles();

        // Highlight the selected button
        currentActiveButton = button;
        currentActiveButton.setStyle("-fx-background-color: #f4f4fc; -fx-background-radius: 50 0 0 50;");
    }

    private void resetButtonStyles() {
        productManagementButton.setStyle("-fx-background-color: #ffffff;");
        userManagementButton.setStyle("-fx-background-color: #ffffff;");
        orderManagementButton.setStyle("-fx-background-color: #ffffff;");
        infoButton.setStyle("-fx-background-color: #ffffff;");
        logoutButton.setStyle("-fx-background-color: #ffffff;");
    }

    @FXML
    public void showDashboardPage() {
        setActiveButton(productManagementButton);
        loadPage("/project/admin/ProductManagementView.fxml", "Product Management");
    }

    @FXML
    public void showAddProductPage() {
        setActiveButton(productManagementButton);
        loadPage("/project/admin/ProductManagementView.fxml", "Product Management");
    }

    @FXML
    public void showUserManagementPage() {
        setActiveButton(userManagementButton);
        loadPage("/project/admin/UserManagementView.fxml", "User Management");
    }

    @FXML
    public void showOrderManagementPage() {
        setActiveButton(orderManagementButton);
        loadPage("/project/admin/OrderManagementView.fxml", "Order Management");
    }

    @FXML
    public void showInfoPage() {
        setActiveButton(infoButton);
        loadPage("/project/admin/InfoView.fxml", "View Info");
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

                currentAdmin = null;
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
            if (controller instanceof InfoController && currentAdmin != null) {
                ((InfoController) controller).setAdmin(currentAdmin);
            }

            mainPane.setCenter(page); // Load the content into the center of the BorderPane
        } catch (IOException e) {
            System.err.println("Error loading page: " + fxmlPath);
            showMessage("Error", "Unable to load " + pageTitle + " page. Error: " + e.getMessage());
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
