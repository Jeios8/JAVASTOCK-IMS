package main.java.com.javastock.viewmodel;

import main.java.com.javastock.utils.DatabaseConnector;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingWorker;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryVM {
    private DefaultTableModel tableModel;
    private Runnable onDataLoaded; // Callback to update UI

    public InventoryVM() {
        tableModel = new DefaultTableModel(
                new String[]{"Product ID", "Product", "Buying Price", "Quantity", "Threshold", "Availability"},
                0
        );
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setOnDataLoaded(Runnable onDataLoaded) {
        this.onDataLoaded = onDataLoaded;
    }

    public void loadInventoryAsync() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                loadInventory();
                return null;
            }

            @Override
            protected void done() {
                if (onDataLoaded != null) onDataLoaded.run();
            }
        }.execute();
    }

    public void loadInventory() {
        String query = """
    SELECT p.product_id, p.product_name, p.unit_price, i.quantity, p.reorder_level,
           CASE 
               WHEN i.quantity = 0 THEN 'Out of stock'
               WHEN i.quantity <= p.reorder_level THEN 'Low stock'
               ELSE 'In-stock'
           END AS availability
    FROM inventory i
    JOIN products p ON i.product_id = p.product_id;
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0);  // Clear previous data

            while (rs.next()) {
                // Ensure the array size matches exactly the number of columns in DefaultTableModel
                Object[] rowData = new Object[tableModel.getColumnCount()];

                rowData[0] = rs.getInt("product_id");
                rowData[1] = rs.getString("product_name");
                rowData[2] = "₱ " + rs.getDouble("unit_price");
                rowData[3] = rs.getInt("quantity");
                rowData[4] = rs.getInt("reorder_level");
                rowData[5] = rs.getString("availability");

                if (rowData.length == tableModel.getColumnCount()) {
                    tableModel.addRow(rowData);
                } else {
                    System.err.println("Error: Column count mismatch! Expected " + tableModel.getColumnCount() + " but got " + rowData.length);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateAndAddProduct(String name, String price, String qty, String threshold,
                                         String category, String supplier, String warehouse) {
        double unitPrice;
        int quantity, reorderLevel;

        try {
            unitPrice = Double.parseDouble(price);
            quantity = Integer.parseInt(qty);
            reorderLevel = Integer.parseInt(threshold);
        } catch (NumberFormatException e) {
            return false; // Validation failed
        }

        boolean success = addProduct(name, unitPrice, quantity, reorderLevel, category, supplier, warehouse);
        if (success) {
            loadInventoryAsync();
        }

        return success;
    }

    private boolean addProduct(String name, double unitPrice, int quantity, int reorderLevel, String category, String supplier, String warehouse) {
        Connection conn = null;
        PreparedStatement productStmt = null;
        PreparedStatement inventoryStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            // **Insert into products table**
            String productQuery = """
        INSERT INTO products (product_name, unit_price, reorder_level, category_id, supplier_id)
        VALUES (?, ?, ?, 
                (SELECT category_id FROM categories WHERE category_name = ? LIMIT 1),
                (SELECT supplier_id FROM suppliers WHERE supplier_name = ? LIMIT 1))
        """;

            productStmt = conn.prepareStatement(productQuery, Statement.RETURN_GENERATED_KEYS);
            productStmt.setString(1, name);
            productStmt.setDouble(2, unitPrice);
            productStmt.setInt(3, reorderLevel);
            productStmt.setString(4, category);
            productStmt.setString(5, supplier);

            int affectedRows = productStmt.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            // **Retrieve generated product_id**
            generatedKeys = productStmt.getGeneratedKeys();
            int productId = -1;
            if (generatedKeys.next()) {
                productId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return false;
            }

            // **Insert into inventory table**
            String inventoryQuery = """
        INSERT INTO inventory (product_id, warehouse_id, quantity, status)
        VALUES (?, (SELECT warehouse_id FROM warehouses WHERE warehouse_name = ? LIMIT 1), ?, 'Received')
        """;

            inventoryStmt = conn.prepareStatement(inventoryQuery);
            inventoryStmt.setInt(1, productId);
            inventoryStmt.setString(2, warehouse);
            inventoryStmt.setInt(3, quantity);

            int inventoryRows = inventoryStmt.executeUpdate();
            if (inventoryRows == 0) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (productStmt != null) productStmt.close();
                if (inventoryStmt != null) inventoryStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String[] getCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT category_name FROM categories WHERE is_active = true";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories.toArray(new String[0]);
    }

    public String[] getSuppliers() {
        List<String> suppliers = new ArrayList<>();
        String query = "SELECT supplier_name FROM suppliers WHERE is_active = true";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                suppliers.add(rs.getString("supplier_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers.toArray(new String[0]);
    }

    public String[] getWarehouses() {
        List<String> warehouses = new ArrayList<>();
        String query = "SELECT warehouse_name FROM warehouses WHERE is_active = true";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                warehouses.add(rs.getString("warehouse_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouses.toArray(new String[0]);
    }

    public void filterInventory(String availability) {
        String query = """
    SELECT p.product_id, p.product_name, p.unit_price, i.quantity, p.reorder_level,
           CASE 
               WHEN i.quantity = 0 THEN 'Out of stock'
               WHEN i.quantity <= p.reorder_level THEN 'Low stock'
               ELSE 'In-stock'
           END AS availability
    FROM inventory i
    JOIN products p ON i.product_id = p.product_id
    WHERE (CASE 
               WHEN i.quantity = 0 THEN 'Out of stock'
               WHEN i.quantity <= p.reorder_level THEN 'Low stock'
               ELSE 'In-stock'
           END) = ?;
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, availability);
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        "₱" + rs.getDouble("unit_price"),
                        rs.getInt("quantity") + " Units",
                        rs.getInt("reorder_level") + " Units",
                        rs.getString("availability")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}