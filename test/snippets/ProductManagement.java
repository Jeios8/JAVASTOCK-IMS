import java.sql.*;
import java.util.Scanner;

public class ProductManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/inventorydb"; // Replace with your database name
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "root"; // Replace with your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nProduct Management System");
            System.out.println("1. Add Product");
            System.out.println("2. Edit Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View All Products");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    editProduct(scanner);
                    break;
                case 3:
                    deleteProduct(scanner);
                    break;
                case 4:
                    viewAllProducts();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addProduct(Scanner scanner) {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter unit of measure: ");
        String unitOfMeasure = scanner.nextLine();

        System.out.print("Enter category ID (or 0 for none): ");
        int categoryId = scanner.nextInt();
        if (categoryId == 0) categoryId = 0; // Handle no category

        System.out.print("Enter supplier ID (or 0 for none): ");
        int supplierId = scanner.nextInt();
        if (supplierId == 0) supplierId = 0; // Handle no supplier

        System.out.print("Enter reorder level: ");
        int reorderLevel = scanner.nextInt();

        System.out.print("Enter unit price: ");
        double unitPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter image path: ");
        String imagePath = scanner.nextLine();

        String query = "INSERT INTO products (product_name, description, unit_of_measure, category_id, supplier_id, reorder_level, unit_price, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, productName);
            pstmt.setString(2, description);
            pstmt.setString(3, unitOfMeasure);
            pstmt.setObject(4, categoryId != 0 ? categoryId : null); // Set to null if no category
            pstmt.setObject(5, supplierId != 0 ? supplierId : null); // Set to null if no supplier
            pstmt.setInt(6, reorderLevel);
            pstmt.setDouble(7, unitPrice);
            pstmt.setString(8, imagePath);
            pstmt.executeUpdate();
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private static void editProduct(Scanner scanner) {
        System.out.print("Enter product ID to edit: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new product name: ");
        String productName = scanner.nextLine();

        System.out.print("Enter new description: ");
        String description = scanner.nextLine();

        System.out.print("Enter new unit of measure: ");
        String unitOfMeasure = scanner.nextLine();

        System.out.print("Enter new category ID (or 0 for none): ");
        int categoryId = scanner.nextInt();
        if (categoryId == 0) categoryId = 0; // Handle no category

        System.out.print("Enter new supplier ID (or 0 for none): ");
        int supplierId = scanner.nextInt();
        if (supplierId == 0) supplierId = 0; // Handle no supplier

        System.out.print("Enter new reorder level: ");
        int reorderLevel = scanner.nextInt();

        System.out.print("Enter new unit price: ");
        double unitPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new image path: ");
        String imagePath = scanner.nextLine();

        System.out.print("Is the product active? (true/false): ");
        boolean isActive = scanner.nextBoolean();

        String query = "UPDATE products SET product_name = ?, description = ?, unit_of_measure = ?, category_id = ?, supplier_id = ?, reorder_level = ?, unit_price = ?, image_path = ?, is_active = ? WHERE product_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, productName);
            pstmt.setString(2, description);
            pstmt.setString(3, unitOfMeasure);
            pstmt.setObject(4, categoryId != 0 ? categoryId : null); // Set to null if no category
            pstmt.setObject(5, supplierId != 0 ? supplierId : null); // Set to null if no supplier
            pstmt.setInt(6, reorderLevel);
            pstmt.setDouble(7, unitPrice);
            pstmt.setString(8, imagePath);
            pstmt.setBoolean(9, isActive);
            pstmt.setInt(10, productId);
            pstmt.executeUpdate();
            System.out.println("Product updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    private static void deleteProduct(Scanner scanner) {
        System.out.print("Enter product ID to delete: ");
        int productId = scanner.nextInt();

        String query = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            System.out.println("Product deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    private static void viewAllProducts() {
        String query = "SELECT * FROM products";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nAll Products:");
            while (rs.next()) {
                int id = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                String description = rs.getString("description");
                String unitOfMeasure = rs.getString("unit_of_measure");
                Integer categoryId = rs.getObject("category_id", Integer.class);
                Integer supplierId = rs.getObject("supplier_id", Integer.class);
                int reorderLevel = rs.getInt("reorder_level");
                double unitPrice = rs.getDouble("unit_price");
                String imagePath = rs.getString("image_path");
                boolean isActive = rs.getBoolean("is_active");

                System.out.println("ID: " + id + ", Name: " + productName + ", Description: " + description +
                        ", Unit of Measure: " + unitOfMeasure + ", Category ID: " + categoryId +
                        ", Supplier ID: " + supplierId + ", Reorder Level: " + reorderLevel +
                        ", Unit Price: " + unitPrice + ", Image Path: " + imagePath +
                        ", Active: " + isActive);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving products: " + e.getMessage());
        }
    }
}