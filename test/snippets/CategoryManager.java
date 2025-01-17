package snippets;

import java.sql.*;
import java.util.Scanner;

public class CategoryManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/InventoryDB";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nCategory Management Menu:");
            System.out.println("1. Add Category");
            System.out.println("2. Edit Category");
            System.out.println("3. Delete Category");
            System.out.println("4. View All Categories");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addCategory(scanner);
                    break;
                case 2:
                    editCategory(scanner);
                    break;
                case 3:
                    deleteCategory(scanner);
                    break;
                case 4:
                    viewAllCategories();
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

    private static void addCategory(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Categories (category_name) VALUES (?)")) {

            System.out.print("Enter category name: ");
            String name = scanner.nextLine();
            stmt.setString(1, name);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Category added successfully!");
            } else {
                System.out.println("Failed to add category.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void editCategory(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("UPDATE Categories SET category_name = ? WHERE category_id = ?")) {

            System.out.print("Enter category ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new category name: ");
            String name = scanner.nextLine();

            stmt.setString(1, name);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Category updated successfully!");
            } else {
                System.out.println("Category not found or failed to update.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteCategory(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Categories WHERE category_id = ?")) {

            System.out.print("Enter category ID: ");
            int id = scanner.nextInt();

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Category deleted successfully!");
            } else {
                System.out.println("Category not found or failed to delete.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewAllCategories() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Categories")) {

            System.out.println("All Categories:");
            while (rs.next()) {
                int id = rs.getInt("category_id");
                String name = rs.getString("category_name");
                System.out.println("ID: " + id + ", Name: " + name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}