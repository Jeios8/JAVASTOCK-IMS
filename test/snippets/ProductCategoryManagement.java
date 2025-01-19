import java.sql.*;
import java.util.Scanner;

public class ProductCategoryManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/inventorydb"; // Replace with your database name
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "root"; // Replace with your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nCategory Management System");
            System.out.println("1. Add Category");
            System.out.println("2. Edit Category");
            System.out.println("3. Delete Category");
            System.out.println("4. View All Categories");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
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
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addCategory(Scanner scanner) {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();

        String query = "INSERT INTO categories (category_name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, categoryName);
            pstmt.executeUpdate();
            System.out.println("Category added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    private static void editCategory(Scanner scanner) {
        System.out.print("Enter category ID to edit: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new category name: ");
        String categoryName = scanner.nextLine();

        System.out.print("Is the category active? (true/false): ");
        boolean isActive = scanner.nextBoolean();

        String query = "UPDATE categories SET category_name = ?, is_active = ? WHERE category_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, categoryName);
            pstmt.setBoolean(2, isActive);
            pstmt.setInt(3, categoryId);
            pstmt.executeUpdate();
            System.out.println("Category updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating category: " + e.getMessage());
        }
    }

    private static void deleteCategory(Scanner scanner) {
        System.out.print("Enter category ID to delete: ");
        int categoryId = scanner.nextInt();

        String query = "DELETE FROM categories WHERE category_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, categoryId);
            pstmt.executeUpdate();
            System.out.println("Category deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting category: " + e.getMessage());
        }
    }

    private static void viewAllCategories() {
        String query = "SELECT * FROM categories";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nAll Categories:");
            while (rs.next()) {
                int id = rs.getInt("category_id");
                String categoryName = rs.getString("category_name");
                boolean isActive = rs.getBoolean("is_active");

                System.out.println("ID: " + id + ", Name: " + categoryName + ", Active: " + isActive);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving categories: " + e.getMessage());
        }
    }
}
