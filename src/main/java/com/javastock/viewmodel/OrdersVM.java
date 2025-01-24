package main.java.com.javastock.viewmodel;

import main.java.com.javastock.utils.DatabaseConnector;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingWorker;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersVM {
    private DefaultTableModel salesOrdersTableModel;
    private DefaultTableModel purchaseOrdersTableModel;
    private Runnable onDataLoaded; // Callback to update UI

    public OrdersVM() {
        salesOrdersTableModel = new DefaultTableModel(
                new String[]{"Order ID", "Customer", "Order Date", "Status"},
                0
        );

        purchaseOrdersTableModel = new DefaultTableModel(
                new String[]{"Order ID", "Supplier", "Order Date", "Expected Arrival", "Status"},
                0
        );
    }

    public DefaultTableModel getSalesOrdersTableModel() {
        return salesOrdersTableModel;
    }

    public DefaultTableModel getPurchaseOrdersTableModel() {
        return purchaseOrdersTableModel;
    }

    public void setOnDataLoaded(Runnable onDataLoaded) {
        this.onDataLoaded = onDataLoaded;
    }

    public void loadOrdersAsync() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                loadSalesOrders();
                loadPurchaseOrders();
                return null;
            }

            @Override
            protected void done() {
                if (onDataLoaded != null) onDataLoaded.run();
            }
        }.execute();
    }

    private void loadSalesOrders() {
        String query = """
        SELECT 
            so.sales_order_id AS order_id,
            CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
            so.order_date,
            so.status
        FROM sales_orders so
        LEFT JOIN customers c ON so.customer_id = c.customer_id
        ORDER BY so.order_date DESC;
        """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            salesOrdersTableModel.setRowCount(0); // Clear previous data

            while (rs.next()) {
                salesOrdersTableModel.addRow(new Object[]{
                        rs.getInt("order_id"),
                        rs.getString("customer_name") != null ? rs.getString("customer_name") : "Guest",
                        rs.getDate("order_date"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPurchaseOrders() {
        String query = """
        SELECT 
            po.purchase_order_id AS order_id,
            s.supplier_name,
            po.order_date,
            po.expected_arrival_date,
            po.status
        FROM purchase_orders po
        LEFT JOIN suppliers s ON po.supplier_id = s.supplier_id
        ORDER BY po.order_date DESC;
        """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            purchaseOrdersTableModel.setRowCount(0); // Clear previous data

            while (rs.next()) {
                purchaseOrdersTableModel.addRow(new Object[]{
                        rs.getInt("order_id"),
                        rs.getString("supplier_name"),
                        rs.getDate("order_date"),
                        rs.getDate("expected_arrival_date"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object[] getOrderDetailsById(int orderId, boolean isSalesOrder) {
        String query;
        if (isSalesOrder) {
            query = """
            SELECT 
                so.sales_order_id AS order_id,
                CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
                so.order_date,
                so.status
            FROM sales_orders so
            LEFT JOIN customers c ON so.customer_id = c.customer_id
            WHERE so.sales_order_id = ?
            """;
        } else {
            query = """
            SELECT 
                po.purchase_order_id AS order_id,
                s.supplier_name,
                po.order_date,
                po.expected_arrival_date,
                po.status
            FROM purchase_orders po
            LEFT JOIN suppliers s ON po.supplier_id = s.supplier_id
            WHERE po.purchase_order_id = ?
            """;
        }

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    if (isSalesOrder) {
                        return new Object[]{
                                rs.getInt("order_id"),
                                rs.getString("customer_name") != null ? rs.getString("customer_name") : "Guest",
                                rs.getDate("order_date"),
                                rs.getString("status")
                        };
                    } else {
                        return new Object[]{
                                rs.getInt("order_id"),
                                rs.getString("supplier_name"),
                                rs.getDate("order_date"),
                                rs.getDate("expected_arrival_date"),
                                rs.getString("status")
                        };
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getCustomerNames() {
        List<String> customers = new ArrayList<>();
        String query = "SELECT CONCAT(first_name, ' ', last_name) FROM customers WHERE is_active = TRUE ORDER BY first_name ASC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                customers.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers.toArray(new String[0]);
    }

    public boolean addOrder(int customerId, String orderDate, String status) {
        String query = """
        INSERT INTO sales_orders (customer_id, order_date, status) 
        VALUES (?, ?, ?);
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerId);
            stmt.setDate(2, Date.valueOf(orderDate));
            stmt.setString(3, status);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("❌ Database Error in addOrder(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public DefaultTableModel getCustomerOrdersTableModel() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Order ID", "Customer", "Date", "Status"}, 0);

        String query = """
        SELECT o.sales_order_id, CONCAT(c.first_name, ' ', c.last_name) AS customer, 
               o.order_date, o.status
        FROM sales_orders o
        JOIN customers c ON o.customer_id = c.customer_id
        ORDER BY o.order_date DESC;
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("sales_order_id"),
                        rs.getString("customer"),
                        rs.getDate("order_date"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }
    public DefaultTableModel getSupplierOrdersTableModel() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"PO ID", "Supplier", "Date", "Status"}, 0);

        String query = """
        SELECT po.purchase_order_id, s.supplier_name, po.order_date, po.status
        FROM purchase_orders po
        JOIN suppliers s ON po.supplier_id = s.supplier_id
        ORDER BY po.order_date DESC;
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("purchase_order_id"),
                        rs.getString("supplier_name"),
                        rs.getDate("order_date"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

}
