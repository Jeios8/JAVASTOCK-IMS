package main.java.com.javastock.view;

import main.java.com.javastock.utils.DataPreloader;
import main.java.com.javastock.viewmodel.OrdersVM;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class CreateOrderDialog extends JDialog {
    private JComboBox<String> customerDropdown, statusDropdown;
    private JDatePickerImpl orderDatePicker;
    private JButton addButton, cancelButton;
    private OrdersVM ordersVM;

    public CreateOrderDialog(JFrame parent, OrdersVM ordersVM) {
        super(parent, "Create Order", true);
        this.ordersVM = ordersVM;
        setLayout(new BorderLayout());
        setSize(420, 350);
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

        // **Dropdowns with Rounded Borders**
        customerDropdown = createRoundedDropdown(DataPreloader.getData("customers"));
        statusDropdown = createRoundedDropdown(new String[]{"Pending", "Processed", "Shipped", "Delivered", "Cancelled"});

        // **Date Picker for Order Date**
        orderDatePicker = createDatePicker();

        // **Add Labels & Fields**
        mainPanel.add(new JLabel("Customer:"), gbc);
        mainPanel.add(customerDropdown, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Order Date:"), gbc);
        mainPanel.add(orderDatePicker, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Order Status:"), gbc);
        mainPanel.add(statusDropdown, inputGbc);

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

    // **Rounded JComboBox**
    private JComboBox<String> createRoundedDropdown(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBorder(new RoundedBorder(10));
        comboBox.setPreferredSize(new Dimension(200, 30));
        return comboBox;
    }

    // **Rounded Buttons**
    private JButton createRoundedButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(new RoundedBorder(15));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    // **Rounded Border for UI Components**
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

    // **Load dropdown values asynchronously**
    private void loadDropdownData() {
        new SwingWorker<String[], Void>() {
            @Override
            protected String[] doInBackground() {
                return ordersVM.getCustomerNames();
            }

            @Override
            protected void done() {
                try {
                    String[] data = get();
                    customerDropdown.setModel(new DefaultComboBoxModel<>(data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void createOrder() {
        java.util.Date selectedDate = (java.util.Date) orderDatePicker.getModel().getValue();
        String orderDate = selectedDate != null ? new SimpleDateFormat("yyyy-MM-dd").format(selectedDate) : "N/A";

        boolean success = ordersVM.addOrder(
                customerDropdown.getSelectedIndex() + 1001, // Adjust index to match DB ID starting at 1001
                orderDate,
                (String) statusDropdown.getSelectedItem()
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Order created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
}
