package main.java.com.javastock.view;

import main.java.com.javastock.utils.DataPreloader;
import main.java.com.javastock.utils.DatabaseConnector;
import main.java.com.javastock.viewmodel.OrdersVM;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

public class CreateSupplierOrderDialog extends JDialog {
    private JComboBox<String> supplierDropdown, itemDropdown, warehouseDropdown;
    private JTextField quantityField;
    private JDatePickerImpl arrivalDatePicker;
    private JButton addButton, cancelButton;
    private OrdersVM ordersVM;

    public CreateSupplierOrderDialog(JFrame parent, OrdersVM ordersVM) {
        super(parent, "Create Supplier Order", true);
        this.ordersVM = ordersVM;
        setLayout(new BorderLayout());
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(false);

        ImageIcon img = new ImageIcon("src/main/resources/icons/icon_app.png");
        setIconImage(img.getImage());

        // **Main Panel**
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.4;

        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.gridx = 1;
        inputGbc.gridy = 0;
        inputGbc.weightx = 0.6;
        inputGbc.fill = GridBagConstraints.HORIZONTAL;

        // **Supplier Information Panel**
        supplierDropdown = createRoundedDropdown(DataPreloader.getData("suppliers"));
        arrivalDatePicker = createDatePicker();

        mainPanel.add(new JLabel("Supplier Name:"), gbc);
        mainPanel.add(supplierDropdown, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Expected Arrival:"), gbc);
        mainPanel.add(arrivalDatePicker, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        // **Order Details Panel**
        itemDropdown = createRoundedDropdown(DataPreloader.getData("products"));
        quantityField = createTextField("Enter Quantity");
        warehouseDropdown = createRoundedDropdown(DataPreloader.getData("warehouses"));

        mainPanel.add(new JLabel("Item Name:"), gbc);
        mainPanel.add(itemDropdown, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Quantity:"), gbc);
        mainPanel.add(quantityField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Warehouse to Assign:"), gbc);
        mainPanel.add(warehouseDropdown, inputGbc);

        add(mainPanel, BorderLayout.CENTER);

        // **Buttons Panel**
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        addButton = createRoundedButton("Create Order", new Color(0, 123, 255)); // Blue
        cancelButton = createRoundedButton("Cancel", new Color(220, 53, 69)); // Red

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // **Button Actions**
        addButton.addActionListener(e -> createOrder());
        cancelButton.addActionListener(e -> dispose());

        // **Load dropdown data asynchronously**
        loadDropdownData();

        setVisible(true);
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setColumns(15);
        return field;
    }

    private JComboBox<String> createRoundedDropdown(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBorder(new RoundedBorder(10));
        comboBox.setPreferredSize(new Dimension(200, 30));
        return comboBox;
    }

    private JButton createRoundedButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(new RoundedBorder(15));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private final int radius;
        public RoundedBorder(int radius) {
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.GRAY);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }
    }

    private void loadDropdownData() {
        new SwingWorker<String[], Void>() {
            @Override
            protected String[] doInBackground() {
                return ordersVM.getProductNames();
            }
            @Override
            protected void done() {
                try {
                    String[] data = get();
                    itemDropdown.setModel(new DefaultComboBoxModel<>(data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void createOrder() {
        // Convert supplier and warehouse names to IDs
        int supplierId = ordersVM.getSupplierIdByName((String) supplierDropdown.getSelectedItem()); // ✅ Converts name to ID
        int warehouseId = ordersVM.getWarehouseIdByName((String) warehouseDropdown.getSelectedItem()); // ✅ Converts name to ID

        // Check if conversion was successful (-1 means not found)
        if (supplierId == -1 || warehouseId == -1) {
            JOptionPane.showMessageDialog(this, "Invalid Supplier or Warehouse!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = ordersVM.addSupplierOrder(
                supplierId,  // ✅ Now passing an int
                (String) itemDropdown.getSelectedItem(),
                Integer.parseInt(quantityField.getText()),
                warehouseId,  // ✅ Now passing an int
                arrivalDatePicker.getModel().getValue() != null ?
                        new SimpleDateFormat("yyyy-MM-dd").format(arrivalDatePicker.getModel().getValue()) : "N/A"
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Supplier order created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, getDateProperties());
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setPreferredSize(new Dimension(200, 30));
        return datePicker;
    }

    private Properties getDateProperties() {
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        return p;
    }

    class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormat.parse(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                return dateFormat.format(((java.util.Calendar) value).getTime());
            }
            return "";
        }
    }

    private int getIdOrCreate(String table, String idColumn, String condition, String[] params) {
        String checkQuery = "SELECT " + idColumn + " FROM " + table + " WHERE " + condition + " LIMIT 1";
        String insertQuery = "INSERT INTO " + table + " (" + condition.replace(" AND ", ", ").replace(" = ?", "") + ") VALUES (" +
                "?,".repeat(params.length - 1) + "?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            // **Set values for check query**
            for (int i = 0; i < params.length; i++) {
                checkStmt.setString(i + 1, params[i]);
            }
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) return rs.getInt(idColumn); // ✅ Return existing ID

            // **Insert new record if not found**
            for (int i = 0; i < params.length; i++) {
                insertStmt.setString(i + 1, params[i]);
            }
            if (insertStmt.executeUpdate() > 0) {
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) return generatedKeys.getInt(1); // ✅ Return new ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // ❌ Return -1 if failed
    }

    public boolean addSupplierOrder(int supplierId, String itemName, int quantity, int warehouseId, String arrivalDate) {
        String orderQuery = """
    INSERT INTO purchase_orders (supplier_id, order_date, expected_arrival_date, status)
    VALUES (?, CURDATE(), ?, 'Pending');
    """;

        String itemQuery = """
    INSERT INTO purchase_order_items (purchase_order_id, product_id, quantity, unit_price)
    VALUES ((SELECT MAX(purchase_order_id) FROM purchase_orders), 
            (SELECT product_id FROM products WHERE product_name = ? LIMIT 1), ?, 
            (SELECT unit_price FROM products WHERE product_name = ? LIMIT 1));
    """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement itemStmt = conn.prepareStatement(itemQuery)) {

            // **Insert into `purchase_orders`**
            orderStmt.setInt(1, supplierId);
            orderStmt.setDate(2, Date.valueOf(arrivalDate));
            if (orderStmt.executeUpdate() > 0) {

                // **Insert into `purchase_order_items`**
                itemStmt.setString(1, itemName);
                itemStmt.setInt(2, quantity);
                itemStmt.setString(3, itemName);
                itemStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
