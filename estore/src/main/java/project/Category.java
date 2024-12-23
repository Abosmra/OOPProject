package project;

import java.util.ArrayList;

public class Category {

    private int categoryId;
    private String name;
    private ArrayList<Product> products;

    public Category(int categoryId, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }
        this.categoryId = categoryId;
        this.name = name.trim();
        this.products = new ArrayList<>();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }
        this.name = name.trim();
    }

    public void setProducts(ArrayList<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("Products list cannot be null.");
        }
        this.products = products;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        products.add(product);
    }

}
