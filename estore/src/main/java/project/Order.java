package project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Order {

    private static int idCounter = initializeIdCounter();
    private int orderId;
    private List<Product> orderedProducts;
    private static PaymentMethod paymentMethod;

    public enum PaymentMethod {
        CASH,
        CARD,
        INSTAPAY;

        public static boolean isValid(String paymentMethod) {
            for (PaymentMethod method : PaymentMethod.values()) {
                if (method.name().equalsIgnoreCase(paymentMethod)) {
                    return true;
                }
            }
            return false;
        }
    }

    public Order(List<Product> products, PaymentMethod paymentMethod) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product.");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method cannot be empty.");
        }

        this.orderId = idCounter++;
        this.orderedProducts = new ArrayList<>(products);
        Order.paymentMethod = paymentMethod;
    }

    public Order(int orderId, List<Product> products, PaymentMethod paymentMethod) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product.");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method cannot be empty.");
        }
        this.orderId = orderId;
        this.orderedProducts = new ArrayList<>(products);
        Order.paymentMethod = paymentMethod;
    }

    private static int initializeIdCounter() {
        int maxOrderId = 0;
        for (String line : Database.loadFileLines()) {
            if (line.startsWith("ORDER:")) {
                String[] parts = line.split(",");
                try {
                    int currentId = Integer.parseInt(parts[0].substring(6));
                    maxOrderId = Math.max(maxOrderId, currentId);
                } catch (NumberFormatException e) {
                    System.out.println("Corrupted order ID encountered: " + parts[0]);
                }
            }
        }
        return maxOrderId + 1;
    }

    public int getOrderId() {
        return orderId;
    }

    public List<Product> getOrderedProducts() {
        return orderedProducts;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public static void addOrder(Order order) {
        Database.orders.add(order);
        Database.saveDatabaseToFile();
    }

    public static void checkOut(Cart cart, Scanner scanner) {
        Order order = new Order(new ArrayList<>(cart.getProducts()), paymentMethod);
        Order.addOrder(order);
        cart.clearCart();
    }

    public String toSaveString() {
        StringBuilder sb = new StringBuilder();
        sb.append(orderId).append(",");
        sb.append(paymentMethod.name()).append(",");
        for (Product product : orderedProducts) {
            sb.append(product.getName()).append(";").append(product.getPrice()).append(";")
                    .append(product.getCategory()).append(";").append(product.getStock()).append("|");
        }
        return sb.toString();
    }

    public static Order fromSaveString(String[] parts) {
        int id = Integer.parseInt(parts[0]);
        Order.paymentMethod = PaymentMethod.valueOf(parts[1]);
        List<Product> products = new ArrayList<>();
        for (String productData : parts[2].split("\\|")) {
            if (!productData.isEmpty()) {
                String[] productParts = productData.split(";");
                products.add(new Product(productParts[0], Double.parseDouble(productParts[1]), productParts[2], Integer.parseInt(productParts[3])));
            }
        }
        return new Order(id, products, paymentMethod);
    }
}
