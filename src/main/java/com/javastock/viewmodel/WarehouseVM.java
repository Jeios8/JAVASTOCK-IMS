package main.java.com.javastock.viewmodel;

import main.java.com.javastock.dao.WarehouseDAO;
import main.java.com.javastock.model.Warehouse;
import main.java.com.javastock.utils.DatabaseConnector;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingWorker;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseVM {
    private DefaultTableModel tableModel;
    private Runnable onDataLoaded; // Callback to update UI

    public WarehouseVM() {
        tableModel = new DefaultTableModel(
                new String[]{"Warehouse ID", "Warehouse Name", "Contact Name", "Phone", "Email", "Address", "Status"},
                0
        );
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setOnDataLoaded(Runnable onDataLoaded) {
        this.onDataLoaded = onDataLoaded;
    }

    public void loadWarehousesAsync() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                loadWarehouses();
                return null;
            }

            @Override
            protected void done() {
                if (onDataLoaded != null) onDataLoaded.run();
            }
        }.execute();
    }

    public void loadWarehouses() {
        String query = """
        SELECT 
            warehouse_id, 
            warehouse_name, 
            contact_name, 
            phone, 
            email, 
            address, 
            CASE 
                WHEN is_active THEN 'Active'
                ELSE 'Inactive'
            END AS status
        FROM warehouses
        ORDER BY warehouse_name ASC;
        """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0);  // Clear previous data

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("warehouse_id"),
                        rs.getString("warehouse_name"),
                        rs.getString("contact_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean addWarehouse(String name, String contact, String phone, String email, String address) {
        if (name.isEmpty() || email.isEmpty()) {
            System.err.println("❌ Validation Error: Warehouse name and email are required.");
            return false;
        }

        String query = """
        INSERT INTO warehouses (warehouse_name, contact_name, phone, email, address, is_active)
        VALUES (?, ?, ?, ?, ?, TRUE);
        """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, contact);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, address);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                loadWarehousesAsync();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Database Error in addWarehouse(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public String[] getWarehouseNames() {
        List<String> warehouses = new ArrayList<>();
        String query = "SELECT warehouse_name FROM warehouses WHERE is_active = TRUE ORDER BY warehouse_name ASC";

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

    public Object[] getWarehouseDetailsById(int warehouseId) {
        String query = """
        SELECT warehouse_name, contact_name, phone, email, address, is_active
        FROM warehouses
        WHERE warehouse_id = ?
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, warehouseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                            rs.getString("warehouse_name"),
                            rs.getString("contact_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("address"),
                            rs.getBoolean("is_active")
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
