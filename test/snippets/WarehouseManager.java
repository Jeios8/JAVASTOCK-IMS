package snippets;

import java.sql.*;
import java.util.Scanner;

public class WarehouseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/InventoryDB";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nWarehouse Management Menu:");
            System.out.println("1. Add Warehouse");
            System.out.println("2. Edit Warehouse");
            System.out.println("3. Delete Warehouse");
            System.out.println("4. View All Warehouses");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addWarehouse(scanner);
                    break;
                case 2:
                    editWarehouse(scanner);
                    break;
                case 3:
                    deleteWarehouse(scanner);
                    break;
                case 4:
                    viewAllWarehouses();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void addWarehouse(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Warehouses (warehouse_name, location, capacity) VALUES (?, ?, ?)")) {

            System.out.print("Enter warehouse name: ");
            String name = scanner.nextLine();
            System.out.print("Enter warehouse location: ");
            String location = scanner.nextLine();
            System.out.print("Enter warehouse capacity: ");
            int capacity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setInt(3, capacity);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Warehouse added successfully!");
            } else {
                System.out.println("Failed to add warehouse.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void editWarehouse(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("UPDATE Warehouses SET warehouse_name = ?, location = ?, capacity = ? WHERE warehouse_id = ?")) {

            System.out.print("Enter warehouse ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new warehouse name: ");
            String name = scanner.nextLine();
            System.out.print("Enter new warehouse location: ");
            String location = scanner.nextLine();
            System.out.print("Enter new warehouse capacity: ");
            int capacity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setInt(3, capacity);
            stmt.setInt(4, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Warehouse updated successfully!");
            } else {
                System.out.println("Warehouse not found or failed to update.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteWarehouse(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Warehouses WHERE warehouse_id = ?")) {

            System.out.print("Enter warehouse ID: ");
            int id = scanner.nextInt();

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Warehouse deleted successfully!");
            } else {
                System.out.println("Warehouse not found or failed to delete.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewAllWarehouses() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Warehouses")) {

            System.out.println("All Warehouses:");
            while (rs.next()) {
                int id = rs.getInt("warehouse_id");
                String name = rs.getString("warehouse_name");
                String location = rs.getString("location");
                int capacity = rs.getInt("capacity");
                System.out.println("ID: " + id + ", Name: " + name + ", Location: " + location + ", Capacity: " + capacity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class Login {

        // Database credentials (Replace with your actual credentials)
        private static final String url = "jdbc:mysql://localhost:3306/InventoryDB";
        private static final String user = "root";
        private static final String password = "root";

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String passwordInput = scanner.nextLine();

            try {
                Connection connection = DriverManager.getConnection(url, user, password);
                String query = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, passwordInput);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("snippets.WarehouseManager.Login Successful!");
                    // Proceed to other operations here
                } else {
                    System.out.println("Invalid username or password.");
                }

                resultSet.close();
                preparedStatement.close();
                connection.close();

            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }

            scanner.close();
        }
    }
}