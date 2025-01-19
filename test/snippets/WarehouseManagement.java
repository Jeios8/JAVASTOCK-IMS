import java.sql.*;
import java.util.Scanner;

public class WarehouseManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/inventorydb"; // Replace with your database name
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "root"; // Replace with your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nWarehouse Management System");
            System.out.println("1. Add Warehouse");
            System.out.println("2. Edit Warehouse");
            System.out.println("3. Delete Warehouse");
            System.out.println("4. View All Warehouses");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
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
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addWarehouse(Scanner scanner) {
        System.out.print("Enter warehouse name: ");
        String warehouseName = scanner.nextLine();

        System.out.print("Enter location: ");
        String location = scanner.nextLine();

        String query = "INSERT INTO warehouses (warehouse_name, location) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, warehouseName);
            pstmt.setString(2, location);
            pstmt.executeUpdate();
            System.out.println("Warehouse added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding warehouse: " + e.getMessage());
        }
    }

    private static void editWarehouse(Scanner scanner) {
        System.out.print("Enter warehouse ID to edit: ");
        int warehouseId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new warehouse name: ");
        String warehouseName = scanner.nextLine();

        System.out.print("Enter new location: ");
        String location = scanner.nextLine();

        System.out.print("Is the warehouse active? (true/false): ");
        boolean isActive = scanner.nextBoolean();

        String query = "UPDATE warehouses SET warehouse_name = ?, location = ?, is_active = ? WHERE warehouse_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, warehouseName);
            pstmt.setString(2, location);
            pstmt.setBoolean(3, isActive);
            pstmt.setInt(4, warehouseId);
            pstmt.executeUpdate();
            System.out.println("Warehouse updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating warehouse: " + e.getMessage());
        }
    }

    private static void deleteWarehouse(Scanner scanner) {
        System.out.print("Enter warehouse ID to delete: ");
        int warehouseId = scanner.nextInt();

        String query = "DELETE FROM warehouses WHERE warehouse_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, warehouseId);
            pstmt.executeUpdate();
            System.out.println("Warehouse deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting warehouse: " + e.getMessage());
        }
    }

    private static void viewAllWarehouses() {
        String query = "SELECT * FROM warehouses";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nAll Warehouses:");
            while (rs.next()) {
                int id = rs.getInt("warehouse_id");
                String warehouseName = rs.getString("warehouse_name");
                String location = rs.getString("location");
                boolean isActive = rs.getBoolean("is_active");

                System.out.println("ID: " + id + ", Warehouse Name: " + warehouseName + ", Location: " + location + ", Active: " + isActive);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving warehouses: " + e.getMessage());
        }
    }
}