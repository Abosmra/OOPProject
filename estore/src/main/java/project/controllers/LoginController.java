package project.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import project.Admin;
import project.Customer;
import project.Database;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private int failedAttempts = 0;
    private final int MAX_ATTEMPTS = 3;

    @FXML
    private void initialize() {
        try {
            usernameField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    passwordField.requestFocus();
                }
            });

            passwordField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    login();
                }
            });
        } catch (Exception e) {
            showMessage("Error", "An error occurred during initialization: " + e.getMessage());
        }
    }

    @FXML
    public void login() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showMessage("Error", "Username and password cannot be empty.");
                return;
            }

            Object user = authenticateUser(username, password);

              if (user instanceof Admin admin) {
                  showMessage("Success", "Welcome Admin " + admin.getUsername() + "!");
                  loadScene("/project/admin/adminDashboard.fxml", admin);
                  resetFailedAttempts();
              } else if (user instanceof Customer customer) {
                  showMessage("Success", "Welcome " + customer.getUsername() + "!");
                  loadScene("/project/customer/customerDashboard.fxml", customer);
                  resetFailedAttempts();
              } else {
                  handleFailedLogin();
              }
        } catch (Exception e) {
            showMessage("Error", "An unexpected error occurred during login: " + e.getMessage());
        }
    }

    private Object authenticateUser(String username, String password) {
        try {
            if (Database.admins == null || Database.customers == null) {
                showMessage("Error", "Database is not initialized.");
                return null;
            }

            Admin admin = Database.admins.stream()
                    .filter(a -> a.getUsername().equals(username) && a.getPassword().equals(password))
                    .findFirst()
                    .orElse(null);

            if (admin != null) {
                return admin;
            }

            return Database.customers.stream()
                    .filter(c -> c.getUsername().equals(username) && c.getPassword().equals(password))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            showMessage("Error", "An error occurred during authentication: " + e.getMessage());
            return null;
        }
    }

    private void handleFailedLogin() {
        failedAttempts++;
        if (failedAttempts >= MAX_ATTEMPTS) {
            showMessage("Error", "Too many failed attempts. Login disabled temporarily.");
            goBack();
        } else {
            showMessage("Error", "Invalid credentials! Attempts left: " + (MAX_ATTEMPTS - failedAttempts));
        }
    }

    private void resetFailedAttempts() {
        failedAttempts = 0;
    }

    private void loadScene(String fxmlPath, Object controllerData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if (controllerData != null) {
                Object controller = loader.getController();
                if (controller instanceof AdminDashboardController adminController && controllerData instanceof Admin admin) {
                    adminController.setAdmin(admin);
                } else if (controller instanceof CustomerDashboardController customerController && controllerData instanceof Customer customer) {
                    customerController.setCustomer(customer);
                }
            }

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showMessage("Error", "Failed to load the scene: " + fxmlPath + ". " + e.getMessage());
        }
    }

    @FXML
    public void goBack() {
        try {
            loadScene("/project/MainMenu.fxml", null);
        } catch (Exception e) {
            showMessage("Error", "Failed to navigate back to the main menu: " + e.getMessage());
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
