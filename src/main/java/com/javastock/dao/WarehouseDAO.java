package main.java.com.javastock.dao;

import main.java.com.javastock.model.Warehouse;
import main.java.com.javastock.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO {

    // Fetch all warehouses
    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouseList = new ArrayList<>();
        String query = "SELECT * FROM warehouses";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                warehouseList.add(mapResultSetToWarehouse(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch warehouses from database", e);
        }
        return warehouseList;
    }

    // Fetch a single warehouse by ID
    public Warehouse getWarehouseById(int warehouseId) {
        String query = "SELECT * FROM warehouses WHERE warehouse_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, warehouseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToWarehouse(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch warehouse record", e);
        }
        return null;
    }

    // Insert a new warehouse
    public boolean insertWarehouse(Warehouse warehouse) {
        String query = """
        INSERT INTO warehouses (warehouse_name, contact_name, phone, email, address, is_active)
        VALUES (?, ?, ?, ?, ?, TRUE)
        """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, warehouse.getWarehouseName());
            stmt.setString(2, warehouse.getContactName());
            stmt.setString(3, warehouse.getPhone());
            stmt.setString(4, warehouse.getEmail());
            stmt.setString(5, warehouse.getAddress());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database Error in insertWarehouse(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Update an existing warehouse
    public boolean updateWarehouse(Warehouse warehouse) {
        String query = """
        UPDATE warehouses SET 
            warehouse_name = ?, 
            contact_name = ?, 
            phone = ?, 
            email = ?, 
            address = ?, 
            is_active = ? 
        WHERE warehouse_id = ?
        """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, warehouse.getWarehouseName());
            stmt.setString(2, warehouse.getContactName());
            stmt.setString(3, warehouse.getPhone());
            stmt.setString(4, warehouse.getEmail());
            stmt.setString(5, warehouse.getAddress());
            stmt.setBoolean(6, warehouse.isActive());
            stmt.setInt(7, warehouse.getWarehouseId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database Error in updateWarehouse(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete a warehouse by ID
    public boolean deleteWarehouse(int warehouseId) {
        String query = "DELETE FROM warehouses WHERE warehouse_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, warehouseId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database Error in deleteWarehouse(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Helper method: Map ResultSet to Warehouse object
    private Warehouse mapResultSetToWarehouse(ResultSet rs) throws SQLException {
        return new Warehouse(
                rs.getInt("warehouse_id"),
                rs.getString("warehouse_name"),
                rs.getString("contact_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getBoolean("is_active")
        );
    }
}