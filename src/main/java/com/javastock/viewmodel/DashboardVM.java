package main.java.com.javastock.viewmodel;

import main.java.com.javastock.utils.DatabaseConnector;
import java.sql.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

/**
 * ViewModel for the Dashboard, providing data insights for sales, purchases,
 * stock levels, and other key business metrics.
 */
public class DashboardVM {

    /**
     * Retrieves an overview of sales data including total sales, revenue, profit,
     * and canceled orders.
     *
     * @return A map containing sales statistics.
     */
    public Map<String, Integer> getSalesOverview() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String query = """
            SELECT COUNT(DISTINCT so.sales_order_id) AS total_sales,
                   SUM(soi.unit_price * soi.quantity) AS revenue,
                   SUM(soi.unit_price * soi.quantity) - SUM(soi.unit_price * soi.quantity * 0.8) AS profit,
                   COUNT(CASE WHEN so.status = 'Cancelled' THEN 1 END) AS canceled_orders
            FROM sales_order_items soi
            JOIN sales_orders so ON soi.sales_order_id = so.sales_order_id;
            """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                data.put("Total Sales", rs.getInt("total_sales"));
                data.put("Revenue", rs.getInt("revenue"));
                data.put("Profit", rs.getInt("profit"));
                data.put("Canceled Orders", rs.getInt("canceled_orders"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Retrieves an overview of purchase data including total purchases, cost, and expected returns.
     *
     * @return A map containing purchase statistics.
     */
    public Map<String, Integer> getPurchaseOverview() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String query = """
                SELECT 
                    COUNT(DISTINCT purchase_order_id) AS total_purchases,
                    SUM(unit_price * quantity) AS cost,
                    SUM(unit_price * quantity * 0.05) AS returns
                FROM purchase_order_items;
                """;
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                data.put("Total Purchases", rs.getInt("total_purchases"));
                data.put("Cost", rs.getInt("cost"));
                data.put("Returns", rs.getInt("returns"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Retrieves a list of the top 10 best-selling products based on sales quantity.
     *
     * @return A list of maps containing product details (name, sold quantity, remaining stock, price).
     */
    public List<Map<String, Object>> getTopSellingProducts() {
        List<Map<String, Object>> topSellingProducts = new ArrayList<>();

        String query = "SELECT p.product_name, SUM(soi.quantity) AS sold_quantity, " +
                "i.quantity AS remaining_quantity, p.unit_price " +
                "FROM sales_order_items soi " +
                "JOIN products p ON soi.product_id = p.product_id " +
                "LEFT JOIN inventory i ON i.product_id = p.product_id " +
                "GROUP BY p.product_name " +
                "ORDER BY sold_quantity DESC " +
                "LIMIT 10";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> product = new HashMap<>();
                product.put("product_name", rs.getString("product_name"));
                product.put("sold_quantity", rs.getInt("sold_quantity"));
                product.put("remaining_quantity", rs.getInt("remaining_quantity"));
                product.put("unit_price", rs.getDouble("unit_price"));
                topSellingProducts.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topSellingProducts;
    }

    /**
     * Retrieves a table model containing products with low stock levels.
     *
     * @return A DefaultTableModel containing product names, stock, and reorder levels.
     */
    public DefaultTableModel getLowStockAlerts() {
        String[] columns = {"Product Name", "Stock", "Reorder Level"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        String query = """
                SELECT p.product_name, i.quantity, p.reorder_level
                FROM inventory i
                JOIN products p ON i.product_id = p.product_id
                WHERE i.quantity < p.reorder_level
                ORDER BY i.quantity ASC;
                """;
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("product_name"), rs.getInt("quantity"), rs.getInt("reorder_level")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * Retrieves a list of products with low stock levels.
     *
     * @return A list of maps containing product details (name, remaining stock, price).
     */
    public List<Map<String, Object>> getLowStockProducts() {
        List<Map<String, Object>> lowStockProducts = new ArrayList<>();

        String query = "SELECT product_name, quantity AS remaining_quantity, unit_price " +
                "FROM inventory i " +
                "JOIN products p ON i.product_id = p.product_id " +
                "WHERE i.quantity <= p.reorder_level " +
                "ORDER BY i.quantity ASC " +
                "LIMIT 10";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> product = new HashMap<>();
                product.put("product_name", rs.getString("product_name"));
                product.put("remaining_quantity", rs.getInt("remaining_quantity"));
                product.put("unit_price", rs.getDouble("unit_price"));
                lowStockProducts.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lowStockProducts;
    }
}