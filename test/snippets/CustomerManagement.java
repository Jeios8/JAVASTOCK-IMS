import java.sql.*;
import java.util.Scanner;

public class CustomerManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/inventorydb"; // Replace with your database name
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "root"; // Replace with your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nCustomer Management System");
            System.out.println("1. Add Customer");
            System.out.println("2. Edit Customer");
            System.out.println("3. Delete Customer");
            System.out.println("4. View All Customers");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addCustomer(scanner);
                    break;
                case 2:
                    editCustomer(scanner);
                    break;
                case 3:
                    deleteCustomer(scanner);
                    break;
                case 4:
                    viewAllCustomers();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addCustomer(Scanner scanner) {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        String query = "INSERT INTO customers (first_name, last_name) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.executeUpdate();
            System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    private static void editCustomer(Scanner scanner) {
        System.out.print("Enter customer ID to edit: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter new last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Is the customer active? (true/false): ");
        boolean isActive = scanner.nextBoolean();

        String query = "UPDATE customers SET first_name = ?, last_name = ?, is_active = ? WHERE customer_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setBoolean(3, isActive);
            pstmt.setInt(4, customerId);
            pstmt.executeUpdate();
            System.out.println("Customer updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

    private static void deleteCustomer(Scanner scanner) {
        System.out.print("Enter customer ID to delete: ");
        int customerId = scanner.nextInt();

        String query = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();
            System.out.println("Customer deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }

    private static void viewAllCustomers() {
        String query = "SELECT * FROM customers";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nAll Customers:");
            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                boolean isActive = rs.getBoolean("is_active");

                System.out.println("ID: " + id + ", Name: " + firstName + " " + lastName + ", Active: " + isActive);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving customers: " + e.getMessage());
        }
    }
}