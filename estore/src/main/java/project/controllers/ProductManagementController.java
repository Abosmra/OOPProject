package project.controllers;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import project.Category;
import project.Database;
import project.Product;

public class ProductManagementController {

    @FXML
    private TextField productField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField stockField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private ComboBox<String> deleteCategoryComboBox;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, Integer> stockColumn;

    @FXML
    private Button deleteProductButton;

    private ObservableList<Product> allProducts;
    private static final String DEFAULT_CATEGORY = "Select Category";
    private static final String ADD_NEW_CATEGORY = "Add New Category";

    @FXML
    public void initialize() {
        setupProductTable();
        loadCategories();
        loadCategoriesIntoDeleteComboBox();
        configureCategoryComboBox();

        deleteProductButton.setDisable(true);
        productsTable.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            deleteProductButton.setDisable(newSelection == null);
        });
    }

    private void setupProductTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        allProducts = FXCollections.observableArrayList(loadAllProductsFromCategories());
        productsTable.setItems(allProducts);
    }

    private ObservableList<Product> loadAllProductsFromCategories() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        for (Category category : Database.categories) {
            products.addAll(category.getProducts());
        }
        return products;
    }

    private void loadCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList();
        categories.add(DEFAULT_CATEGORY);
        Database.categories.forEach(category -> categories.add(category.getName()));
        categories.add(ADD_NEW_CATEGORY);
        categoryComboBox.setItems(categories);
        categoryComboBox.getSelectionModel().select(DEFAULT_CATEGORY);
    }

    private void configureCategoryComboBox() {
        categoryComboBox.setOnAction(_ -> {
            if (ADD_NEW_CATEGORY.equals(categoryComboBox.getValue())) {
                addNewCategoryInline();
            }
        });
    }

    private void addNewCategoryInline() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Category");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter category name:");

        dialog.showAndWait().ifPresent(input -> {
            String categoryName = input.trim();
            if (categoryName.isEmpty()) {
                showMessage("Error", "Category name cannot be empty.");
            } else if (Database.categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(categoryName))) {
                showMessage("Error", "Category already exists.");
            } else {
                Category newCategory = new Category(Database.categories.size() + 1, categoryName);
                Database.categories.add(newCategory);
                Database.saveDatabaseToFile();
                showMessage("Success", "Category '" + categoryName + "' added successfully.");
                loadCategories();
                loadCategoriesIntoDeleteComboBox();
                categoryComboBox.getSelectionModel().select(categoryName);
            }
        });
    }

    private void loadCategoriesIntoDeleteComboBox() {
        ObservableList<String> categoryNames = FXCollections.observableArrayList();
        Database.categories.forEach(category -> categoryNames.add(category.getName()));
        deleteCategoryComboBox.setItems(categoryNames);
    }

    @FXML
    private void addProduct() {
        String productName = productField.getText().trim();
        String productPrice = priceField.getText().trim();
        String productStock = stockField.getText().trim();
        String categoryName = categoryComboBox.getValue();

        if (productName.isEmpty() || productPrice.isEmpty() || productStock.isEmpty()) {
            showMessage("Error", "Product name, price, and stock cannot be empty.");
        } else if (DEFAULT_CATEGORY.equals(categoryName) || ADD_NEW_CATEGORY.equals(categoryName)) {
            showMessage("Error", "Please choose a valid category.");
        } else {
            try {
                double price = Double.parseDouble(productPrice);
                int stock = Integer.parseInt(productStock);

                Category category = Database.categories.stream()
                        .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                        .findFirst()
                        .orElse(null);

                if (category != null) {
                    Product existingProduct = category.getProducts().stream()
                            .filter(p -> p.getName().equalsIgnoreCase(productName))
                            .findFirst()
                            .orElse(null);

                    if (existingProduct != null) {
                        existingProduct.setPrice(price);
                        existingProduct.setStock(stock);
                        showMessage("Success", "Product '" + productName + "' updated successfully.");
                    } else {
                        Product newProduct = new Product(productName, price, category.getName(), stock);
                        category.addProduct(newProduct);
                        showMessage("Success", "Product '" + productName + "' added successfully.");
                    }

                    Database.saveDatabaseToFile();
                    productField.clear();
                    priceField.clear();
                    stockField.clear();
                    categoryComboBox.getSelectionModel().select(DEFAULT_CATEGORY);
                    refreshProducts();
                } else {
                    showMessage("Error", "Category not found.");
                }
            } catch (NumberFormatException e) {
                showMessage("Error", "Invalid price or stock. Please enter numeric values.");
            }
        }
    }

    @FXML
    private void deleteSelectedProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showMessage("Error", "Please select a product to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Product Confirmation");
        confirmation.setHeaderText("Are you sure you want to delete the product: " + selectedProduct.getName() + "?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Category category = Database.categories.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(selectedProduct.getCategory()))
                    .findFirst()
                    .orElse(null);

            if (category != null) {
                category.getProducts().remove(selectedProduct);
                Database.saveDatabaseToFile();
                showMessage("Success", "Product '" + selectedProduct.getName() + "' has been deleted.");
                refreshProducts();
            }
        }
    }

    @FXML
    private void deleteSelectedCategory() {
        String selectedCategoryName = deleteCategoryComboBox.getValue();

        if (selectedCategoryName == null) {
            showMessage("Error", "Please select a category to delete.");
            return;
        }

        Category selectedCategory = Database.categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase(selectedCategoryName))
                .findFirst()
                .orElse(null);

        if (selectedCategory != null) {
            StringBuilder productList = new StringBuilder();
            for (Product product : selectedCategory.getProducts()) {
                productList.append("- ").append(product.getName()).append("\n");
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Category Confirmation");
            confirmation.setHeaderText("Are you sure you want to delete the category: " + selectedCategoryName + "?");
            confirmation.setContentText("The following products will also be deleted:\n" + productList);

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Database.categories.remove(selectedCategory);
                Database.saveDatabaseToFile();
                showMessage("Success", "Category and its products have been deleted successfully.");
                refreshProducts();
                loadCategoriesIntoDeleteComboBox();
            }
        }
    }

    private void refreshProducts() {
        allProducts.setAll(loadAllProductsFromCategories());
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
