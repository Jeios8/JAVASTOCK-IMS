package main.java.com.javastock.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class DatabaseSeeder {

    public DatabaseSeeder() {
    }

    public void seedRoles() {
        String query = """
                    INSERT INTO roles (role_id, role_name, description)
                    VALUES (?, ?, ?)
                """;

        Object[][] roles = {
                {1, "Admin", "Administrator with full access"},
                {2, "Manager", "Manages operations and staff"},
                {3, "Staff", "Handles daily tasks"},
                {4, "Moderator", "Moderates content and user activities"},
                {5, "Viewer", "Read-only access"}
        };

        try (Connection connection = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] role : roles) {
                stmt.setInt(1, (int) role[0]);
                stmt.setString(2, (String) role[1]);
                stmt.setString(3, (String) role[2]);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Roles seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during roles database seeding: " + e.getMessage());
        }
    }

    public void seedUsers() {
        String query = """
        INSERT INTO users (username, password_hash, first_name, last_name, role_id)
        VALUES (?, ?, ?, ?, ?)
    """;

        Object[][] users = {
                {"admin", Hasher.hashPassword("admin"), "Admin", "User", 1},
                {"manager", Hasher.hashPassword("manager"), "Manager", "User", 2},
                {"staff", Hasher.hashPassword("staff"), "Staff", "User", 3},
                {"moderator", Hasher.hashPassword("moderator"), "Moderator", "User", 4},
                {"viewer", Hasher.hashPassword("viewer"), "Viewer", "User", 5}
        };

        try (Connection connection = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] user : users) {
                stmt.setString(1, (String) user[0]);
                stmt.setString(2, (String) user[1]);
                stmt.setString(3, (String) user[2]);
                stmt.setString(4, (String) user[3]);
                stmt.setInt(5, (int) user[4]);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Users seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during users database seeding: " + e.getMessage());
        }
    }


    public void seedCategories() {
        String query = """
                    INSERT INTO categories (category_id, category_name, is_active)
                    VALUES (?, ?, ?)
                """;

        Object[][] categories = {
                {1, "Electronics", true},
                {2, "Furniture", true},
                {3, "Clothing", true},
                {4, "Books", true},
                {5, "Toys", true}
        };

        try (Connection connection = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] category : categories) {
                stmt.setInt(1, (int) category[0]);
                stmt.setString(2, (String) category[1]);
                stmt.setBoolean(3, (boolean) category[2]);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Categories seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during categories database seeding: " + e.getMessage());
        }
    }

    public void seedSuppliers() {
        String query = """
                    INSERT INTO suppliers (supplier_id, supplier_name, contact_name, phone, email, address, is_active)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        Object[][] suppliers = {
                {1, "TechCorp", "Alice Johnson", "123-456-7890", "alice@techcorp.com", "123 Tech Street", true},
                {2, "FurniPro", "Bob Smith", "234-567-8901", "bob@furnipro.com", "456 Furni Ave", true},
                {3, "Clothify", "Catherine Lee", "345-678-9012", "catherine@clothify.com", "789 Cloth Blvd", true},
                {4, "BookNest", "David Wright", "456-789-0123", "david@booknest.com", "321 Book Lane", true},
                {5, "ToyWorld", "Eva Green", "567-890-1234", "eva@toyworld.com", "654 Toy Circle", true}
        };

        try (Connection connection = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] supplier : suppliers) {
                stmt.setInt(1, (int) supplier[0]);
                stmt.setString(2, (String) supplier[1]);
                stmt.setString(3, (String) supplier[2]);
                stmt.setString(4, (String) supplier[3]);
                stmt.setString(5, (String) supplier[4]);
                stmt.setString(6, (String) supplier[5]);
                stmt.setBoolean(7, (boolean) supplier[6]);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Suppliers seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during suppliers database seeding: " + e.getMessage());
        }
    }

    public void seedWarehouses() {
        String query = """
                    INSERT INTO warehouses (warehouse_id, warehouse_name, location, is_active)
                    VALUES (?, ?, ?, ?)
                """;

        Object[][] warehouses = {
                {1, "Main Warehouse", "Downtown City", true},
                {2, "North Hub", "Uptown", true},
                {3, "South Hub", "South District", true},
                {4, "East Center", "East Side", true},
                {5, "West Depot", "West Hills", true}
        };

        try (Connection connection = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] warehouse : warehouses) {
                stmt.setInt(1, (int) warehouse[0]);
                stmt.setString(2, (String) warehouse[1]);
                stmt.setString(3, (String) warehouse[2]);
                stmt.setBoolean(4, (boolean) warehouse[3]);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Warehouses seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during warehouses database seeding: " + e.getMessage());
        }
    }

    public void seedProducts() {
        String query = """
                    INSERT INTO products (product_id, product_name, description, unit_of_measure, category_id, supplier_id, reorder_level, unit_price, image_path, is_active)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Object[][] products = {
                {1, "Smartphone", "Latest model smartphone", "Piece", 1, 1, 5, 699.99, "images/smartphone.png", true},
                {2, "Office Chair", "Ergonomic office chair", "Piece", 2, 2, 10, 129.99, "images/office_chair.png", true},
                {3, "T-shirt", "Cotton t-shirt", "Piece", 3, 3, 20, 9.99, "images/tshirt.png", true},
                {4, "Science Book", "Educational science book", "Piece", 4, 4, 15, 19.99, "images/science_book.png", true},
                {5, "Building Blocks", "Creative building blocks for kids", "Piece", 5, 5, 10, 29.99, "images/building_blocks.png", true}
        };

        try (Connection connection = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] product : products) {
                stmt.setInt(1, (int) product[0]);
                stmt.setString(2, (String) product[1]);
                stmt.setString(3, (String) product[2]);
                stmt.setString(4, (String) product[3]);
                stmt.setInt(5, (int) product[4]);
                stmt.setInt(6, (int) product[5]);
                stmt.setInt(7, (int) product[6]);
                stmt.setDouble(8, (double) product[7]);
                stmt.setString(9, (String) product[8]);
                stmt.setBoolean(10, (boolean) product[9]);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Products seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during products database seeding: " + e.getMessage());
        }
    }

    public void seedInventory() {
        String query = """
                    INSERT INTO inventory (inventory_id, product_id, warehouse_id, quantity)
                    VALUES (?, ?, ?, ?)
                """;

        Object[][] inventoryData = {
                {1, 1, 1, 500},
                {2, 2, 2, 300},
                {3, 3, 3, 200},
                {4, 4, 4, 150},
                {5, 5, 5, 100}
        };

        try (Connection connection = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] record : inventoryData) {
                stmt.setInt(1, (int) record[0]);
                stmt.setInt(2, (int) record[1]);
                stmt.setInt(3, (int) record[2]);
                stmt.setInt(4, (int) record[3]);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Inventory seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during inventory database seeding: " + e.getMessage());
        }
    }

    public void seedCustomers() {
        String query = """
                    INSERT INTO customers (customer_id, first_name, last_name, is_active)
                    VALUES (?, ?, ?, ?)
                """;

        Object[][] customers = {
                {1, "John", "Doe", true},
                {2, "Jane", "Smith", true},
                {3, "Alice", "Brown", true},
                {4, "Bob", "Davis", true},
                {5, "Carol", "Johnson", true},
                {6, "Michael", "Wilson", true},
                {7, "Nancy", "Moore", true},
                {8, "Steve", "Taylor", true},
                {9, "Rachel", "Anderson", true},
                {10, "Chris", "Thomas", true}
        };

        try (Connection connection = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Object[] customer : customers) {
                stmt.setInt(1, (int) customer[0]);
                stmt.setString(2, (String) customer[1]);
                stmt.setString(3, (String) customer[2]);
                stmt.setBoolean(4, (boolean) customer[3]);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Customers seeded successfully!");

        } catch (SQLException e) {
            System.err.println("Error during customers database seeding: " + e.getMessage());
        }
    }
}