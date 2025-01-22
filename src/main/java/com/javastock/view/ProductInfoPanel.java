package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.ProductVM;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProductInfoPanel extends JPanel {
    private final ProductVM productVM;
    private final JFrame parentFrame;

    // Editable fields
    private JTextField productNameField, buyingPriceField, categoryField, supplierField, quantityField, thresholdField;
    private JButton editSaveButton;

    public ProductInfoPanel(JFrame parent, int productId) {
        this.parentFrame = parent;
        this.productVM = new ProductVM(productId);

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding

        // ✅ Fetch Product Data using ProductVM
        int productIdVal = productVM.getProductId();
        String productName = productVM.getProductName();
        double buyingPrice = productVM.getBuyingPrice();
        String category = productVM.getCategory();
        String supplierName = productVM.getSupplier();
        int quantity = productVM.getQuantity();
        int threshold = productVM.getThreshold();
        int stockOnHand = productVM.getStockOnHand();
        int onTheWayStock = productVM.getOnTheWayStock();

        // ✅ Buttons Panel (Anchored to the Top Right)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editSaveButton = new JButton("Edit");
        JButton downloadButton = new JButton("Download");
        buttonPanel.add(editSaveButton);
        buttonPanel.add(downloadButton);

        // ✅ Make the Edit/Save Button Functional
        editSaveButton.addActionListener(e -> toggleEditSaveMode());

        // ✅ Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Overview", createOverviewPanel(productIdVal, productName, buyingPrice, category, supplierName, quantity, threshold, stockOnHand, onTheWayStock));
        tabbedPane.addTab("Purchases", new JPanel()); // Placeholder
        tabbedPane.addTab("Adjustments", new JPanel()); // Placeholder
        tabbedPane.addTab("History", new JPanel()); // Placeholder

        // ✅ Main Panel (Contains Tabs & Buttons)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.NORTH);
        add(createStockLocationsPanel(), BorderLayout.CENTER);
    }

    private JPanel createOverviewPanel(int productId, String productName, double buyingPrice, String category, String supplierName, int quantity, int threshold, int stockOnHand, int onTheWayStock) {
        JPanel overviewPanel = new JPanel(new BorderLayout(10, 10));

        // ✅ Left Side - Product & Supplier Details
        JPanel leftPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Primary Details"));

        leftPanel.add(new JLabel("Product ID:"));
        leftPanel.add(new JLabel(String.valueOf(productId)));

        leftPanel.add(new JLabel("Product Name:"));
        productNameField = createTextField(productName);
        leftPanel.add(productNameField);

        leftPanel.add(new JLabel("Buying Price:"));
        buyingPriceField = createTextField(String.valueOf(buyingPrice));
        leftPanel.add(buyingPriceField);

        leftPanel.add(new JLabel("Category:"));
        categoryField = createTextField(category);
        leftPanel.add(categoryField);

        leftPanel.add(new JLabel("Supplier:"));
        supplierField = createTextField(supplierName);
        leftPanel.add(supplierField);

        leftPanel.add(new JLabel("Quantity:"));
        quantityField = createTextField(String.valueOf(quantity));
        leftPanel.add(quantityField);

        overviewPanel.add(leftPanel, BorderLayout.WEST);

        // ✅ Right Side - Stock Summary & Image
        JPanel rightPanel = new JPanel(new BorderLayout());

        // ✅ Product Image
        JLabel productImageLabel = new JLabel();
        productImageLabel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        productImageLabel.setHorizontalAlignment(JLabel.CENTER);
        productImageLabel.setPreferredSize(new Dimension(150, 150));
        productImageLabel.setIcon(new ImageIcon("path/to/product-image.jpg")); // Placeholder

        // ✅ Stock Summary Panel
        JPanel stockPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        stockPanel.setBorder(BorderFactory.createTitledBorder("Stock Summary"));

        stockPanel.add(new JLabel("Stock On Hand:"));
        stockPanel.add(new JLabel(String.valueOf(stockOnHand)));

        stockPanel.add(new JLabel("On the Way:"));
        stockPanel.add(new JLabel(String.valueOf(onTheWayStock)));

        stockPanel.add(new JLabel("Threshold:"));
        thresholdField = createTextField(String.valueOf(threshold));
        stockPanel.add(thresholdField);

        rightPanel.add(productImageLabel, BorderLayout.NORTH);
        rightPanel.add(stockPanel, BorderLayout.SOUTH);

        overviewPanel.add(rightPanel, BorderLayout.EAST);

        return overviewPanel;
    }

    private JPanel createStockLocationsPanel() {
        JPanel stockLocationsPanel = new JPanel(new BorderLayout(10, 10));
        stockLocationsPanel.setBorder(BorderFactory.createTitledBorder("Stock Locations"));

        // ✅ Fetch stock location data from ProductVM
        String[] columnNames = {"Store Name", "Stock in Hand"};
        Object[][] data = productVM.getStockLocations();

        JTable stockLocationsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(stockLocationsTable);
        stockLocationsPanel.add(scrollPane, BorderLayout.CENTER);

        return stockLocationsPanel;
    }

    // ✅ Method to toggle between Edit and Save mode
    private void toggleEditSaveMode() {
        boolean isEditing = productNameField.isEditable();

        if (isEditing) {
            // ✅ Save Data
            saveProductChanges();
            editSaveButton.setText("Edit");
        } else {
            // ✅ Enable Fields for Editing
            productNameField.setEditable(true);
            buyingPriceField.setEditable(true);
//            categoryField.setEditable(true);
//            supplierField.setEditable(true);
            quantityField.setEditable(true);
            thresholdField.setEditable(true);
            editSaveButton.setText("Save");
        }
    }

    // ✅ Save Updated Product Information
    private void saveProductChanges() {
        String newName = productNameField.getText();
        double newPrice;
        int newQuantity, newThreshold;

        try {
            newPrice = Double.parseDouble(buyingPriceField.getText());
            newQuantity = Integer.parseInt(quantityField.getText());
            newThreshold = Integer.parseInt(thresholdField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = productVM.updateProduct(newName, newPrice, newQuantity, newThreshold);
        if (success) {
            JOptionPane.showMessageDialog(this, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update product.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // ✅ Disable Fields After Saving
        productNameField.setEditable(false);
        buyingPriceField.setEditable(false);
        categoryField.setEditable(false);
        supplierField.setEditable(false);
        quantityField.setEditable(false);
        thresholdField.setEditable(false);
    }

    // ✅ Helper Method to Create a Disabled JTextField
    private JTextField createTextField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        return field;
    }
}