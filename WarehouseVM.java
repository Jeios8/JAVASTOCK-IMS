package main.java.com.javastock.viewmodel;

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
                new String[]{"Warehouse ID", "Warehouse Name", "Location", "Contact Name", "Phone"},
                0
        );
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setOnDataLoaded(Runnable onDataLoaded) {
        this.onDataLoaded = onDataLoaded;
    }

    public void loadWarehouseAsync() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                loadWarehouse();
                return null;
            }

            @Override
            protected void done() {
                if (onDataLoaded != null) onDataLoaded.run();
            }
        }.execute();
    }

    public void loadWarehouse() {
        String query = """
    SELECT warehouse_id, warehouse_name, location, contact_name, phone
    FROM warehouses;
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0);  // Clear previous data

            while (rs.next()) {
                // Ensure the array size matches exactly the number of columns in DefaultTableModel
                Object[] rowData = new Object[tableModel.getColumnCount()];

                rowData[0] = rs.getInt("warehouse_id");
                rowData[1] = rs.getString("warehouse_name");
                rowData[2] = rs.getString("location");
                rowData[3] = rs.getString("contact_name");
                rowData[4] = rs.getString("phone");

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

    public boolean validateAndAddWarehouse(String name, String location, String contact, String phone) {
        
        boolean success = addWarehouse(name, location, contact, phone);
        if (success) {
            loadWarehouseAsync();
        }

        return success;
    }

    private boolean addWarehouse(String name, String location, String contact, String phone) {
        Connection conn = null;
        PreparedStatement warehouseStmt = null;
        // PreparedStatement warehouseStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            // **Insert into warehouses table**
            String warehouseQuery = """
        INSERT INTO warehouses (warehouse_name, location, contact_name, phone)
        VALUES (?, ?, ?, ?)
        """;

            warehouseStmt = conn.prepareStatement(warehouseQuery, Statement.RETURN_GENERATED_KEYS);
            warehouseStmt.setString(1, name);
            warehouseStmt.setString(2, location);
            warehouseStmt.setString(3, contact);
            warehouseStmt.setString(4, phone);

            int affectedRows = warehouseStmt.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            // **Retrieve generated warehouse_id**
            generatedKeys = warehouseStmt.getGeneratedKeys();
            int warehouseId = -1;
            if (generatedKeys.next()) {
                warehouseId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return false;
            }

            // **Insert into warehouseINVENTORY table**
//            String warehouseQuery = """
//        INSERT INTO warehouse (product_id, warehouse_id, quantity, status)
//        VALUES (?, (SELECT warehouse_id FROM warehouses WHERE warehouse_name = ? LIMIT 1), ?, 'Received')
//        """;
//
//            warehouseStmt = conn.prepareStatement(warehouseQuery);
//            warehouseStmt.setInt(1, productId);
//            warehouseStmt.setString(2, warehouse);
//            warehouseStmt.setInt(3, quantity);
//
//            int warehouseRows = warehouseStmt.executeUpdate();
//            if (warehouseRows == 0) {
//                conn.rollback();
//                return false;
//            }
//
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
                //  if (productStmt != null) productStmt.close();
                if (warehouseStmt != null) warehouseStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

//    public String[] getCategories() {
//        List<String> categories = new ArrayList<>();
//        String query = "SELECT category_name FROM categories WHERE is_active = true";
//
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                categories.add(rs.getString("category_name"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return categories.toArray(new String[0]);
//    }
//
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

//    public String[] getWarehouses() {
//        List<String> warehouses = new ArrayList<>();
//        String query = "SELECT warehouse_name FROM warehouses WHERE is_active = true";
//
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                warehouses.add(rs.getString("warehouse_name"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return warehouses.toArray(new String[0]);
//    }
//
//    public void filterWarehouse(String availability) {
//        String query = """
//    SELECT p.product_id, p.product_name, p.unit_price, i.quantity, p.reorder_level,
//           CASE
//               WHEN i.quantity = 0 THEN 'Out of stock'
//               WHEN i.quantity <= p.reorder_level THEN 'Low stock'
//               ELSE 'In-stock'
//           END AS availability
//    FROM warehouse i
//    JOIN products p ON i.product_id = p.product_id
//    WHERE (CASE
//               WHEN i.quantity = 0 THEN 'Out of stock'
//               WHEN i.quantity <= p.reorder_level THEN 'Low stock'
//               ELSE 'In-stock'
//           END) = ?;
//    """;
//
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setString(1, availability);
//            ResultSet rs = stmt.executeQuery();
//            tableModel.setRowCount(0);
//
//            while (rs.next()) {
//                tableModel.addRow(new Object[]{
//                        rs.getInt("product_id"),
//                        rs.getString("product_name"),
//                        "₱" + rs.getDouble("unit_price"),
//                        rs.getInt("quantity") + " Units",
//                        rs.getInt("reorder_level") + " Units",
//                        rs.getString("availability")
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}