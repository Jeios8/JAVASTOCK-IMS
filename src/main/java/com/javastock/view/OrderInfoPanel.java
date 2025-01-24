package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.OrdersVM;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OrderInfoPanel extends JPanel {
    private final OrdersVM ordersVM;
    private final JFrame parentFrame;
    private final int orderId;

    private JTextField customerNameField, orderDateField, totalAmountField, statusField;
    private JLabel orderIdLabel;
    private JButton editSaveButton;
    private boolean isEditing = false;

    public OrderInfoPanel(JFrame parent, int orderId) {
        this.parentFrame = parent;
        this.orderId = orderId;
        this.ordersVM = new OrdersVM();

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        initializeFields();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editSaveButton = new JButton("Edit");
        buttonPanel.add(editSaveButton);

        JPanel detailsPanel = createDetailsPanel();

        add(buttonPanel, BorderLayout.NORTH);
        add(detailsPanel, BorderLayout.CENTER);

        editSaveButton.addActionListener(e -> toggleEditSaveMode());
        loadOrderDataAsync();
    }

    private void initializeFields() {
        orderIdLabel = new JLabel("Loading...");
        customerNameField = createEditableTextField("Loading...");
        orderDateField = createEditableTextField("Loading...");
        totalAmountField = createEditableTextField("Loading...");
        statusField = createEditableTextField("Loading...");
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Order ID:"), gbc);
        gbc.gridx = 1;
        panel.add(orderIdLabel, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        panel.add(customerNameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Order Date:"), gbc);
        gbc.gridx = 1;
        panel.add(orderDateField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Total Amount:"), gbc);
        gbc.gridx = 1;
        panel.add(totalAmountField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        panel.add(statusField, gbc);

        return panel;
    }

    private void toggleEditSaveMode() {
        isEditing = !isEditing;
        boolean editable = isEditing;

        customerNameField.setEditable(editable);
        orderDateField.setEditable(false); // Keep order date uneditable
        totalAmountField.setEditable(false); // Keep total uneditable
        statusField.setEditable(editable);

        editSaveButton.setText(isEditing ? "Save" : "Edit");
    }

    private void loadOrderDataAsync() {
        new SwingWorker<Void, Void>() {
            private String customerName, orderDate, totalAmount, status;

            @Override
            protected Void doInBackground() {
                boolean isSalesOrder = determineOrderType(orderId); // Add logic to determine if Sales or Purchase Order
                Object[] orderDetails = ordersVM.getOrderDetailsById(orderId, isSalesOrder);

                if (orderDetails != null) {
                    customerName = (String) orderDetails[0];
                    orderDate = (String) orderDetails[1];
                    totalAmount = "₱ " + orderDetails[2];
                    status = (String) orderDetails[3];
                }
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    orderIdLabel.setText(String.valueOf(orderId));
                    customerNameField.setText(customerName);
                    orderDateField.setText(orderDate);
                    totalAmountField.setText(totalAmount);
                    statusField.setText(status);
                });
            }
        }.execute();
    }

    private JTextField createEditableTextField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setColumns(15);
        return field;
    }

    private boolean determineOrderType(int orderId) {
        return orderId >= 1000; // Example logic: Sales orders start from 1000, purchase orders below
    }
}
