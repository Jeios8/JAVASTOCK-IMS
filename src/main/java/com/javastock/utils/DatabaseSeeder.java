package main.java.com.javastock.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;

public class DatabaseSeeder {
    private final Connection connection;

    public DatabaseSeeder() {
        this.connection = DatabaseConnector.getConnection();
    }

    public void seedDatabase() {
        seedRoles();
        seedUsers();
        seedCategories();
        seedSuppliers();
        seedWarehouses();
        seedProducts();
        seedInventory();
        seedCustomers();
        closeConnection();
    }

    private void seedRoles() {
        String query = "INSERT INTO roles (role_id, role_name, description) VALUES (?, ?, ?)";
        Object[][] roles = {
                {1, "Admin", "Administrator with full access"},
                {2, "Manager", "Manages operations and staff"},
                {3, "Cashier", "Handles customer transactions"},
                {4, "Stock Manager", "Manages inventory and stock levels"},
                {5, "Viewer", "Read-only access"}
        };
        executeBatchInsert(query, roles);
    }

    private void seedUsers() {
        String query = "INSERT INTO users (username, password_hash, first_name, last_name, role_id) VALUES (?, ?, ?, ?, ?)";
        Object[][] users = {
                {"admin", Hasher.hashPassword("admin"), "John", "Doe", 1},
                {"manager", Hasher.hashPassword("manager"), "Jane", "Smith", 2},
                {"cashier1", Hasher.hashPassword("cashier1"), "Alice", "Brown", 3},
                {"stockman", Hasher.hashPassword("stockman"), "Bob", "Johnson", 4},
                {"viewer", Hasher.hashPassword("viewer"), "Charlie", "Davis", 5}
        };
        executeBatchInsert(query, users);
    }

    private void seedCategories() {
        String query = "INSERT INTO categories (category_id, category_name, is_active) VALUES (?, ?, ?)";
        Object[][] categories = {
                {1, "Fruits & Vegetables", true},
                {2, "Dairy & Eggs", true},
                {3, "Meat & Seafood", true},
                {4, "Beverages", true},
                {5, "Bakery & Bread", true},
                {6, "Frozen Foods", true},
                {7, "Pantry Staples", true},
                {8, "Household Essentials", true}
        };
        executeBatchInsert(query, categories);
    }

