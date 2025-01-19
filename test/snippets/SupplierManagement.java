import java.sql.*;
import java.util.Scanner;

public class SupplierManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/inventorydb"; // Replace with your database name
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "root"; // Replace with your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nSupplier Management System");
            System.out.println("1. Add Supplier");
            System.out.println("2. Edit Supplier");
            System.out.println("3. Delete Supplier");
            System.out.println("4. View All Suppliers");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addSupplier(scanner);
                    break;
                case 2:
                    editSupplier(scanner);
                    break;
                case 3:
                    deleteSupplier(scanner);
                    break;
                case 4:
                    viewAllSuppliers();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addSupplier(Scanner scanner) {
        System.out.print("Enter supplier name: ");
        String supplierName = scanner.nextLine();

        System.out.print("Enter contact name: ");
        String contactName = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        String query = "INSERT INTO suppliers (supplier_name, contact_name, phone, email, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, supplierName);
            pstmt.setString(2, contactName);
            pstmt.setString(3, phone);
            pstmt.setString(4, email);
            pstmt.setString(5, address);
            pstmt.executeUpdate();
            System.out.println("Supplier added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding supplier: " + e.getMessage());
        }
    }

    private static void editSupplier(Scanner scanner) {
        System.out.print("Enter supplier ID to edit: ");
        int supplierId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new supplier name: ");
        String supplierName = scanner.nextLine();

        System.out.print("Enter new contact name: ");
        String contactName = scanner.nextLine();

        System.out.print("Enter new phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter new email: ");
        String email = scanner.nextLine();

        System.out.print("Enter new address: ");
        String address = scanner.nextLine();

        System.out.print("Is the supplier active? (true/false): ");
        boolean isActive = scanner.nextBoolean();

        String query = "UPDATE suppliers SET supplier_name = ?, contact_name = ?, phone = ?, email = ?, address = ?, is_active = ? WHERE supplier_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, supplierName);
            pstmt.setString(2, contactName);
            pstmt.setString(3, phone);
            pstmt.setString(4, email);
            pstmt.setString(5, address);
            pstmt.setBoolean(6, isActive);
            pstmt.setInt(7, supplierId);
            pstmt.executeUpdate();
            System.out.println("Supplier updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating supplier: " + e.getMessage());
        }
    }

    private static void deleteSupplier(Scanner scanner) {
        System.out.print("Enter supplier ID to delete: ");
        int supplierId = scanner.nextInt();

        String query = "DELETE FROM suppliers WHERE supplier_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, supplierId);
            pstmt.executeUpdate();
            System.out.println("Supplier deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting supplier: " + e.getMessage());
        }
    }

    private static void viewAllSuppliers() {
        String query = "SELECT * FROM suppliers";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nAll Suppliers:");
            while (rs.next()) {
                int id = rs.getInt("supplier_id");
                String supplierName = rs.getString("supplier_name");
                String contactName = rs.getString("contact_name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String address = rs.getString("address");
                boolean isActive = rs.getBoolean("is_active");

                System.out.println("ID: " + id + ", Supplier Name: " + supplierName + ", Contact Name: " + contactName + ", Phone: " + phone + ", Email: " + email + ", Address: " + address + ", Active: " + isActive);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving suppliers: " + e.getMessage());
        }
    }
}