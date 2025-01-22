package main.java.com.javastock.viewmodel;

import main.java.com.javastock.utils.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductVM {
    private final int productId;

    public ProductVM(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return getStringValue("SELECT product_name FROM products WHERE product_id = ?", productId);
    }

    public double getBuyingPrice() {
        return getDoubleValue("SELECT unit_price FROM products WHERE product_id = ?", productId);
    }

    public String getSupplier() {
        return getStringValue("""
            SELECT s.supplier_name 
            FROM products p 
            JOIN suppliers s ON p.supplier_id = s.supplier_id 
            WHERE p.product_id = ?
        """, productId);
    }

    public int getQuantity() {
        return getIntValue("SELECT quantity FROM inventory WHERE product_id = ?", productId);
    }

    public int getStockOnHand() {
        return getIntValue("SELECT SUM(quantity) FROM inventory WHERE product_id = ?", productId);
    }

    public int getOnTheWayStock() {
        return getIntValue("""
            SELECT SUM(quantity) 
            FROM purchase_order_items poi
            JOIN purchase_orders po ON poi.purchase_order_id = po.purchase_order_id
            WHERE poi.product_id = ? AND po.status = 'Ordered'
        """, productId);
    }

    private String getStringValue(String query, int productId) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    private double getDoubleValue(String query, int productId) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private int getIntValue(String query, int productId) {
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getThreshold() {
        return getIntValue("SELECT reorder_level FROM products WHERE product_id = ?", productId);
    }

    public String getCategory() {
        return getStringValue("""
            SELECT c.category_name 
            FROM products p 
            JOIN categories c ON p.category_id = c.category_id 
            WHERE p.product_id = ?
        """, productId);
    }

    public Object[][] getStockLocations() {
        List<Object[]> stockData = new ArrayList<>();
        String query = """
        SELECT w.warehouse_name, i.quantity 
        FROM inventory i
        JOIN warehouses w ON i.warehouse_id = w.warehouse_id
        WHERE i.product_id = ?
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                stockData.add(new Object[]{rs.getString("warehouse_name"), rs.getInt("quantity")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockData.toArray(new Object[0][2]);
    }

    public boolean updateProduct(String name, double price, int quantity, int threshold) {
        String updateProductQuery = "UPDATE products SET product_name = ?, unit_price = ?, reorder_level = ? WHERE product_id = ?";
        String updateInventoryQuery = "UPDATE inventory SET quantity = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false); // ✅ Start transaction

            // ✅ Update the `products` table
            try (PreparedStatement stmt = conn.prepareStatement(updateProductQuery)) {
                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setInt(3, threshold);
                stmt.setInt(4, productId);
                stmt.executeUpdate();
            }

            // ✅ Update the `inventory` table
            try (PreparedStatement stmt = conn.prepareStatement(updateInventoryQuery)) {
                stmt.setInt(1, quantity);
                stmt.setInt(2, productId);
                stmt.executeUpdate();
            }

            conn.commit(); // ✅ Commit transaction if successful
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
