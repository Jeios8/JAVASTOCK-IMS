package main.java.com.javastock.dao;

import main.java.com.javastock.model.Supplier;
import main.java.com.javastock.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    // Fetch all suppliers
    public List<Supplier> getAllSuppliers() {
        List<Supplier> supplierList = new ArrayList<>();
        String query = "SELECT * FROM suppliers";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                supplierList.add(mapResultSetToSupplier(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch suppliers from database", e);
        }
        return supplierList;
    }

    // Fetch a single supplier by ID
    public Supplier getSupplierById(int supplierId) {
        String query = "SELECT * FROM suppliers WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, supplierId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSupplier(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch supplier record", e);
        }
        return null;
    }

    // Insert a new supplier
    public boolean insertSupplier(Supplier supplier) {
        String query = """
        INSERT INTO suppliers (supplier_name, contact_name, phone, email, address, is_active)
        VALUES (?, ?, ?, ?, ?, TRUE)
        """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, supplier.getSupplierName());
            stmt.setString(2, supplier.getContactName());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            stmt.setString(5, supplier.getAddress());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database Error in insertSupplier(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Update an existing supplier
    public boolean updateSupplier(Supplier supplier) {
        String query = """
        UPDATE suppliers SET 
            supplier_name = ?, 
            contact_name = ?, 
            phone = ?, 
            email = ?, 
            address = ?, 
            is_active = ? 
        WHERE supplier_id = ?
        """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, supplier.getSupplierName());
            stmt.setString(2, supplier.getContactName());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            stmt.setString(5, supplier.getAddress());
            stmt.setBoolean(6, supplier.isActive());
            stmt.setInt(7, supplier.getSupplierId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database Error in updateSupplier(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete a supplier by ID
    public boolean deleteSupplier(int supplierId) {
        String query = "DELETE FROM suppliers WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, supplierId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database Error in deleteSupplier(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Helper method: Map ResultSet to Supplier object
    private Supplier mapResultSetToSupplier(ResultSet rs) throws SQLException {
        return new Supplier(
                rs.getInt("supplier_id"),
                rs.getString("supplier_name"),
                rs.getString("contact_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getBoolean("is_active")
        );
    }
}