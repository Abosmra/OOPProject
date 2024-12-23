package project;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Database.loadDatabaseFromFile();

        if (Database.categories.isEmpty() && Database.admins.isEmpty() && Database.customers.isEmpty()) {
            Database.initializeSampleData();
            System.out.println("Sample data initialized.");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(Database::saveDatabaseToFile));

        try {
            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            primaryStage.initStyle(StageStyle.UNIFIED);
            primaryStage.setTitle("Store");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
            primaryStage.setScene(new Scene(root, 600, 500));
            primaryStage.show();
            primaryStage.setResizable(false);

        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
