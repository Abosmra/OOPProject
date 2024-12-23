package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import project.Admin;
import project.Database;

public class InfoController {

    private Admin currentAdmin;

    @FXML
    private TextField usernameText;

    @FXML
    private TextField dobText;

    @FXML
    private TextField roleText;

    @FXML
    private TextField hoursText;

    public void setAdmin(Admin admin) {
        this.currentAdmin = admin;
        populateFields();
    }

    private void populateFields() {
        if (currentAdmin != null) {
            usernameText.setText(currentAdmin.getUsername());
            dobText.setText(currentAdmin.getDateOfBirth());
            roleText.setText(currentAdmin.getRole());
            hoursText.setText(String.valueOf(currentAdmin.getWorkingHours()));
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        usernameText.setText("");
        dobText.setText("");
        roleText.setText("");
        hoursText.setText("");
    }

    @FXML
    private void updateInfo() {
        try {
            if (currentAdmin == null) {
                showAlert(Alert.AlertType.WARNING, "Update Failed", "No admin is selected.", null);
                return;
            }

            String newUsername = usernameText.getText().trim();
            if (newUsername.isEmpty()
                    || dobText.getText().trim().isEmpty()
                    || roleText.getText().trim().isEmpty()
                    || hoursText.getText().trim().isEmpty()) {

                showAlert(Alert.AlertType.WARNING, "Update Failed", "All fields must be filled.", null);
                return;
            }

            if (!newUsername.equals(currentAdmin.getUsername()) &&
                Database.admins.stream().anyMatch(admin -> admin.getUsername().equalsIgnoreCase(newUsername))) {
                showAlert(Alert.AlertType.WARNING, "Update Failed", "Username already exists. Please choose a different username.", null);
                return;
            }

            currentAdmin.setUsername(newUsername);
            currentAdmin.setDateOfBirth(dobText.getText().trim());
            currentAdmin.setRole(roleText.getText().trim());
            currentAdmin.setWorkingHours(Integer.parseInt(hoursText.getText().trim()));

            Database.saveDatabaseToFile();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Admin information updated successfully.", null);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Invalid working hours format. Please enter a valid number.", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update admin information. Please check your input.", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message, String details) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        if (details != null) {
            alert.setContentText(details);
        }
        alert.showAndWait();
    }
}
