package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.SupplierVM;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SupplierInfoPanel extends JPanel {
    private final SupplierVM supplierVM;
    private final JFrame parentFrame;
    private final int supplierId;

    private JTextField supplierNameField, contactNameField, phoneField, emailField, addressField, statusField;
    private JLabel supplierIdLabel;
    private JButton editSaveButton;
    private boolean isEditing = false;

    public SupplierInfoPanel(JFrame parent, int supplierId) {
        this.parentFrame = parent;
        this.supplierId = supplierId;
        this.supplierVM = new SupplierVM();

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
        loadSupplierDataAsync();
    }

    private void initializeFields() {
        supplierIdLabel = new JLabel("Loading...");
        supplierNameField = createEditableTextField("Loading...");
        contactNameField = createEditableTextField("Loading...");
        phoneField = createEditableTextField("Loading...");
        emailField = createEditableTextField("Loading...");
        addressField = createEditableTextField("Loading...");
        statusField = createEditableTextField("Loading...");
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Supplier ID:"), gbc);
        gbc.gridx = 1;
        panel.add(supplierIdLabel, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Supplier Name:"), gbc);
        gbc.gridx = 1;
        panel.add(supplierNameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Contact Name:"), gbc);
        gbc.gridx = 1;
        panel.add(contactNameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        panel.add(statusField, gbc);

        return panel;
    }

    private void toggleEditSaveMode() {
        isEditing = !isEditing;
        boolean editable = isEditing;

        supplierNameField.setEditable(editable);
        contactNameField.setEditable(editable);
        phoneField.setEditable(editable);
        emailField.setEditable(editable);
        addressField.setEditable(editable);

        editSaveButton.setText(isEditing ? "Save" : "Edit");
    }

    private void loadSupplierDataAsync() {
        new SwingWorker<Void, Void>() {
            private String supplierName, contactName, phone, email, address, status;

            @Override
            protected Void doInBackground() {
                Object[] supplierDetails = supplierVM.getSupplierDetailsById(supplierId);

                if (supplierDetails != null) {
                    supplierName = (String) supplierDetails[0];
                    contactName = (String) supplierDetails[1];
                    phone = (String) supplierDetails[2];
                    email = (String) supplierDetails[3];
                    address = (String) supplierDetails[4];
                    status = (Boolean) supplierDetails[5] ? "Active" : "Inactive";
                }
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    supplierIdLabel.setText(String.valueOf(supplierId));
                    supplierNameField.setText(supplierName);
                    contactNameField.setText(contactName);
                    phoneField.setText(phone);
                    emailField.setText(email);
                    addressField.setText(address);
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
}
