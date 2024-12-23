package project;

public class Product {

    private String name;
    private double price;
    private int stock;
    private String category;

    public Product(String name, double price, String category, int stock) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }

        this.name = name.trim();
        this.price = price;
        this.stock = stock;
        this.category = category.trim();
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        this.name = name.trim();
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.stock = stock;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        this.category = category.trim();
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getCategory() {
        return category;
    }

    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (quantity > stock) {
            throw new RuntimeException("Not enough stock available for " + name + ".");
        }
        stock -= quantity;
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        stock += quantity;
        System.out.println(quantity + " " + name + "(s) have been added to stock.");
    }

    public boolean isAvailable() {
        return stock > 0;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nPrice: $%.2f\nCategory: %s\nStock: %d\n",
                name, price, category, stock);
    }
}
