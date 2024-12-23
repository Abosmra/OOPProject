package project.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import project.Customer;
import project.Database;

public class CustomerViewInfoController {

    private Customer currentCustomer;

    @FXML
    private TextField username_text, dob_text, address_text, interests_text, balance_text;

    @FXML
    private ChoiceBox<String> gender_ChoiceBox;

    @FXML
    private void initialize() {
        gender_ChoiceBox.setItems(FXCollections.observableArrayList("MALE", "FEMALE"));
    }

    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
        populateFields();
    }

    private void populateFields() {
        if (currentCustomer != null) {
            username_text.setText(currentCustomer.getUsername());
            dob_text.setText(currentCustomer.getDateOfBirth());
            gender_ChoiceBox.setValue(currentCustomer.getGender() != null ? currentCustomer.getGender().toString() : null);
            address_text.setText(currentCustomer.getAddress());
            interests_text.setText(currentCustomer.getInterests());
            balance_text.setText(String.valueOf(currentCustomer.getBalance()));
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        username_text.setText("");
        dob_text.setText("");
        gender_ChoiceBox.setValue(null);
        address_text.setText("");
        interests_text.setText("");
        balance_text.setText("");
    }

    @FXML
    private void updateInfo() {
        try {
            if (currentCustomer == null) {
                showAlert(Alert.AlertType.WARNING, "Update Failed", "No customer is selected.", null);
                return;
            }

            String newUsername = username_text.getText().trim();
            if (newUsername.isEmpty()
                    || dob_text.getText().trim().isEmpty()
                    || gender_ChoiceBox.getValue() == null
                    || address_text.getText().trim().isEmpty()
                    || interests_text.getText().trim().isEmpty()
                    || balance_text.getText().trim().isEmpty()) {

                showAlert(Alert.AlertType.WARNING, "Update Failed", "All fields must be filled.", null);
                return;
            }

            if (!newUsername.equals(currentCustomer.getUsername()) &&
                Database.customers.stream().anyMatch(customer -> customer.getUsername().equalsIgnoreCase(newUsername))) {
                showAlert(Alert.AlertType.WARNING, "Update Failed", "Username already exists. Please choose a different username.", null);
                return;
            }

            currentCustomer.setUsername(newUsername);
            currentCustomer.setDateOfBirth(dob_text.getText().trim());
            currentCustomer.setGender(Customer.Gender.valueOf(gender_ChoiceBox.getValue()));
            currentCustomer.setAddress(address_text.getText().trim());
            currentCustomer.setInterests(interests_text.getText().trim());
            currentCustomer.setBalance(Double.parseDouble(balance_text.getText().trim()));

            Database.saveDatabaseToFile();

            populateFields();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer information updated successfully.", null);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Invalid balance format. Please enter a valid number.", e.getMessage());
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Invalid input for gender. Please select a valid option.", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update customer information. Please check your input.", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
