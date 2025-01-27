package main.java.com.javastock.viewmodel;

import main.java.com.javastock.utils.DatabaseConnector;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingWorker;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersVM {
    private final DefaultTableModel salesOrdersTableModel;
    private final DefaultTableModel purchaseOrdersTableModel;
    private Runnable onDataLoaded;

    public OrdersVM() {
        salesOrdersTableModel = new DefaultTableModel(
                new String[]{"Order ID", "Customer", "Order Date", "Status"}, 0
        );
        purchaseOrdersTableModel = new DefaultTableModel(
                new String[]{"Order ID", "Supplier", "Order Date", "Expected Arrival", "Status"}, 0
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
                loadOrders(true);
                loadOrders(false);
                return null;
            }

            @Override
            protected void done() {
                if (onDataLoaded != null) onDataLoaded.run();
            }
        }.execute();
    }

    private void loadOrders(boolean isSalesOrder) {
        String query = isSalesOrder ? """
        SELECT so.sales_order_id, CONCAT(c.first_name, ' ', c.last_name) AS customer, 
               so.order_date, so.status
        FROM sales_orders so
        LEFT JOIN customers c ON so.customer_id = c.customer_id
        ORDER BY so.order_date DESC;
        """ :
                """
                SELECT po.purchase_order_id, s.supplier_name, po.order_date, po.expected_arrival_date, po.status
                FROM purchase_orders po
                LEFT JOIN suppliers s ON po.supplier_id = s.supplier_id
                ORDER BY po.order_date DESC;
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            DefaultTableModel tableModel = isSalesOrder ? salesOrdersTableModel : purchaseOrdersTableModel;
            tableModel.setRowCount(0);

            while (rs.next()) {
                tableModel.addRow(isSalesOrder ?
                        new Object[]{rs.getInt(1), rs.getString(2) != null ? rs.getString(2) : "Guest", rs.getDate(3), rs.getString(4)} :
                        new Object[]{rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getDate(4), rs.getString(5)});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object[] getOrderDetailsById(int orderId, boolean isSalesOrder) {
        String query = isSalesOrder ? """
            SELECT so.sales_order_id, CONCAT(c.first_name, ' ', c.last_name) AS customer, 
                   so.order_date, so.status
            FROM sales_orders so
            LEFT JOIN customers c ON so.customer_id = c.customer_id
            WHERE so.sales_order_id = ?
            """ :
                """
                SELECT po.purchase_order_id, s.supplier_name, po.order_date, 
                       po.expected_arrival_date, po.status
                FROM purchase_orders po
                LEFT JOIN suppliers s ON po.supplier_id = s.supplier_id
                WHERE po.purchase_order_id = ?
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return isSalesOrder ?
                            new Object[]{rs.getInt(1), rs.getString(2) != null ? rs.getString(2) : "Guest", rs.getDate(3), rs.getString(4)} :
                            new Object[]{rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getDate(4), rs.getString(5)};
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addCustomerOrder(String firstName, String lastName, String phone, String email, String productName, int quantity) {
        int customerId = getOrCreateCustomer(firstName, lastName, phone, email);
        if (customerId == -1) return false;

        return insertOrder(customerId, productName, quantity, true);
    }

    public boolean addSupplierOrder(int supplierId, String productName, int quantity, int warehouseId, String arrivalDate) {
        return insertOrder(supplierId, productName, quantity, false, warehouseId, arrivalDate);
    }

    private boolean insertOrder(int entityId, String productName, int quantity, boolean isCustomerOrder, Object... extraParams) {
        String orderQuery = isCustomerOrder ? """
            INSERT INTO sales_orders (customer_id, order_date, status)
            VALUES (?, CURDATE(), 'Pending')
            """ :
                """
                INSERT INTO purchase_orders (supplier_id, order_date, expected_arrival_date, status)
                VALUES (?, CURDATE(), ?, 'Pending')
                """;

        String itemQuery = isCustomerOrder ? """
            INSERT INTO sales_order_items (sales_order_id, product_id, quantity, unit_price)
            VALUES ((SELECT MAX(sales_order_id) FROM sales_orders), 
                    (SELECT product_id FROM products WHERE product_name = ? LIMIT 1), ?, 
                    (SELECT unit_price FROM products WHERE product_name = ? LIMIT 1))
            """ :
                """
                INSERT INTO purchase_order_items (purchase_order_id, product_id, quantity, unit_price)
                VALUES ((SELECT MAX(purchase_order_id) FROM purchase_orders), 
                        (SELECT product_id FROM products WHERE product_name = ? LIMIT 1), ?, 
                        (SELECT unit_price FROM products WHERE product_name = ? LIMIT 1))
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderQuery);
             PreparedStatement itemStmt = conn.prepareStatement(itemQuery)) {

            orderStmt.setInt(1, entityId);
            if (!isCustomerOrder) {
                orderStmt.setString(2, (String) extraParams[1]); // expected_arrival_date
            }
            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows > 0) {
                itemStmt.setString(1, productName);
                itemStmt.setInt(2, quantity);
                itemStmt.setString(3, productName);
                itemStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getOrCreateCustomer(String firstName, String lastName, String phone, String email) {
        return getOrCreateEntity(
                "customers", "customer_id", "first_name = ? AND last_name = ? AND phone = ? AND email = ?",
                new String[]{firstName, lastName, phone, email},
                new String[]{"first_name", "last_name", "phone", "email", "is_active"},
                new String[]{firstName, lastName, phone, email, "1"}
        );
    }

    public int getSupplierIdByName(String supplierName) {
        return getEntityId("suppliers", "supplier_id", "supplier_name", supplierName);
    }

    public int getWarehouseIdByName(String warehouseName) {
        return getEntityId("warehouses", "warehouse_id", "warehouse_name", warehouseName);
    }

    private int getEntityId(String tableName, String idColumn, String nameColumn, String nameValue) {
        String query = "SELECT " + idColumn + " FROM " + tableName + " WHERE " + nameColumn + " = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nameValue);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getInt(idColumn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getOrCreateEntity(String tableName, String idColumn, String condition, String[] conditionValues,
                                  String[] insertColumns, String[] insertValues) {
        String checkQuery = "SELECT " + idColumn + " FROM " + tableName + " WHERE " + condition + " LIMIT 1";
        String insertQuery = "INSERT INTO " + tableName + " (" + String.join(", ", insertColumns) + ") " +
                "VALUES (" + "?,".repeat(insertColumns.length - 1) + "?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            // **1️⃣ Check if the entity exists**
            for (int i = 0; i < conditionValues.length; i++) {
                checkStmt.setString(i + 1, conditionValues[i]);
            }
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(idColumn); // ✅ Return existing entity ID
            }

            // **2️⃣ Insert new entity**
            for (int i = 0; i < insertValues.length; i++) {
                insertStmt.setString(i + 1, insertValues[i]);
            }
            int affectedRows = insertStmt.executeUpdate();

            // **3️⃣ Retrieve and return generated ID**
            if (affectedRows > 0) {
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // ✅ Return new entity ID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // ❌ Return -1 if creation failed
    }

    public String[] getProductNames() {
        List<String> products = new ArrayList<>();
        String query = "SELECT product_name FROM products ORDER BY product_name ASC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(rs.getString("product_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products.toArray(new String[0]);
    }

}
