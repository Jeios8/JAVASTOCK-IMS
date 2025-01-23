package main.java.com.javastock.view;

import main.java.com.javastock.utils.DataPreloader;
import main.java.com.javastock.viewmodel.WarehouseVM;
import javax.swing.*;
import java.awt.*;

public class AddWarehouseDialog extends JDialog {
    private JTextField nameField, locationField, contactField, phoneField;
    private JButton addButton, cancelButton;
    private WarehouseVM viewModel;

    public AddWarehouseDialog(JFrame parent, WarehouseVM viewModel) {
        super(parent, "Add Warehouse", true);
        this.viewModel = viewModel;
        setLayout(new BorderLayout());
        setSize(420, 440);
        setLocationRelativeTo(parent);
        setResizable(false);

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

        // **Create Rounded Text Fields**
        nameField = createRoundedTextField("Enter warehouse name...");
        locationField = createRoundedTextField("Enter location");
        contactField = createRoundedTextField("Enter contact name");
        phoneField = createRoundedTextField("Enter phone");

        // **Add Labels & Fields**
        mainPanel.add(new JLabel("Warehouse Name:"), gbc);
        mainPanel.add(nameField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Location:"), gbc);
        mainPanel.add(locationField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Contact Name:"), gbc);
        mainPanel.add(contactField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Phone number:"), gbc);
        mainPanel.add(phoneField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        add(mainPanel, BorderLayout.CENTER);

        // **Buttons Panel**
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        addButton = createRoundedButton("Add Warehouse", new Color(0, 123, 255)); // Blue
        cancelButton = createRoundedButton("Cancel", new Color(220, 53, 69)); // Red

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // **Button Actions**
        addButton.addActionListener(e -> addWarehouse());
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    // **Rounded JTextField with Placeholder Support**
    private JTextField createRoundedTextField(String placeholder) {
        JTextField field = new PlaceholderTextField(placeholder);
        field.setBorder(new RoundedBorder(10));
        field.setPreferredSize(new Dimension(200, 30));
        return field;
    }

    // **Custom JTextField with Placeholder**
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

    private void addWarehouse() {
        boolean success = viewModel.validateAndAddWarehouse(nameField.getText(), locationField.getText(), contactField.getText(), phoneField.getText());

        if (success) {
            JOptionPane.showMessageDialog(this, "Warehouse added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input or failed to add warehouse.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
