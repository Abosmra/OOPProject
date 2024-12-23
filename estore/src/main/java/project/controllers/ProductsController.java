package project.controllers;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.Cart;
import project.Category;
import project.Database;
import project.Product;

public class ProductsController {

    @FXML
    private VBox root;

    private Cart cart;
    private CartController cartController;

    private List<Category> categories = Database.categories;

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setCartController(CartController cartController) {
        this.cartController = cartController;
    }

    @FXML
    public void initialize() {
        for (Category category : categories) {
            VBox categoryRow = createCategoryRow(category);
            root.getChildren().add(categoryRow);
        }
    }

    private VBox createCategoryRow(Category category) {
        VBox categoryRow = new VBox(10);
        categoryRow.setStyle("-fx-padding: 20; -fx-background-color: #f7f7f7; -fx-border-color: #d3d3d3; -fx-border-width: 1;");
        categoryRow.setPrefWidth(1000);
        categoryRow.setPrefHeight(300);
        categoryRow.setMinHeight(300);
        categoryRow.setMaxHeight(300);

        Label categoryLabel = new Label(category.getName());
        categoryLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-padding: 10;");
        categoryRow.getChildren().add(categoryLabel);

        HBox productRow = new HBox(20);
        productRow.setStyle("-fx-padding: 20; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(productRow);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        for (Product product : category.getProducts()) {
            VBox productBox = createProductBox(product);
            productRow.getChildren().add(productBox);
        }

        categoryRow.getChildren().add(scrollPane);

        return categoryRow;
    }

    private VBox createProductBox(Product product) {
        VBox productBox = new VBox(10);
        productBox.setStyle("-fx-padding: 15; -fx-background-color: #f9f9f9; "
                + "-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
        productBox.setPrefWidth(160);
        productBox.setPrefHeight(120);

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #444444;");

        Label priceLabel = new Label(String.format("Price: $%.2f", product.getPrice()));
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setPrefWidth(120);
        addToCartButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: #ffffff; -fx-border-radius: 5; -fx-background-radius: 5;");

        addToCartButton.setOnAction(_ -> {
            if (!product.isAvailable()) {
                showAlert("Out of Stock", product.getName() + " is currently out of stock.", Alert.AlertType.WARNING);
                return;
            }

            if (cartController != null) {
                cartController.addProductToCart(product);
            } else if (cart != null) {
                cart.addProduct(product);
            } else {
                showAlert("No Cart", "No cart is set. Cannot add product.", Alert.AlertType.ERROR);
                return;
            }
            product.setStock(product.getStock() - 1);
        });

        productBox.getChildren().addAll(nameLabel, priceLabel, addToCartButton);
        return productBox;
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
