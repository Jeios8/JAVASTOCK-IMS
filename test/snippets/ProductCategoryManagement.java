import java.sql.*;
import java.util.Scanner;

public class ProductCategoryManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/inventory"; // replace with your database name
    private static final String USER = "root"; // replace with your MySQL username
    private static final String PASSWORD = "root"; // replace with your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nProduct Category Management System");
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
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        String query = "INSERT INTO product_categories (category_name, description) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, categoryName);
            pstmt.setString(2, description);
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
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();

        String query = "UPDATE product_categories SET category_name = ?, description = ? WHERE category_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, categoryName);
            pstmt.setString(2, description);
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

        String query = "DELETE FROM product_categories WHERE category_id = ?";
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
        String query = "SELECT * FROM product_categories";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nAll Categories:");
            while (rs.next()) {
                int id = rs.getInt("category_id");
                String categoryName = rs.getString("category_name");
                String description = rs.getString("description");
                Timestamp createdAt = rs.getTimestamp("created_at");

                System.out.println("ID: " + id + ", Name: " + categoryName +
                        ", Description: " + description +
                        ", Created At: " + createdAt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving categories: " + e.getMessage());
        }
    }
}