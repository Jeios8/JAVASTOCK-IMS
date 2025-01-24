package main.java.com.javastock.viewmodel;

import main.java.com.javastock.dao.SupplierDAO;
import main.java.com.javastock.model.Supplier;
import main.java.com.javastock.utils.DatabaseConnector;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingWorker;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierVM {
    private DefaultTableModel tableModel;
    private Runnable onDataLoaded; // Callback to update UI

    public SupplierVM() {
        tableModel = new DefaultTableModel(
                new String[]{"Supplier ID", "Supplier Name", "Contact Name", "Phone", "Email", "Address", "Status"},
                0
        );
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setOnDataLoaded(Runnable onDataLoaded) {
        this.onDataLoaded = onDataLoaded;
    }

    public void loadSuppliersAsync() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                loadSuppliers();
                return null;
            }

            @Override
            protected void done() {
                if (onDataLoaded != null) onDataLoaded.run();
            }
        }.execute();
    }

    public void loadSuppliers() {
        String query = """
        SELECT 
            supplier_id, 
            supplier_name, 
            contact_name, 
            phone, 
            email, 
            address, 
            CASE 
                WHEN is_active THEN 'Active'
                ELSE 'Inactive'
            END AS status
        FROM suppliers
        ORDER BY supplier_name ASC;
        """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0);  // Clear previous data

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
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

    public boolean addSupplier(String name, String contact, String phone, String email, String address) {
        if (name.isEmpty() || email.isEmpty()) {
            System.err.println("❌ Validation Error: Supplier name and email are required.");
            return false;
        }

        String query = """
        INSERT INTO suppliers (supplier_name, contact_name, phone, email, address, is_active)
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
                loadSuppliersAsync();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Database Error in addSupplier(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public String[] getSupplierNames() {
        List<String> suppliers = new ArrayList<>();
        String query = "SELECT supplier_name FROM suppliers WHERE is_active = TRUE ORDER BY supplier_name ASC";

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

    public Object[] getSupplierDetailsById(int supplierId) {
        String query = """
        SELECT supplier_name, contact_name, phone, email, address, is_active
        FROM suppliers
        WHERE supplier_id = ?
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, supplierId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                            rs.getString("supplier_name"),
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
