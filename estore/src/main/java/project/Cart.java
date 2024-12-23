package project;

import java.util.ArrayList;

public class Cart {

    private ArrayList<Product> products;
    public double totalCost;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public int getCartSize() {
        return products.size();
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = new ArrayList<>(products);
    }

    public void addProduct(Product product) {
        products.add(product);
        totalCost += product.getPrice();
    }

    public void removeProduct(Product product) {
        products.remove(product);
        totalCost -= product.getPrice();
        product.setStock(product.getStock() + 1);
    }

    public void clearCart() {
        products.clear();
        totalCost = 0;
    }

}
