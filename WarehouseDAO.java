package main.java.com.javastock.dao;

import main.java.com.javastock.model.Warehouse;
import main.java.com.javastock.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAO {

    // Fetch all Warehouse from the database
    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouses = new ArrayList<>();
        String query = "SELECT * FROM warehouses"; // Ensure your table name is correct

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Warehouse warehouse = new Warehouse(
                        rs.getInt("warehouse_id"),
                        rs.getString("warehouse_name"),
                        rs.getString("location"),
                        rs.getString("contact_name"),
                        rs.getString("phone"),
                        rs.getBoolean("is_active")
                );
                warehouses.add(warehouse);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch warehouse from database", e);
        }
        return warehouses;
    }

    // Fetch a single warehouse by ID
    public Warehouse getWarehouseById(int warehouseId) {
        String query = "SELECT * FROM warehouses WHERE warehouse_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, warehouseId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Warehouse(
                        rs.getInt("warehouse_id"),
                        rs.getString("warehouse_name"),
                        rs.getString("location"),
                        rs.getString("contact_name"),
                        rs.getString("phone"),
                        rs.getBoolean("is_active")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch warehouse from database", e);
        }
        return null;
    }

    // Insert a new warehouse into the database
    public boolean insertWarehouse(Warehouse warehouse) {
        String query = "INSERT INTO warehouses (warehouse_name, location, contact_name, phone, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, warehouse.getWarehouseName());
            stmt.setString(2, warehouse.getLocation());
            stmt.setString(3, warehouse.getContactName());
            stmt.setString(4, warehouse.getPhone());
            stmt.setBoolean(5, warehouse.isActive());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert warehouse into database", e);
        }
    }

    // Update an existing warehouse
    public boolean updateWarehouse(Warehouse warehouse) {
        String query = "UPDATE warehouses SET warehouse_name=?, location=?, contact_name=?, phone=?, is_active=? WHERE warehouse_id=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, warehouse.getWarehouseName());
            stmt.setString(2, warehouse.getLocation());
            stmt.setString(3, warehouse.getContactName());
            stmt.setString(4, warehouse.getPhone());
            stmt.setBoolean(5, warehouse.isActive());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update warehouse in database", e);
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
            throw new RuntimeException("Failed to delete warehouse from database", e);
        }
    }
}








//package main.java.com.javastock.dao;
//
//import main.java.com.javastock.model.Warehouse;
//import main.java.com.javastock.utils.DatabaseConnector;
//import main.java.com.javastock.viewmodel.WarehouseVM;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class WarehouseDAO {
//
//    // Fetch all warehouse from the database
//    public List<Warehouse> getAllWarehouses() {
//        List<Warehouse> warehouses = new ArrayList<>();
//        String query = "SELECT * FROM warehouses"; // Ensure your table name is correct
//
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                Warehouse warehouse = new Warehouse(
//                        rs.getInt("warehouse_id"),
//                        rs.getString("warehouse_name"),
//                        rs.getString("location"),
//                        rs.getString("contact_name"),
//                        rs.getString("phone"),
//                        rs.getBoolean("is_active")
//                );
//                
//                warehouses.add(warehouse);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to fetch warehouses from database", e);
//        }
//        return warehouses;
//    }
//
//    // Fetch a single warehouse by ID
//    public Warehouse getWarehouseById(int warehouseId) {
//        String query = "SELECT * FROM warehouses WHERE warehouse_id = ?";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setInt(1, warehouseId);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                return new Warehouse(
//                        rs.getInt("warehouse_id"),
//                        rs.getString("warehouse_name"),
//                        rs.getString("location"),
//                        rs.getString("contact_name"),
//                        rs.getString("phone"),
//                        rs.getBoolean("is_active")
//                );
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to fetch warehouse from database", e);
//        }
//        return null;
//    }
//
//    // Insert a new warehouse into the database
//    public boolean insertWarehouse(Warehouse warehouse) {
//        String query = "INSERT INTO warehouses (warehouse_name, location, contact_name, phone, is_active) VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//
//            stmt.setString(1, warehouse.getWarehouseName());
//            stmt.setString(2, warehouse.getLocation());
//            stmt.setString(3, warehouse.getContactName());
//            stmt.setString(4, warehouse.getPhone());
//            stmt.setBoolean(5, warehouse.isActive());
//
//            int affectedRows = stmt.executeUpdate();
//            return affectedRows > 0;
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to insert warehouse into database", e);
//        }
//    }
//
//    // Update an existing warehouse
//    public boolean updateWarehouse(Warehouse warehouse) {
//        String query = "UPDATE warehouses SET warehouse_name=?, location=?, contact_name=?, phone=?, is_active=? WHERE warehouse_id=?";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setString(1, warehouse.getWarehouseName());
//            stmt.setString(2, warehouse.getLocation());
//            stmt.setString(3, warehouse.getContactName());
//            stmt.setString(4, warehouse.getPhone());
//            stmt.setBoolean(5, warehouse.isActive());
//            stmt.setInt(6, warehouse.getWarehouseId());
//
//            int affectedRows = stmt.executeUpdate();
//            return affectedRows > 0;
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to update warehouse in database", e);
//        }
//    }
//
//    // Delete a warehouse by ID
//    public boolean deleteWarehouse(int warehouseId) {
//        String query = "DELETE FROM warehouses WHERE warehouse_id = ?";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setInt(1, warehouseId);
//            int affectedRows = stmt.executeUpdate();
//            return affectedRows > 0;
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to delete warehouse from database", e);
//        }
//    }
//}
