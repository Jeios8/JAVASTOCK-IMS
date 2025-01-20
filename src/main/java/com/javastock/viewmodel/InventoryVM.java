package main.java.com.javastock.viewmodel;

import main.java.com.javastock.utils.DatabaseConnector;

import javax.swing.table.DefaultTableModel;
import javax.swing.SwingWorker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Consumer;

public class InventoryVM {
    private DefaultTableModel tableModel;
    private Consumer<Void> onDataLoaded; // Callback when data is ready

    public InventoryVM() {
        tableModel = new DefaultTableModel(
                new String[]{"Product", "Buying Price", "Quantity", "Threshold", "Expiry Date", "Availability"},
                0
        );
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    // Set callback for notifying UI when data is loaded
    public void setOnDataLoaded(Consumer<Void> onDataLoaded) {
        this.onDataLoaded = onDataLoaded;
    }

    // Asynchronous Data Loader
    public void loadInventoryAsync() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                loadInventory(); // Fetch data in background
                return null;
            }

            @Override
            protected void done() {
                if (onDataLoaded != null) {
                    onDataLoaded.accept(null); // Notify UI that data is ready
                }
            }
        }.execute();
    }

    public void loadInventory() {
        String query = """
            SELECT p.product_name, p.unit_price, i.quantity, p.reorder_level, i.last_updated,
                   CASE 
                       WHEN i.quantity = 0 THEN 'Out of stock'
                       WHEN i.quantity <= p.reorder_level THEN 'Low stock'
                       ELSE 'In-stock'
                   END AS availability
            FROM inventory i
            JOIN products p ON i.product_id = p.product_id;
        """;

        try (Connection conn = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("product_name"),
                        "₱" + rs.getDouble("unit_price"),
                        rs.getInt("quantity") + " Units",
                        rs.getInt("reorder_level") + " Units",
                        rs.getDate("last_updated") != null ? rs.getDate("last_updated").toString() : "N/A",
                        rs.getString("availability")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filterInventory(String availability) {
        String query = """
        SELECT p.product_name, p.unit_price, i.quantity, p.reorder_level, i.last_updated,
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

        try (Connection conn = DatabaseConnector.getConnection("errjava_invdb");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, availability);
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("product_name"),
                        "₱" + rs.getDouble("unit_price"),
                        rs.getInt("quantity") + " Units",
                        rs.getInt("reorder_level") + " Units",
                        rs.getDate("last_updated").toString(),
                        rs.getString("availability")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
