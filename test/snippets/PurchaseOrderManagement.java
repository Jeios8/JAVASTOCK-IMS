import java.sql.*;
import java.util.Scanner;

public class PurchaseOrderManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/inventorydb"; // Replace with your database name
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "root";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nPurchase Order Management System");
            System.out.println("1. Add Purchase Order");
            System.out.println("2. Edit Purchase Order");
            System.out.println("3. Delete Purchase Order");
            System.out.println("4. View All Purchase Orders");
            System.out.println("5. Add Item to Purchase Order");
            System.out.println("6. View Items in Purchase Order");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addPurchaseOrder(scanner);
                    break;
                case 2:
                    editPurchaseOrder(scanner);
                    break;
                case 3:
                    deletePurchaseOrder(scanner);
                    break;
                case 4:
                    viewAllPurchaseOrders();
                    break;
                case 5:
                    addItemToPurchaseOrder(scanner);
                    break;
                case 6:
                    viewItemsInPurchaseOrder(scanner);
                    break;
                case 7:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addPurchaseOrder(Scanner scanner) {
        System.out.print("Enter supplier ID (or 0 for none): ");
        int supplierId = scanner.nextInt();
        if (supplierId == 0) supplierId = 0; // Handle no supplier

        System.out.print("Enter order date (YYYY-MM-DD): ");
        String orderDate = scanner.next();

        System.out.print("Enter expected arrival date (YYYY-MM-DD): ");
        String expectedArrivalDate = scanner.next();

        String query = "INSERT INTO purchase_orders (supplier_id, order_date, expected_arrival_date) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setObject(1, supplierId != 0 ? supplierId : null); // Set to null if no supplier
            pstmt.setDate(2, Date.valueOf(orderDate));
            pstmt.setDate(3, Date.valueOf(expectedArrivalDate));
            pstmt.executeUpdate();
            System.out.println("Purchase Order added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding Purchase Order: " + e.getMessage());
        }
    }

    private static void editPurchaseOrder(Scanner scanner) {
        System.out.print("Enter Purchase Order ID to edit: ");
        int purchaseOrderId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new supplier ID (or 0 for none): ");
        int supplierId = scanner.nextInt();
        if (supplierId == 0) supplierId = 0; // Handle no supplier

        System.out.print("Enter new order date (YYYY-MM-DD): ");
        String orderDate = scanner.next();

        System.out.print("Enter new expected arrival date (YYYY-MM-DD): ");
        String expectedArrivalDate = scanner.next();

        System.out.print("Enter new status (Pending, Ordered, Received, Cancelled): ");
        String status = scanner.next();

        String query = "UPDATE purchase_orders SET supplier_id = ?, order_date = ?, expected_arrival_date = ?, status = ? WHERE purchase_order_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setObject(1, supplierId != 0 ? supplierId : null); // Set to null if no supplier
            pstmt.setDate(2, Date.valueOf(orderDate));
            pstmt.setDate(3, Date.valueOf(expectedArrivalDate));
            pstmt.setString(4, status);
            pstmt.setInt(5, purchaseOrderId);
            pstmt.executeUpdate();
            System.out.println("Purchase Order updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating Purchase Order: " + e.getMessage());
        }
    }

    private static void deletePurchaseOrder(Scanner scanner) {
        System.out.print("Enter Purchase Order ID to delete: ");
        int purchaseOrderId = scanner.nextInt();

        String query = "DELETE FROM purchase_orders WHERE purchase_order_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, purchaseOrderId);
            pstmt.executeUpdate();
            System.out.println("Purchase Order deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting Purchase Order: " + e.getMessage());
        }
    }

    private static void viewAllPurchaseOrders() {
        String query = "SELECT * FROM purchase_orders";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nAll Purchase Orders:");
            while (rs.next()) {
                int id = rs.getInt("purchase_order_id");
                int supplierId = rs.getObject("supplier_id", Integer.class);
                Date orderDate = rs.getDate("order_date");
                Date expectedArrivalDate = rs.getDate("expected_arrival_date");
                String status = rs.getString("status");

                System.out.println("ID: " + id + ", Supplier ID: " + supplierId + ", Order Date: " + orderDate +
                        ", Expected Arrival Date: " + expectedArrivalDate + ", Status: " + status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving Purchase Orders: " + e.getMessage());
        }
    }

    private static void addItemToPurchaseOrder(Scanner scanner) {
        System.out.print("Enter Purchase Order ID: ");
        int purchaseOrderId = scanner.nextInt();

        System.out.print("Enter Product ID: ");
        int productId = scanner.nextInt();

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter Unit Price: ");
        double unitPrice = scanner.nextDouble();

        String query = "INSERT INTO purchase_order_items (purchase_order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, purchaseOrderId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, unitPrice);
            pstmt.executeUpdate();
            System.out.println("Item added to Purchase Order successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding item to Purchase Order: " + e.getMessage());
        }
    }

    private static void viewItemsInPurchaseOrder(Scanner scanner) {
        System.out.print("Enter Purchase Order ID to view items: ");
        int purchaseOrderId = scanner.nextInt();

        String query = "SELECT * FROM purchase_order_items WHERE purchase_order_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, purchaseOrderId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\nItems in Purchase Order ID " + purchaseOrderId + ":");
            while (rs.next()) {
                int itemId = rs.getInt("purchase_order_item_id");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                double unitPrice = rs.getDouble("unit_price");

                System.out.println("Item ID: " + itemId + ", Product ID: " + productId + ", Quantity: " + quantity +
                        ", Unit Price: " + unitPrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving items in Purchase Order: " + e.getMessage());
        }
    }
}