    private void seedSuppliers() {
        String query = "INSERT INTO suppliers (supplier_id, supplier_name, contact_name, phone, email, address, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[][] suppliers = {
                {1, "Fresh Farms", "Alice Johnson", "123-456-7890", "alice@freshfarms.com", "100 Market Street", true},
                {2, "Daily Dairy", "Bob Smith", "234-567-8901", "bob@dailydairy.com", "200 Milk Ave", true},
                {3, "Meat Masters", "Catherine Lee", "345-678-9012", "catherine@meatmasters.com", "300 Butcher Blvd", true},
                {4, "Beverage Distributors", "David Wright", "456-789-0123", "david@bevdistributors.com", "400 Drinks Lane", true},
                {5, "Bakery Delights", "Eva Green", "567-890-1234", "eva@bakerydelights.com", "500 Bread Circle", true}
        };
        executeBatchInsert(query, suppliers);
    }

    private void seedProducts() {
        String query = "INSERT INTO products (product_name, description, unit_of_measure, category_id, supplier_id, reorder_level, unit_price, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[][] products = {
                {"Apples", "Fresh red apples", "Kg", 1, 1, 20, 2.50, "images/apples.png"},
                {"Bananas", "Organic ripe bananas", "Kg", 1, 1, 15, 1.99, "images/bananas.png"},
                {"Carrots", "Fresh organic carrots", "Kg", 1, 1, 10, 1.50, "images/carrots.png"},
                {"Tomatoes", "Farm fresh tomatoes", "Kg", 1, 1, 12, 2.20, "images/tomatoes.png"},
                {"Potatoes", "Organic russet potatoes", "Kg", 1, 1, 18, 1.80, "images/potatoes.png"},
                {"Milk", "Organic whole milk", "Litre", 2, 2, 10, 1.99, "images/milk.png"},
                {"Yogurt", "Greek style yogurt", "Cup", 2, 2, 10, 3.50, "images/yogurt.png"},
                {"Cheese", "Cheddar cheese block", "Piece", 2, 2, 10, 4.99, "images/cheese.png"},
                {"Eggs", "Organic farm eggs", "Dozen", 2, 2, 10, 3.99, "images/eggs.png"},
                {"Butter", "Unsalted butter", "Pack", 2, 2, 8, 2.99, "images/butter.png"},
                {"Chicken Breast", "Boneless chicken breast", "Kg", 3, 3, 15, 5.49, "images/chicken_breast.png"},
                {"Ground Beef", "Lean ground beef", "Kg", 3, 3, 10, 8.99, "images/ground_beef.png"},
                {"Beef Steak", "Premium quality beef steak", "Kg", 3, 3, 12, 12.99, "images/beef_steak.png"},
                {"Salmon", "Fresh Atlantic salmon", "Kg", 3, 3, 8, 15.99, "images/salmon.png"},
                {"Shrimp", "Frozen jumbo shrimp", "Kg", 3, 3, 6, 14.99, "images/shrimp.png"},
                {"Orange Juice", "Freshly squeezed orange juice", "Litre", 4, 4, 8, 3.99, "images/orange_juice.png"},
                {"Soft Drinks", "Carbonated soft drink", "Bottle", 4, 4, 20, 1.50, "images/soft_drinks.png"},
                {"Bottled Water", "Mineral bottled water", "Bottle", 4, 4, 30, 0.99, "images/bottled_water.png"},
                {"Coffee", "Ground roasted coffee beans", "Pack", 4, 4, 12, 7.50, "images/coffee.png"},
                {"Tea", "Organic green tea", "Box", 4, 4, 10, 4.00, "images/tea.png"},
                {"Whole Wheat Bread", "Freshly baked whole wheat bread", "Piece", 5, 5, 12, 2.49, "images/bread.png"},
                {"Baguette", "French style baguette", "Piece", 5, 5, 10, 3.00, "images/baguette.png"},
                {"Bagels", "Assorted bagels pack", "Pack", 5, 5, 8, 3.50, "images/bagels.png"},
                {"Cookies", "Chocolate chip cookies", "Pack", 5, 5, 10, 2.99, "images/cookies.png"},
                {"Frozen Pizza", "Ready to cook frozen pizza", "Box", 6, 2, 10, 6.99, "images/frozen_pizza.png"},
                {"Ice Cream", "Vanilla ice cream tub", "Tub", 6, 2, 12, 5.49, "images/ice_cream.png"},
                {"Frozen Vegetables", "Mixed frozen vegetables", "Bag", 6, 2, 15, 4.99, "images/frozen_vegetables.png"},
                {"Frozen Fish", "Frozen white fish fillet", "Kg", 6, 2, 10, 8.50, "images/frozen_fish.png"},
                {"Frozen Berries", "Assorted frozen berries", "Bag", 6, 2, 10, 6.00, "images/frozen_berries.png"},
                {"Rice", "Premium quality rice", "Bag", 7, 3, 30, 20.00, "images/rice.png"},
                {"Pasta", "Durum wheat pasta", "Pack", 7, 3, 10, 2.99, "images/pasta.png"},
                {"Cooking Oil", "Sunflower cooking oil", "Bottle", 7, 3, 15, 5.50, "images/cooking_oil.png"},
                {"Tomato Sauce", "Homemade style tomato sauce", "Bottle", 7, 3, 10, 2.50, "images/tomato_sauce.png"},
                {"Canned Beans", "Organic canned beans", "Can", 7, 3, 12, 1.99, "images/canned_beans.png"},
                {"Toilet Paper", "Soft and strong toilet paper", "Pack", 8, 4, 20, 4.99, "images/toilet_paper.png"},
                {"Laundry Detergent", "Liquid laundry detergent", "Bottle", 8, 4, 10, 7.99, "images/laundry_detergent.png"},
                {"Dish Soap", "Lemon-scented dish soap", "Bottle", 8, 4, 12, 3.99, "images/dish_soap.png"},
                {"Paper Towels", "Absorbent paper towels", "Roll", 8, 4, 15, 5.99, "images/paper_towels.png"},
                {"Shampoo", "Moisturizing shampoo", "Bottle", 8, 4, 8, 6.50, "images/shampoo.png"},
                {"Conditioner", "Nourishing hair conditioner", "Bottle", 8, 4, 8, 6.50, "images/conditioner.png"},
                {"Toothpaste", "Fluoride toothpaste", "Tube", 8, 4, 10, 3.99, "images/toothpaste.png"},
                {"Hand Soap", "Antibacterial hand soap", "Bottle", 8, 4, 15, 2.99, "images/hand_soap.png"},
                {"Deodorant", "Long-lasting deodorant", "Stick", 8, 4, 10, 4.99, "images/deodorant.png"},
                {"Flour", "All-purpose flour", "Bag", 7, 3, 20, 3.50, "images/flour.png"},
                {"Sugar", "Organic cane sugar", "Bag", 7, 3, 20, 4.00, "images/sugar.png"},
                {"Salt", "Himalayan pink salt", "Pack", 7, 3, 15, 2.50, "images/salt.png"},
                {"Pepper", "Black ground pepper", "Pack", 7, 3, 12, 3.00, "images/pepper.png"},
                {"Cereal", "Whole grain breakfast cereal", "Box", 7, 3, 10, 4.99, "images/cereal.png"}
        };
        executeBatchInsert(query, products);
    }

    private void seedWarehouses() {
        String query = "INSERT INTO warehouses (warehouse_id, warehouse_name, location, is_active) VALUES (?, ?, ?, ?)";
        Object[][] warehouses = {
                {1, "Main Warehouse", "Downtown City", true},
                {2, "North Hub", "Uptown", true},
                {3, "South Hub", "South District", true},
                {4, "East Center", "East Side", true},
                {5, "West Depot", "West Hills", true}
        };
        executeBatchInsert(query, warehouses);
    }

    private void seedInventory() {
        String query = "INSERT INTO inventory (product_id, warehouse_id, quantity, expiration_date) VALUES (?, ?, ?, ?)";

        Random random = new Random();
        LocalDate today = LocalDate.now();

        Object[][] inventory = {
                {1, 1, 100}, {2, 1, 150}, {3, 2, 120}, {4, 2, 200}, {5, 3, 180},
                {6, 3, 90}, {7, 4, 75}, {8, 4, 160}, {9, 5, 140}, {10, 5, 130},
                {11, 1, 110}, {12, 1, 95}, {13, 2, 85}, {14, 2, 190}, {15, 3, 175},
                {16, 3, 105}, {17, 4, 125}, {18, 4, 210}, {19, 5, 135}, {20, 5, 145},
                {21, 1, 98}, {22, 1, 175}, {23, 2, 160}, {24, 2, 115}, {25, 3, 195},
                {26, 3, 88}, {27, 4, 150}, {28, 4, 170}, {29, 5, 80}, {30, 5, 155},
                {31, 1, 140}, {32, 1, 132}, {33, 2, 167}, {34, 2, 90}, {35, 3, 110},
                {36, 3, 200}, {37, 4, 145}, {38, 4, 125}, {39, 5, 115}, {40, 5, 180},
                {41, 1, 135}, {42, 1, 85}, {43, 2, 155}, {44, 2, 140}, {45, 3, 195},
                {46, 3, 175}, {47, 4, 125}, {48, 4, 160}
        };

        // Convert inventory array to include expiration date
        Object[][] updatedInventory = new Object[inventory.length][4];

        for (int i = 0; i < inventory.length; i++) {
            updatedInventory[i][0] = inventory[i][0]; // product_id
            updatedInventory[i][1] = inventory[i][1]; // warehouse_id
            updatedInventory[i][2] = inventory[i][2]; // quantity
            updatedInventory[i][3] = today.plusMonths(random.nextInt(10) + 3).toString(); // Expiration date (3-12 months ahead)
        }

        executeBatchInsert(query, updatedInventory);
    }

    private void seedCustomers() {
        String query = "INSERT INTO customers (customer_id, first_name, last_name, phone, email, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        Object[][] customers = {
                {1, "John", "Doe", "123-456-7890", "john.doe@email.com", true},
                {2, "Jane", "Smith", "234-567-8901", "jane.smith@email.com", true},
                {3, "Alice", "Johnson", "345-678-9012", "alice.johnson@email.com", true},
                {4, "Bob", "Williams", "456-789-0123", "bob.williams@email.com", true},
                {5, "Charlie", "Brown", "567-890-1234", "charlie.brown@email.com", true}
        };
        executeBatchInsert(query, customers);
    }

    private void executeBatchInsert(String query, Object[][] data) {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (Object[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    stmt.setObject(i + 1, row[i]);
                }
                stmt.addBatch();
            }
            stmt.executeBatch();
            System.out.println("Data seeded successfully for query: " + query);
        } catch (SQLException e) {
            System.err.println("Error executing batch insert: " + e.getMessage());
        }
    }

    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseSeeder seeder = new DatabaseSeeder();
        seeder.seedDatabase();
    }
}
