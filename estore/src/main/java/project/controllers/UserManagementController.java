package project.controllers;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import project.Admin;
import project.Customer;
import project.Database;

public class UserManagementController {

    @FXML
    private TableView<Admin> adminsTable;

    @FXML
    private TableColumn<Admin, String> adminUsernameColumn;
    @FXML
    private TableColumn<Admin, String> adminRoleColumn;
    @FXML
    private TableColumn<Admin, Integer> adminWorkingHoursColumn;

    @FXML
    private TableView<Customer> customersTable;

    @FXML
    private TableColumn<Customer, String> customerUsernameColumn;
    @FXML
    private TableColumn<Customer, String> customerGenderColumn;
    @FXML
    private TableColumn<Customer, Double> customerBalanceColumn;

    @FXML
    private Button deleteAdminButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    public void initialize() {
        setupTableColumns();
        populateTables();
        configureDeleteButtons();
    }

    private void setupTableColumns() {
        adminUsernameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("username"));
        adminRoleColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("role"));
        adminWorkingHoursColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("workingHours"));

        customerUsernameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("username"));
        customerGenderColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("gender"));
        customerBalanceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("balance"));
    }

    private void populateTables() {
        ObservableList<Admin> admins = FXCollections.observableArrayList(Database.admins);
        adminsTable.setItems(admins);

        ObservableList<Customer> customers = FXCollections.observableArrayList(Database.customers);
        customersTable.setItems(customers);

        deleteAdminButton.setDisable(true);
        deleteCustomerButton.setDisable(true);
    }

    private void configureDeleteButtons() {
        adminsTable.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            deleteAdminButton.setDisable(newSelection == null);
        });

        customersTable.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            deleteCustomerButton.setDisable(newSelection == null);
        });
    }

    @FXML
    public void deleteAdmin() {
        Admin selectedAdmin = adminsTable.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {
            Database.admins.remove(selectedAdmin);
            adminsTable.getItems().remove(selectedAdmin);
            showAlert("Success", "Admin '" + selectedAdmin.getUsername() + "' deleted successfully.");
        } else {
            showAlert("Error", "No admin selected.");
        }
    }

    @FXML
    public void deleteCustomer() {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            Database.customers.remove(selectedCustomer);
            customersTable.getItems().remove(selectedCustomer);
            showAlert("Success", "Customer '" + selectedCustomer.getUsername() + "' deleted successfully.");
        } else {
            showAlert("Error", "No customer selected.");
        }
    }

    @FXML
    public void registerAdminInline() {
        Dialog<Admin> dialog = new Dialog<>();
        dialog.setTitle("Register New Admin");
        dialog.setHeaderText("Enter Admin Details");

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        TextField dateOfBirthField = new TextField();
        dateOfBirthField.setPromptText("Date of Birth (dd-MM-yy)");
        TextField roleField = new TextField();
        roleField.setPromptText("Role");
        TextField workingHoursField = new TextField();
        workingHoursField.setPromptText("Working Hours");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Date of Birth:"), 0, 2);
        grid.add(dateOfBirthField, 1, 2);
        grid.add(new Label("Role:"), 0, 3);
        grid.add(roleField, 1, 3);
        grid.add(new Label("Working Hours:"), 0, 4);
        grid.add(workingHoursField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                try {
                    String username = usernameField.getText().trim();
                    String password = passwordField.getText().trim();
                    String dob = dateOfBirthField.getText().trim();
                    String role = roleField.getText().trim();
                    int workingHours = Integer.parseInt(workingHoursField.getText().trim());

                    if (username.isEmpty() || password.isEmpty() || dob.isEmpty() || role.isEmpty()) {
                        throw new IllegalArgumentException("All fields must be filled.");
                    }

                    boolean usernameExists = Database.admins.stream()
                            .anyMatch(admin -> admin.getUsername().equalsIgnoreCase(username));

                    if (usernameExists) {
                        throw new IllegalArgumentException("Username already exists. Please choose a different one.");
                    }

                    return new Admin(username, password, dob, role, workingHours);
                } catch (IllegalArgumentException e) {
                    showAlert("Error", "Invalid input: " + e.getMessage());
                }
            }
            return null;
        });

        Optional<Admin> result = dialog.showAndWait();

        result.ifPresent(newAdmin -> {
            Database.admins.add(newAdmin);
            adminsTable.getItems().add(newAdmin);
            Database.saveDatabaseToFile();
            showAlert("Success", "Admin '" + newAdmin.getUsername() + "' registered successfully.");
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
