package project.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    public void showLogin() {
        loadScene("login.fxml");
    }

    @FXML
    public void showRegister() {
        loadScene("register.fxml");
    }

    @FXML
    public void exitApplication() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            System.exit(0);
        }
    }

    private void loadScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/project/" + fxmlFile));
            Stage stage = (Stage) Stage.getWindows().get(0);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
        }
    }
}
