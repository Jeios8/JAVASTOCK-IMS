package main.java.com.javastock.dao;

import main.java.com.javastock.model.Inventory;
import main.java.com.javastock.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    // Fetch all inventory records
    public List<Inventory> getAllInventory() {
        List<Inventory> inventoryList = new ArrayList<>();
        String query = "SELECT * FROM inventory";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                inventoryList.add(mapResultSetToInventory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch inventory from database", e);
        }
        return inventoryList;
    }

    // Fetch a single inventory record by ID
    public Inventory getInventoryById(int inventoryId) {
        String query = "SELECT * FROM inventory WHERE inventory_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, inventoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInventory(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch inventory record", e);
        }
        return null;
    }

    // Fetch inventory by product and warehouse
    public Inventory getInventoryByProductAndWarehouse(int productId, int warehouseId) {
        String query = """
            SELECT * FROM inventory 
            WHERE product_id = ? AND warehouse_id = ?
            LIMIT 1
        """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            stmt.setInt(2, warehouseId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInventory(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch inventory record", e);
        }
        return null;
    }

    // Insert new inventory record with a database connection
    public boolean insertInventory(Connection conn, Inventory inventory) {
        String query = """
        INSERT INTO inventory (product_id, warehouse_id, quantity, status, expiration_date)
        VALUES (?, ?, ?, 'Received', ?)
    """;

        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inventory.getProductId());
            stmt.setInt(2, inventory.getWarehouseId());
            stmt.setInt(3, inventory.getQuantity());

            if (inventory.getExpirationDate() == null) {
                stmt.setNull(4, java.sql.Types.DATE);
            } else {
                stmt.setDate(4, inventory.getExpirationDate());
            }

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database Error in insertInventory(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Update inventory quantity with a database connection
    public boolean updateInventoryQuantity(Connection conn, int inventoryId, int newQuantity) {
        String query = "UPDATE inventory SET quantity = ? WHERE inventory_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, inventoryId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database Error in updateInventoryQuantity(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Helper method: Map ResultSet to Inventory object
    private Inventory mapResultSetToInventory(ResultSet rs) throws SQLException {
        return new Inventory(
                rs.getInt("inventory_id"),
                rs.getInt("product_id"),
                rs.getInt("warehouse_id"),
                rs.getInt("quantity")
        );
    }

    public int getWarehouseIdByName(String warehouseName) {
        String query = "SELECT warehouse_id FROM warehouses WHERE warehouse_name = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, warehouseName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("warehouse_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if warehouse not found
    }
}