package main.java.com.javastock.viewmodel;

import main.java.com.javastock.dao.InventoryDAO;
import main.java.com.javastock.dao.ProductDAO;
import main.java.com.javastock.model.Inventory;
import main.java.com.javastock.model.Product;
import main.java.com.javastock.utils.DatabaseConnector;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingWorker;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InventoryVM {
    private DefaultTableModel tableModel;
    private Runnable onDataLoaded; // Callback to update UI

    /**
     * Constructor initializes the table model with predefined column headers.
     */
    public InventoryVM() {
        tableModel = new DefaultTableModel(
                new String[]{"Product ID", "Product Name", "Category",
                        "Supplier", "Warehouse", "Quantity",
                        "Availability", "Threshold", "Expiration Date",
                        "Last Updated"},
                0
        );
    }

    /**
     * Retrieves the inventory table model.
     *
     * @return The DefaultTableModel containing inventory data.
     */
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    /**
     * Sets the callback to be executed when data loading is complete.
     *
     * @param onDataLoaded Runnable callback function.
     */
    public void setOnDataLoaded(Runnable onDataLoaded) {
        this.onDataLoaded = onDataLoaded;
    }

    /**
     * Loads inventory asynchronously using a SwingWorker to prevent UI freezing.
     */
    public void loadInventoryAsync() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                loadInventory();
                return null;
            }

            @Override
            protected void done() {
                if (onDataLoaded != null) onDataLoaded.run();
            }
        }.execute();
    }
    /**
     * Loads inventory data from the database and updates the table model.
     */
    public void loadInventory() {
        String query = """
        SELECT 
            p.product_id, 
            p.product_name, 
            c.category_name, 
            s.supplier_name, 
            w.warehouse_name, 
            i.quantity, 
            CASE 
                WHEN i.quantity = 0 THEN 'Out of stock'
                WHEN i.quantity <= p.reorder_level THEN 'Low stock'
                ELSE 'In-stock'
            END AS stock_status,
            p.reorder_level,
            COALESCE(i.expiration_date, 'N/A') AS expiration_date, 
            i.last_updated
        FROM inventory i
        JOIN products p ON i.product_id = p.product_id
        JOIN categories c ON p.category_id = c.category_id
        JOIN suppliers s ON p.supplier_id = s.supplier_id
        JOIN warehouses w ON i.warehouse_id = w.warehouse_id
        ORDER BY p.product_name ASC;
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0);  // Clear previous data

            while (rs.next()) {
                Object[] rowData = new Object[tableModel.getColumnCount()];

                rowData[0] = rs.getInt("product_id");
                rowData[1] = rs.getString("product_name");
                rowData[2] = rs.getString("category_name");
                rowData[3] = rs.getString("supplier_name");
                rowData[4] = rs.getString("warehouse_name");
                rowData[5] = rs.getInt("quantity");
                rowData[6] = rs.getString("stock_status");
                rowData[7] = rs.getInt("reorder_level");
                rowData[8] = rs.getString("expiration_date");
                rowData[9] = rs.getTimestamp("last_updated");

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

    /**
     * Validates input data and adds a new product to the inventory.
     *
     * @param name           Product name.
     * @param price          Unit price as a string.
     * @param qty            Quantity as a string.
     * @param threshold      Reorder level as a string.
     * @param category       Product category.
     * @param supplier       Supplier name.
     * @param warehouse      Warehouse location.
     * @param expirationDateStr Expiration date as a string.
     * @return True if product addition is successful, false otherwise.
     */
    public boolean validateAndAddProduct(String name, String price, String qty, String threshold,
                                         String category, String supplier, String warehouse, String expirationDateStr) {
        double unitPrice;
        int quantity, reorderLevel;
        Date expirationDate = null;

        try {
            unitPrice = Double.parseDouble(price);
            quantity = Integer.parseInt(qty);
            reorderLevel = Integer.parseInt(threshold);
        } catch (NumberFormatException e) {
            System.err.println("Validation Error: Invalid number format.");
            return false;
        }

        // Convert Expiration Date (only if provided)
        if (expirationDateStr != null && !expirationDateStr.isEmpty() && !expirationDateStr.equalsIgnoreCase("N/A")) {
            expirationDate = parseDate(expirationDateStr);
            if (expirationDate == null) {
                System.err.println("Validation Error: Invalid expiration date.");
                return false;
            }
        }

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);

            // Check if product already exists
            ProductDAO productDAO = new ProductDAO();
            Product existingProduct = productDAO.getProductByName(name);

            boolean success = addProduct(conn, existingProduct, name, unitPrice, quantity, reorderLevel,
                    category, supplier, warehouse, expirationDate);

            if (success) {
                conn.commit();
                loadInventoryAsync();
            } else {
                conn.rollback();
            }

            return success;
        } catch (SQLException e) {
            System.err.println("Database Error in validateAndAddProduct(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Utility method to convert a date string (YYYY-MM-DD) to `java.sql.Date`.
     *
     * @param dateStr The date string.
     * @return A `java.sql.Date` object, or null if the format is invalid.
     */
    private Date parseDate(String dateStr) {
        try {
            return Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Adds a new product to the inventory or updates the quantity if the product already exists.
     * This method inserts a new product if it does not exist or updates the stock for an existing product.
     *
     * @param conn            The database connection to use for the operation.
     * @param existingProduct The existing product instance if found, or null if the product is new.
     * @param name            The name of the product.
     * @param unitPrice       The price per unit of the product.
     * @param quantity        The initial stock quantity of the product.
     * @param reorderLevel    The threshold at which the product should be reordered.
     * @param category        The category to which the product belongs.
     * @param supplier        The supplier of the product.
     * @param warehouse       The warehouse where the product is stored.
     * @param expirationDate  The expiration date of the product, if applicable.
     * @return True if the product is successfully added or updated, false otherwise.
     */
    private boolean addProduct(Connection conn, Product existingProduct, String name, double unitPrice, int quantity,
                               int reorderLevel, String category, String supplier, String warehouse, Date expirationDate) {
        PreparedStatement productStmt = null;
        ResultSet generatedKeys = null;

        try {
            ProductDAO productDAO = new ProductDAO();
            InventoryDAO inventoryDAO = new InventoryDAO();
            int productId;

            // If the product does not exist, insert it into the products table
            if (existingProduct == null) {
                String productQuery = """
                INSERT INTO products (product_name, unit_price, reorder_level, category_id, supplier_id)
                VALUES (?, ?, ?, 
                        (SELECT category_id FROM categories WHERE category_name = ? LIMIT 1),
                        (SELECT supplier_id FROM suppliers WHERE supplier_name = ? LIMIT 1))
            """;

                productStmt = conn.prepareStatement(productQuery, Statement.RETURN_GENERATED_KEYS);
                productStmt.setString(1, name);
                productStmt.setDouble(2, unitPrice);
                productStmt.setInt(3, reorderLevel);
                productStmt.setString(4, category);
                productStmt.setString(5, supplier);

                // Execute the insert statement
                int affectedRows = productStmt.executeUpdate();
                if (affectedRows == 0) {
                    return false; // No rows affected, insertion failed
                }

                // Retrieve the generated product ID
                generatedKeys = productStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    productId = generatedKeys.getInt(1);
                } else {
                    return false; // Failed to get product ID
                }
            } else {
                // If the product exists, get its product ID
                productId = existingProduct.getProductId();
            }

            // Fetch the warehouse ID for the given warehouse name
            int warehouseId = inventoryDAO.getWarehouseIdByName(warehouse);
            if (warehouseId == -1) {
                System.err.println("Error: Warehouse not found: " + warehouse);
                return false;
            }

            // Check if inventory already exists for this product in the specified warehouse
            Inventory existingInventory = inventoryDAO.getInventoryByProductAndWarehouse(productId, warehouseId);

            if (existingInventory == null) {
                // If inventory does not exist, insert a new record
                boolean insertSuccess = inventoryDAO.insertInventory(conn, new Inventory(productId, warehouseId, quantity, expirationDate));
                if (!insertSuccess) {
                    conn.rollback(); // Rollback transaction on failure
                    return false;
                }
            } else {
                // If inventory exists, update the quantity
                int newQuantity = existingInventory.getQuantity() + quantity;
                boolean updateSuccess = inventoryDAO.updateInventoryQuantity(conn, existingInventory.getInventoryId(), newQuantity);
                if (!updateSuccess) {
                    conn.rollback(); // Rollback transaction on failure
                    return false;
                }
            }

            return true; // Operation successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Close resources to prevent memory leaks
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (productStmt != null) productStmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * Retrieves all active product categories from the database.
     *
     * @return An array of category names.
     */
    public String[] getCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT category_name FROM categories WHERE is_active = true";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories.toArray(new String[0]);
    }

    /**
     * Retrieves all active suppliers from the database.
     *
     * @return An array of supplier names.
     */
    public String[] getSuppliers() {
        List<String> suppliers = new ArrayList<>();
        String query = "SELECT supplier_name FROM suppliers WHERE is_active = true";

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

    /**
     * Retrieves all active warehouses from the database.
     *
     * @return An array of warehouse names.
     */
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

    /**
     * Filters the inventory based on stock status (e.g., "In-stock", "Low stock", "Out of stock").
     * This method retrieves inventory records from the database that match the given stock status
     * and updates the table model accordingly.
     *
     * @param stockStatus The stock status to filter by (e.g., "Low stock", "Out of stock", "In-stock").
     */
    public void filterInventory(String stockStatus) {
        String query = """
        SELECT 
            p.product_id, 
            p.product_name, 
            c.category_name, 
            s.supplier_name, 
            w.warehouse_name, 
            i.quantity, 
            CASE 
                WHEN i.quantity = 0 THEN 'Out of stock'
                WHEN i.quantity <= p.reorder_level THEN 'Low stock'
                ELSE 'In-stock'
            END AS stock_status,
            p.reorder_level,
            COALESCE(i.expiration_date, 'N/A') AS expiration_date, 
            i.last_updated
        FROM inventory i
        JOIN products p ON i.product_id = p.product_id
        JOIN categories c ON p.category_id = c.category_id
        JOIN suppliers s ON p.supplier_id = s.supplier_id
        JOIN warehouses w ON i.warehouse_id = w.warehouse_id
        HAVING stock_status = ?; -- Optimized filtering
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Bind the stock status parameter to the prepared statement
            stmt.setString(1, stockStatus);
            ResultSet rs = stmt.executeQuery();

            // Clear previous table data before inserting new rows
            tableModel.setRowCount(0);

            // Iterate through the result set and populate the table model
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category_name"),
                        rs.getString("supplier_name"),
                        rs.getString("warehouse_name"),
                        rs.getInt("quantity"),
                        rs.getString("stock_status"),
                        rs.getInt("reorder_level"),
                        rs.getString("expiration_date"),
                        rs.getTimestamp("last_updated")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}