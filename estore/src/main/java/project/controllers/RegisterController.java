package project.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.Customer;
import project.Database;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private TextField dobField;

    @FXML
    private TextField balanceField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField interestsField;

    @FXML
    public void initialize() {
        try {
            genderChoiceBox.getItems().addAll("MALE", "FEMALE");
            genderChoiceBox.setValue("MALE");
        } catch (Exception e) {
            showAlert("Error", "Failed to initialize gender options.");
        }
    }

    @FXML
    public void register() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String gender = genderChoiceBox.getValue();
            String dob = dobField.getText().trim();
            String balanceText = balanceField.getText().trim();
            String address = addressField.getText().trim();
            String interests = interestsField.getText().trim();

            // Validate inputs using the User class
            if (address.isEmpty()) {
                throw new IllegalArgumentException("Address cannot be empty.");
            }
            if (interests.isEmpty()) {
                throw new IllegalArgumentException("Interests cannot be empty.");
            }

            double balance;
            try {
                balance = Double.parseDouble(balanceText);
                if (balance < 0) {
                    throw new IllegalArgumentException("Balance cannot be negative.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid balance. Please enter a numeric value.");
            }

            // Validate username, password, and date of birth using User class logic
            Customer customer = new Customer(username, password, gender, dob, balance, address, interests);

            // Check if username already exists
            if (Database.customers.stream().anyMatch(c -> c.getUsername().equalsIgnoreCase(username))) {
                throw new IllegalArgumentException("Username already exists. Please choose a different username.");
            }

            // Add new customer to the database
            Database.customers.add(customer);
            showAlert("Success", "Registration successful!");
            goBack();
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred during registration.");
        }
    }

    @FXML
    public void goBack() {
        try {
            loadScene("MainMenu.fxml");
        } catch (Exception e) {
            showAlert("Error", "Failed to navigate back to the main menu.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/project/" + fxmlFile));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert("Error", "Failed to load the scene.");
        }
    }
}
