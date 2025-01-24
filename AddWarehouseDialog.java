package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.WarehouseVM;
import javax.swing.*;
import java.awt.*;

public class AddWarehouseDialog extends JDialog {
    private JTextField nameField, contactField, phoneField, locationField;
    private JButton addButton, cancelButton;
    private WarehouseVM viewModel;

    public AddWarehouseDialog(JFrame parent, WarehouseVM viewModel) {
        super(parent, "Add Warehouse", true);
        this.viewModel = viewModel;
        setLayout(new BorderLayout());
        setSize(420, 320);
        setLocationRelativeTo(parent);
        setResizable(false);

        ImageIcon img = new ImageIcon("src/main/resources/icons/icon_app.png");
        setIconImage(img.getImage());

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

        nameField = createRoundedTextField("Enter warehouse name...");
        contactField = createRoundedTextField("Enter contact name...");
        phoneField = createRoundedTextField("Enter phone number...");
    //    emailField = createRoundedTextField("Enter email...");
        locationField = createRoundedTextField("Enter location...");

        mainPanel.add(new JLabel("Warehouse Name:"), gbc);
        mainPanel.add(nameField, inputGbc);
        gbc.gridy++; inputGbc.gridy++;

        mainPanel.add(new JLabel("Contact Name:"), gbc);
        mainPanel.add(contactField, inputGbc);
        gbc.gridy++; inputGbc.gridy++;

        mainPanel.add(new JLabel("Phone:"), gbc);
        mainPanel.add(phoneField, inputGbc);
        gbc.gridy++; inputGbc.gridy++;

//        mainPanel.add(new JLabel("Email:"), gbc);
//        mainPanel.add(emailField, inputGbc);
//        gbc.gridy++; inputGbc.gridy++;

        mainPanel.add(new JLabel("Location:"), gbc);
        mainPanel.add(locationField, inputGbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        addButton = createRoundedButton("Add Warehouse", new Color(0, 123, 255));
        cancelButton = createRoundedButton("Cancel", new Color(220, 53, 69));

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addWarehouse());
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private JTextField createRoundedTextField(String placeholder) {
        JTextField field = new PlaceholderTextField(placeholder);
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        field.setPreferredSize(new Dimension(200, 30));
        return field;
    }

    class PlaceholderTextField extends JTextField {
        private String placeholder;
        private boolean showingPlaceholder = true;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setText(placeholder);
            setForeground(Color.GRAY);

            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (showingPlaceholder) {
                        setText("");
                        setForeground(Color.BLACK);
                        showingPlaceholder = false;
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
                        showingPlaceholder = true;
                    }
                }
            });
        }

        @Override
        public String getText() {
            return showingPlaceholder ? "" : super.getText();
        }
    }

    private JButton createRoundedButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    private void addWarehouse() {
        boolean success = viewModel.addWarehouse(
                nameField.getText(),
                contactField.getText(),
                phoneField.getText(),
                locationField.getText()
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Warehouse added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input or failed to add warehouse.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}