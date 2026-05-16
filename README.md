# JavaFX E-Store

A desktop e-commerce store built with **Java 23** and **JavaFX 23**, demonstrating
core object-oriented design (inheritance, encapsulation, polymorphism) across a
small but complete admin/customer storefront.

## Features

### Customer
- Register an account and log in
- Browse products grouped into 6 categories (Electronics, Groceries, Clothing, Beauty, Sports, Books)
- Add items to a cart, adjust quantities, and check out
- Pay with the in-app card payment screen (balance-backed wallet)
- View and edit personal information

### Admin
- Log in to a dedicated admin dashboard
- **Product management** — create, edit, delete products and stock
- **User management** — view, edit, delete customer accounts
- **Order management** — view and manage placed orders
- **Info view** — inspect store records

### Persistence
- All state (users, admins, categories, products, orders) is serialized to a
  flat-file database (`storeDatabase.txt`) on shutdown and reloaded on launch
- Sample data is auto-seeded on first run

## Project layout

```
estore/
├── pom.xml
└── src/main/
    ├── java/project/
    │   ├── Main.java              # JavaFX entrypoint
    │   ├── Database.java          # In-memory store + file persistence
    │   ├── User, Admin, Customer  # Domain model (User is abstract)
    │   ├── Category, Product
    │   ├── Cart, Order
    │   └── controllers/           # One FXML controller per view
    └── resources/project/
        ├── *.fxml                 # Login / register / main menu
        ├── admin/*.fxml           # Admin views
        ├── customer/*.fxml        # Customer views
        └── img/                   # UI icons
```

## Requirements

- JDK 23+
- Maven 3.8+

## Running

```bash
cd estore
mvn javafx:run
```

Or build a jar:

```bash
mvn clean package
```

## Default accounts

Seeded on first launch:

| Role     | Username | Password    |
|----------|----------|-------------|
| Admin    | Mohamed  | Mohamed123  |
| Admin    | Mariam   | Mariam123   |
| Customer | c1       | 123123      |
| Customer | c2       | 123123      |
