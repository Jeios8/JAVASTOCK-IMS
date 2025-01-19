package main.java.com.javastock.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventorySeeder {

    public InventorySeeder() {
    }

    public void seedInventory() {
        String query = """
            INSERT INTO inventory (inventory_id, product_id, warehouse_id, quantity)
            VALUES (?, ?, ?, ?)
        """;

        // Seed data
        Object[][] inventoryData = {
                {1, 1, 1, 50},
                {2, 2, 1, 75},
                {3, 3, 2, 20},
                {4, 4, 2, 100},
                {5, 5, 3, 200},
                {6, 6, 3, 150},
                {7, 7, 4, 30},
                {8, 8, 4, 10},
                {9, 9, 5, 60},
                {10, 10, 5, 90}
        };

        try (Connection connection = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] record : inventoryData) {
                int inventoryId = (int) record[0];
                int productId = (int) record[1];
                int warehouseId = (int) record[2];
                int quantity = (int) record[3];

                // Bind parameters
                stmt.setInt(1, inventoryId);
                stmt.setInt(2, productId);
                stmt.setInt(3, warehouseId);
                stmt.setInt(4, quantity);

                // Add to batch
                stmt.addBatch();
            }

            // Execute batch
            stmt.executeBatch();
            System.out.println("Inventory seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during inventory database seeding: " + e.getMessage());
        }
    }

    public void seedProducts() {
        String query = """
            INSERT INTO products (product_id, product_name, description, unit_of_measure, category_id, supplier_id, reorder_level, unit_price, image_path, is_active)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        // Seed data
        Object[][] productData = {
                {1, "Laptop", "High performance laptop", "Piece", 2, 1, 10, 1200.50, "images/laptop.png", true},
                {2, "Smartphone", "Latest model smartphone", "Piece", 2, 2, 15, 800.00, "images/smartphone.png", true},
                {3, "Desk Chair", "Ergonomic desk chair", "Piece", 3, 3, 5, 150.75, "images/desk_chair.png", true},
                {4, "Monitor", "4K UHD monitor", "Piece", 2, 1, 8, 300.40, "images/monitor.png", true},
                {5, "Keyboard", "Mechanical keyboard", "Piece", 2, 4, 20, 70.99, "images/keyboard.png", true},
                {6, "Mouse", "Wireless mouse", "Piece", 2, 4, 25, 35.50, "images/mouse.png", true},
                {7, "Printer", "All-in-one printer", "Piece", 2, 5, 3, 250.00, "images/printer.png", true},
                {8, "Notebook", "College ruled notebook", "Piece", 1, 6, 50, 3.25, "images/notebook.png", true},
                {9, "Pen", "Ballpoint pen", "Piece", 1, 6, 100, 1.10, "images/pen.png", true},
                {10, "Desk", "Adjustable standing desk", "Piece", 3, 3, 2, 500.00, "images/desk.png", true}
        };

        try (Connection connection = DatabaseConnector.getConnection("inventorydb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] record : productData) {
                int productId = (int) record[0];
                String productName = (String) record[1];
                String description = (String) record[2];
                String unitOfMeasure = (String) record[3];
                int categoryId = (int) record[4];
                int supplierId = (int) record[5];
                int reorderLevel = (int) record[6];
                double unitPrice = (double) record[7];
                String imagePath = (String) record[8];
                boolean isActive = (boolean) record[9];

                // Bind parameters
                stmt.setInt(1, productId);
                stmt.setString(2, productName);
                stmt.setString(3, description);
                stmt.setString(4, unitOfMeasure);
                stmt.setInt(5, categoryId);
                stmt.setInt(6, supplierId);
                stmt.setInt(7, reorderLevel);
                stmt.setDouble(8, unitPrice);
                stmt.setString(9, imagePath);
                stmt.setBoolean(10, isActive);

                // Add to batch
                stmt.addBatch();
            }

            // Execute batch
            stmt.executeBatch();
            System.out.println("Products seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during products database seeding: " + e.getMessage());
        }
    }

    public void seedWarehouses() {
        String query = """
            INSERT INTO warehouses (warehouse_id, warehouse_name, location, is_active)
            VALUES (?, ?, ?, ?)
        """;

        // Seed data
        Object[][] warehouseData = {
                {1, "Main Warehouse", "123 Main St, Cityville", true},
                {2, "Secondary Warehouse", "456 Side Rd, Townsburg", true},
                {3, "Regional Warehouse", "789 Region Ave, Metrocity", true},
                {4, "Overflow Warehouse", "321 Overflow Blvd, Suburbia", true},
                {5, "Central Storage", "654 Central Park, Urbantown", true}
        };

        try (Connection connection = DatabaseConnector.getConnection("inventorydb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] record : warehouseData) {
                int warehouseId = (int) record[0];
                String warehouseName = (String) record[1];
                String location = (String) record[2];
                boolean isActive = (boolean) record[3];

                // Bind parameters
                stmt.setInt(1, warehouseId);
                stmt.setString(2, warehouseName);
                stmt.setString(3, location);
                stmt.setBoolean(4, isActive);

                // Add to batch
                stmt.addBatch();
            }

            // Execute batch
            stmt.executeBatch();
            System.out.println("Warehouses seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during warehouses database seeding: " + e.getMessage());
        }
    }
}
