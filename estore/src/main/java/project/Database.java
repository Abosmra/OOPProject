package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static ArrayList<Customer> customers = new ArrayList<>();
    public static ArrayList<Admin> admins = new ArrayList<>();
    public static ArrayList<Category> categories = new ArrayList<>();
    public static ArrayList<Product> products = new ArrayList<>();
    public static ArrayList<Order> orders = new ArrayList<>();

    private static final String DATABASE_FILE = "storeDatabase.txt";

    public static void initializeSampleData() {
        Category electronics = new Category(1, "Electronics");
        electronics.addProduct(new Product("Laptop", 1000.0, "Electronics", 10));
        electronics.addProduct(new Product("Smartphone", 700.0, "Electronics", 15));
        electronics.addProduct(new Product("Headphones", 150.0, "Electronics", 25));
        electronics.addProduct(new Product("Smartwatch", 200.0, "Electronics", 30));
        electronics.addProduct(new Product("Gaming Console", 500.0, "Electronics", 8));
        electronics.addProduct(new Product("Bluetooth Speaker", 120.0, "Electronics", 20));
        electronics.addProduct(new Product("Camera", 900.0, "Electronics", 5));
        electronics.addProduct(new Product("Monitor", 250.0, "Electronics", 12));
        electronics.addProduct(new Product("Keyboard", 80.0, "Electronics", 18));

        Category groceries = new Category(2, "Groceries");
        groceries.addProduct(new Product("Rice", 2.0, "Groceries", 100));
        groceries.addProduct(new Product("Milk", 1.5, "Groceries", 50));
        groceries.addProduct(new Product("Bread", 1.0, "Groceries", 60));
        groceries.addProduct(new Product("Eggs", 3.0, "Groceries", 30));
        groceries.addProduct(new Product("Cheese", 4.0, "Groceries", 40));
        groceries.addProduct(new Product("Apples", 2.5, "Groceries", 80));
        groceries.addProduct(new Product("Bananas", 1.8, "Groceries", 70));
        groceries.addProduct(new Product("Tomatoes", 3.0, "Groceries", 90));
        groceries.addProduct(new Product("Potatoes", 2.0, "Groceries", 100));

        Category clothing = new Category(3, "Clothing");
        clothing.addProduct(new Product("T-Shirt", 20.0, "Clothing", 50));
        clothing.addProduct(new Product("Jeans", 40.0, "Clothing", 30));
        clothing.addProduct(new Product("Jacket", 60.0, "Clothing", 20));
        clothing.addProduct(new Product("Sweater", 50.0, "Clothing", 25));
        clothing.addProduct(new Product("Sneakers", 80.0, "Clothing", 40));
        clothing.addProduct(new Product("Hat", 15.0, "Clothing", 60));
        clothing.addProduct(new Product("Socks", 5.0, "Clothing", 100));
        clothing.addProduct(new Product("Scarf", 25.0, "Clothing", 35));
        clothing.addProduct(new Product("Gloves", 30.0, "Clothing", 25));

        Category beauty = new Category(4, "Beauty");
        beauty.addProduct(new Product("Lipstick", 15.0, "Beauty", 50));
        beauty.addProduct(new Product("Foundation", 30.0, "Beauty", 40));
        beauty.addProduct(new Product("Mascara", 20.0, "Beauty", 60));
        beauty.addProduct(new Product("Perfume", 50.0, "Beauty", 30));
        beauty.addProduct(new Product("Shampoo", 10.0, "Beauty", 80));
        beauty.addProduct(new Product("Conditioner", 10.0, "Beauty", 75));
        beauty.addProduct(new Product("Moisturizer", 25.0, "Beauty", 40));
        beauty.addProduct(new Product("Sunscreen", 20.0, "Beauty", 45));
        beauty.addProduct(new Product("Nail Polish", 10.0, "Beauty", 50));

        Category sports = new Category(5, "Sports");
        sports.addProduct(new Product("Football", 25.0, "Sports", 30));
        sports.addProduct(new Product("Basketball", 30.0, "Sports", 20));
        sports.addProduct(new Product("Tennis Racket", 60.0, "Sports", 10));
        sports.addProduct(new Product("Yoga Mat", 20.0, "Sports", 40));
        sports.addProduct(new Product("Dumbbells", 50.0, "Sports", 25));
        sports.addProduct(new Product("Cycling Helmet", 40.0, "Sports", 15));
        sports.addProduct(new Product("Running Shoes", 70.0, "Sports", 20));
        sports.addProduct(new Product("Skipping Rope", 15.0, "Sports", 60));
        sports.addProduct(new Product("Swim Goggles", 10.0, "Sports", 50));

        Category books = new Category(6, "Books");
        books.addProduct(new Product("Novel", 15.0, "Books", 50));
        books.addProduct(new Product("Biography", 20.0, "Books", 40));
        books.addProduct(new Product("Science Fiction", 18.0, "Books", 30));
        books.addProduct(new Product("Mystery", 22.0, "Books", 25));
        books.addProduct(new Product("Fantasy", 25.0, "Books", 20));
        books.addProduct(new Product("Cookbook", 30.0, "Books", 15));
        books.addProduct(new Product("Travel Guide", 35.0, "Books", 10));
        books.addProduct(new Product("Self-Help", 20.0, "Books", 35));
        books.addProduct(new Product("History", 28.0, "Books", 12));

        categories.add(electronics);
        categories.add(groceries);
        categories.add(clothing);
        categories.add(beauty);
        categories.add(sports);
        categories.add(books);

        admins.add(new Admin("Mohamed", "Mohamed123", "11-10-05", "Owner", 2000));
        admins.add(new Admin("Mariam", "Mariam123", "16-10-05", "Super Admin", 2000));
        admins.add(new Admin("Nada", "Nada123", "29-09-05", "Super Admin", 2));
        admins.add(new Admin("Zeyad", "Zeyad123", "15-10-05", "Super Admin", 2));

        customers.add(new Customer("c1", "123123", "MALE", "01-01-2000", 500.0, "123 Street", "Gaming"));
        customers.add(new Customer("c2", "123123", "FEMALE", "01-01-2000", 500.0, "123 Street", "Beauty"));
        customers.add(new Customer("c3", "123123", "MALE", "01-01-2000", 500.0, "123 Street", "Sports"));
        customers.add(new Customer("c4", "123123", "FEMALE", "01-01-2000", 500.0, "123 Street", "Reading"));

    }

    public static void saveDatabaseToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE))) {
            for (Category category : categories) {
                writer.write("CATEGORY:" + category.getCategoryId() + "," + category.getName());
                writer.newLine();
                for (Product product : category.getProducts()) {
                    writer.write("PRODUCT:" + product.getName() + "," + product.getPrice() + "," + product.getCategory() + "," + product.getStock());
                    writer.newLine();
                }
            }
            for (Admin admin : admins) {
                writer.write("ADMIN:" + admin.getUsername() + "," + admin.getPassword() + ","
                        + admin.getDateOfBirth() + "," + admin.getRole() + "," + admin.getWorkingHours());
                writer.newLine();
            }

            for (Customer customer : customers) {
                writer.write("CUSTOMER:" + customer.getUsername() + "," + customer.getPassword() + "," + customer.getGender() + "," + customer.getDateOfBirth() + "," + customer.getBalance() + "," + customer.getAddress() + "," + customer.getInterests());
                writer.newLine();
            }
            for (Order order : orders) {
                writer.write("ORDER:" + order.toSaveString());
                writer.newLine();
            }
            System.out.println("Database saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving database: " + e.getMessage());
        }
    }

    public static void loadDatabaseFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("CATEGORY:")) {
                    String[] parts = line.substring(9).split(",");
                    categories.add(new Category(Integer.parseInt(parts[0]), parts[1]));
                } else if (line.startsWith("PRODUCT:")) {
                    String[] parts = line.substring(8).split(",");
                    Product product = new Product(parts[0], Double.parseDouble(parts[1]), parts[2], Integer.parseInt(parts[3]));
                    for (Category category : categories) {
                        if (category.getName().equals(product.getCategory())) {
                            category.addProduct(product);
                            break;
                        }
                    }
                } else if (line.startsWith("ADMIN:")) {
                    String[] parts = line.substring(6).split(",");
                    admins.add(new Admin(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4])));
                } else if (line.startsWith("CUSTOMER:")) {
                    String[] parts = line.substring(9).split(",");
                    customers.add(new Customer(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4]), parts[5], parts[6]));
                } else if (line.startsWith("ORDER:")) {
                    String[] parts = line.substring(6).split(",");
                    orders.add(Order.fromSaveString(parts));
                }
            }
            System.out.println("Database loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading database: " + e.getMessage());
        }
    }

    public static List<String> loadFileLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading the database file: " + e.getMessage());
        }
        return lines;
    }

}
