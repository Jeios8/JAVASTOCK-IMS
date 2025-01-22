package main.java.com.javastock.view;

import main.java.com.javastock.utils.DataPreloader;
import main.java.com.javastock.viewmodel.InventoryVM;
import javax.swing.*;
import java.awt.*;

public class AddProductDialog extends JDialog {
    private JTextField nameField, unitPriceField, quantityField, reorderLevelField;
    private JComboBox<String> categoryDropdown, supplierDropdown, warehouseDropdown;
    private JButton addButton, cancelButton;
    private InventoryVM viewModel;

    public AddProductDialog(JFrame parent, InventoryVM viewModel) {
        super(parent, "Add Product", true);
        this.viewModel = viewModel;
        setLayout(new BorderLayout());
        setSize(420, 600);
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
        nameField = createRoundedTextField("Enter product name...");
        unitPriceField = createRoundedTextField("Enter unit price...");
        quantityField = createRoundedTextField("Enter quantity...");
        reorderLevelField = createRoundedTextField("Enter reorder level...");

        // **Dropdowns with Rounded Borders**
        categoryDropdown = createRoundedDropdown(DataPreloader.getData("categories"));
        supplierDropdown = createRoundedDropdown(DataPreloader.getData("suppliers"));
        warehouseDropdown = createRoundedDropdown(DataPreloader.getData("warehouses"));

        // **Add Labels & Fields**
        mainPanel.add(new JLabel("Product Name:"), gbc);
        mainPanel.add(nameField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Unit Price:"), gbc);
        mainPanel.add(unitPriceField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Quantity:"), gbc);
        mainPanel.add(quantityField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Reorder Level:"), gbc);
        mainPanel.add(reorderLevelField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Category:"), gbc);
        mainPanel.add(categoryDropdown, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Supplier:"), gbc);
        mainPanel.add(supplierDropdown, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Warehouse:"), gbc);
        mainPanel.add(warehouseDropdown, inputGbc);

        add(mainPanel, BorderLayout.CENTER);

        // **Buttons Panel**
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        addButton = createRoundedButton("Add Product", new Color(0, 123, 255)); // Blue
        cancelButton = createRoundedButton("Cancel", new Color(220, 53, 69)); // Red

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // **Button Actions**
        addButton.addActionListener(e -> addProduct());
        cancelButton.addActionListener(e -> dispose());

        // **Load dropdown data asynchronously**
        loadDropdownData();

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
        new SwingWorker<String[][], Void>() {
            @Override
            protected String[][] doInBackground() {
                String[] categories = viewModel.getCategories();
                String[] suppliers = viewModel.getSuppliers();
                String[] warehouses = viewModel.getWarehouses();
                return new String[][]{categories, suppliers, warehouses};
            }

            @Override
            protected void done() {
                try {
                    String[][] data = get();
                    categoryDropdown.setModel(new DefaultComboBoxModel<>(data[0]));
                    supplierDropdown.setModel(new DefaultComboBoxModel<>(data[1]));
                    warehouseDropdown.setModel(new DefaultComboBoxModel<>(data[2]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void addProduct() {
        boolean success = viewModel.validateAndAddProduct(
                nameField.getText(), unitPriceField.getText(), quantityField.getText(), reorderLevelField.getText(),
                (String) categoryDropdown.getSelectedItem(), (String) supplierDropdown.getSelectedItem(),
                (String) warehouseDropdown.getSelectedItem()
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input or failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
