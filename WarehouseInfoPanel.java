package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.WarehouseVM;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WarehouseInfoPanel extends JPanel {
    private final WarehouseVM warehouseVM;
    private final JFrame parentFrame;
    private final int warehouseId;

    private JTextField warehouseNameField, contactNameField, phoneField, locationField, statusField;
    private JLabel warehouseIdLabel;
    private JButton editSaveButton;
    private boolean isEditing = false;

    public WarehouseInfoPanel(JFrame parent, int warehouseId) {
        this.parentFrame = parent;
        this.warehouseId = warehouseId;
        this.warehouseVM = new WarehouseVM();

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
        loadWarehouseDataAsync();
    }

    private void initializeFields() {
        warehouseIdLabel = new JLabel("Loading...");
        warehouseNameField = createEditableTextField("Loading...");
        contactNameField = createEditableTextField("Loading...");
        phoneField = createEditableTextField("Loading...");
      //  emailField = createEditableTextField("Loading...");
        locationField = createEditableTextField("Loading...");
        statusField = createEditableTextField("Loading...");
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Warehouse ID:"), gbc);
        gbc.gridx = 1;
        panel.add(warehouseIdLabel, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Warehouse Name:"), gbc);
        gbc.gridx = 1;
        panel.add(warehouseNameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Contact Name:"), gbc);
        gbc.gridx = 1;
        panel.add(contactNameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

//        gbc.gridx = 0; gbc.gridy++;
//        panel.add(new JLabel("Email:"), gbc);
//        gbc.gridx = 1;
//        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        panel.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        panel.add(statusField, gbc);

        return panel;
    }

    private void toggleEditSaveMode() {
        isEditing = !isEditing;
        boolean editable = isEditing;

        warehouseNameField.setEditable(editable);
        contactNameField.setEditable(editable);
        phoneField.setEditable(editable);
     //   emailField.setEditable(editable);
        locationField.setEditable(editable);

        editSaveButton.setText(isEditing ? "Save" : "Edit");
    }

    private void loadWarehouseDataAsync() {
        new SwingWorker<Void, Void>() {
            private String warehouseName, contactName, phone, location, status;

            @Override
            protected Void doInBackground() {
                Object[] warehouseDetails = warehouseVM.getWarehouseDetailsById(warehouseId);

                if (warehouseDetails != null) {
                    warehouseName = (String) warehouseDetails[0];
                    contactName = (String) warehouseDetails[1];
                    phone = (String) warehouseDetails[2];
                //    email = (String) warehouseDetails[3];
                    location = (String) warehouseDetails[4];
                    status = (Boolean) warehouseDetails[5] ? "Active" : "Inactive";
                }
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    warehouseIdLabel.setText(String.valueOf(warehouseId));
                    warehouseNameField.setText(warehouseName);
                    contactNameField.setText(contactName);
                    phoneField.setText(phone);
               //     emailField.setText(email);
                    locationField.setText(location);
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
