package project.controllers;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.Cart;
import project.Order;
import project.Order.PaymentMethod;

public class CardPaymentController {

    @FXML
    private TextField cardNumberField;

    @FXML
    private Button confirmPaymentButton;

    @FXML
    private TextField cvvField;

    @FXML
    private TextField expirationDateField;

    private Cart cart;
    private Stage stage;

    @FXML
    public void initialize() {}

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void confirmPayment() {
        String cardNumber = cardNumberField.getText().trim();
        String cvv = cvvField.getText().trim();
        String expirationDateInput = expirationDateField.getText().trim();

        if (validateCardDetails(cardNumber, cvv, expirationDateInput)) {
            try {
                PaymentMethod chosenMethod = Order.PaymentMethod.CARD;
                Order order = new Order(cart.getProducts(), chosenMethod);
                Order.addOrder(order);

                showSuccessAlert("Order placed successfully with Card!");
                updateView(); // Clear cart and close stage
            } catch (Exception e) {
                showAlert("Failed to place order. " + e.getMessage());
            }
        }
    }

    private boolean validateCardDetails(String cardNumber, String cvv, String expirationDateInput) {
        if (!cardNumber.matches("\\d{16}")) {
            showAlert("Invalid card number. Please enter 16 digits.");
            return false;
        }

        if (!cvv.matches("\\d{3}")) {
            showAlert("Invalid CVV. Please enter 3 digits.");
            return false;
        }

        try {
            YearMonth expirationDate = YearMonth.parse(expirationDateInput, DateTimeFormatter.ofPattern("MM/yyyy"));
            if (expirationDate.isBefore(YearMonth.now())) {
                showAlert("The card is already expired.");
                return false;
            }
        } catch (DateTimeParseException e) {
            showAlert("Invalid expiration date format. Please use MM/YYYY.");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateView() {
        if (cart != null) {
            cart.clearCart(); // Clear the cart
        }
        if (stage != null) {
            stage.close(); // Close the stage
        }
    }
    
}
