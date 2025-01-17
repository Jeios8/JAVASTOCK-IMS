package snippets;

import java.sql.*;
import java.util.Scanner;

public class SupplierManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/InventoryDB";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nSupplier Management Menu:");
            System.out.println("1. Add Supplier");
            System.out.println("2. Edit Supplier");
            System.out.println("3. Delete Supplier");
            System.out.println("4. View All Suppliers");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
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
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void addSupplier(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Suppliers (supplier_name, contact_name, phone, email, address) VALUES (?, ?, ?, ?, ?)")) {

            System.out.print("Enter supplier name: ");
            String name = scanner.nextLine();
            System.out.print("Enter contact person: ");
            String contactPerson = scanner.nextLine();
            System.out.print("Enter phone number: ");
            String phone = scanner.nextLine();
            System.out.print("Enter email address: ");
            String email = scanner.nextLine();
            System.out.print("Enter address: ");
            String address = scanner.nextLine();

            stmt.setString(1, name);
            stmt.setString(2, contactPerson);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, address);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Supplier added successfully!");
            } else {
                System.out.println("Failed to add supplier.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void editSupplier(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("UPDATE Suppliers SET supplier_name = ?, contact_name = ?, phone = ?, email = ?, address = ? WHERE supplier_id = ?")) {

            System.out.print("Enter supplier ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new supplier name: ");
            String name = scanner.nextLine();
            System.out.print("Enter new contact person: ");
            String contactPerson = scanner.nextLine();
            System.out.print("Enter new phone number: ");
            String phone = scanner.nextLine();
            System.out.print("Enter new email address: ");
            String email = scanner.nextLine();
            System.out.print("Enter new address: ");
            String address = scanner.nextLine();

            stmt.setString(1, name);
            stmt.setString(2, contactPerson);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, address);
            stmt.setInt(6, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Supplier updated successfully!");
            } else {
                System.out.println("Supplier not found or failed to update.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteSupplier(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Suppliers WHERE supplier_id = ?")) {

            System.out.print("Enter supplier ID: ");
            int id = scanner.nextInt();

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Supplier deleted successfully!");
            } else {
                System.out.println("Supplier not found or failed to delete.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewAllSuppliers() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Suppliers")) {

            System.out.println("All Suppliers:");
            while (rs.next()) {
                int id = rs.getInt("supplier_id");
                String name = rs.getString("supplier_name");
                String contactPerson = rs.getString("contact_name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String address = rs.getString("address");
                System.out.println("ID: " + id + ", Name: " + name + ", Contact Person: " + contactPerson + ", Phone: " + phone + ", Email: " + email + "Address:" + address);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}