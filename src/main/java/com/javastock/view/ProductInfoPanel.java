package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.ProductVM;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProductInfoPanel extends JPanel {
    private final ProductVM productVM;
    private final JFrame parentFrame;
    private final int productId;

    // Editable fields
    private JTextField productNameField, buyingPriceField, categoryField, supplierField, quantityField, thresholdField;
    private JLabel productIdLabel, stockOnHandLabel, onTheWayStockLabel;
    private JButton editSaveButton;
    private boolean isEditing = false; // Toggle state for edit mode

    public ProductInfoPanel(JFrame parent, int productId) {
        this.parentFrame = parent;
        this.productId = productId;
        this.productVM = new ProductVM(productId);

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding

        // ✅ Buttons Panel (Anchored to the Top Right)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editSaveButton = new JButton("Edit");
        JButton downloadButton = new JButton("Download");
        buttonPanel.add(editSaveButton);
        buttonPanel.add(downloadButton);

        // ✅ Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Overview", createOverviewPanel());
        tabbedPane.addTab("Purchases", new JPanel()); // Placeholder
        tabbedPane.addTab("Adjustments", new JPanel()); // Placeholder
        tabbedPane.addTab("History", new JPanel()); // Placeholder

        // ✅ Main Panel (Contains Tabs & Buttons)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.NORTH);
        add(createStockLocationsPanel(), BorderLayout.CENTER);

        // ✅ Add Action Listener for Edit Button
        editSaveButton.addActionListener(e -> toggleEditSaveMode());

        // ✅ Load Data Asynchronously
        loadProductDataAsync();
    }

    private JPanel createOverviewPanel() {
        JPanel overviewPanel = new JPanel(new GridBagLayout()); // ✅ Use GridBagLayout for better alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // ✅ Spacing between elements
        gbc.fill = GridBagConstraints.BOTH;

        // ✅ Left Panel - Product & Supplier Details
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Primary Details"));

        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(5, 5, 5, 5);
        leftGbc.anchor = GridBagConstraints.WEST;
        leftGbc.gridx = 0; leftGbc.gridy = 0;

        leftPanel.add(new JLabel("Product ID:"), leftGbc);
        leftGbc.gridx = 1;
        productIdLabel = new JLabel("Loading...");
        leftPanel.add(productIdLabel, leftGbc);

        leftGbc.gridx = 0; leftGbc.gridy++;
        leftPanel.add(new JLabel("Product Name:"), leftGbc);
        leftGbc.gridx = 1;
        productNameField = createEditableTextField("Loading...");
        leftPanel.add(productNameField, leftGbc);

        leftGbc.gridx = 0; leftGbc.gridy++;
        leftPanel.add(new JLabel("Buying Price:"), leftGbc);
        leftGbc.gridx = 1;
        buyingPriceField = createEditableTextField("Loading...");
        leftPanel.add(buyingPriceField, leftGbc);

        leftGbc.gridx = 0; leftGbc.gridy++;
        leftPanel.add(new JLabel("Category:"), leftGbc);
        leftGbc.gridx = 1;
        categoryField = createEditableTextField("Loading...");
        leftPanel.add(categoryField, leftGbc);

        leftGbc.gridx = 0; leftGbc.gridy++;
        leftPanel.add(new JLabel("Supplier:"), leftGbc);
        leftGbc.gridx = 1;
        supplierField = createEditableTextField("Loading...");
        leftPanel.add(supplierField, leftGbc);

        leftGbc.gridx = 0; leftGbc.gridy++;
        leftPanel.add(new JLabel("Quantity:"), leftGbc);
        leftGbc.gridx = 1;
        quantityField = createEditableTextField("Loading...");
        leftPanel.add(quantityField, leftGbc);

        leftGbc.gridx = 0; leftGbc.gridy++;
        leftPanel.add(new JLabel("Threshold:"), leftGbc);
        leftGbc.gridx = 1;
        thresholdField = createEditableTextField("Loading...");
        leftPanel.add(thresholdField, leftGbc);

        // ✅ Right Panel - Stock Summary & Image
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Stock Summary"));

        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.insets = new Insets(5, 5, 5, 5);
        rightGbc.anchor = GridBagConstraints.CENTER;
        rightGbc.gridx = 0; rightGbc.gridy = 0;

        // ✅ Product Image
        JLabel productImageLabel = new JLabel();
        productImageLabel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        productImageLabel.setHorizontalAlignment(JLabel.CENTER);
        productImageLabel.setPreferredSize(new Dimension(150, 150));
        productImageLabel.setIcon(new ImageIcon("path/to/product-image.jpg")); // Placeholder
        rightPanel.add(productImageLabel, rightGbc);

        rightGbc.gridy++;
        rightPanel.add(new JLabel("Stock On Hand:"), rightGbc);
        rightGbc.gridx = 1;
        stockOnHandLabel = new JLabel("Loading...");
        rightPanel.add(stockOnHandLabel, rightGbc);

        rightGbc.gridx = 0; rightGbc.gridy++;
        rightPanel.add(new JLabel("On the Way:"), rightGbc);
        rightGbc.gridx = 1;
        onTheWayStockLabel = new JLabel("Loading...");
        rightPanel.add(onTheWayStockLabel, rightGbc);

        rightGbc.gridx = 0; rightGbc.gridy++;
        rightPanel.add(new JLabel("Threshold:"), rightGbc);
        rightGbc.gridx = 1;
        rightPanel.add(thresholdField, rightGbc);

        // ✅ Add Panels to Overview Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4; // ✅ 60% of width to left panel
        overviewPanel.add(leftPanel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.6; // ✅ 40% of width to right panel
        overviewPanel.add(rightPanel, gbc);

        return overviewPanel;
    }

    private JPanel createStockLocationsPanel() {
        JPanel stockLocationsPanel = new JPanel(new BorderLayout(10, 10));
        stockLocationsPanel.setBorder(BorderFactory.createTitledBorder("Stock Locations"));

        String[] columnNames = {"Store Name", "Stock in Hand"};
        Object[][] data = {{"Loading...", "Loading..."}};

        JTable stockLocationsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(stockLocationsTable);
        stockLocationsPanel.add(scrollPane, BorderLayout.CENTER);

        return stockLocationsPanel;
    }

    // **Load Product Data Asynchronously**
    public void loadProductDataAsync() {
        new SwingWorker<Void, Void>() {
            private String productName, category, supplier;
            private double buyingPrice;
            private int quantity, threshold, stockOnHand, onTheWayStock;
            private Object[][] stockLocations;

            @Override
            protected Void doInBackground() {
                // ✅ Fetch Data in Background
                productName = productVM.getProductName();
                category = productVM.getCategory();
                supplier = productVM.getSupplier();
                buyingPrice = productVM.getBuyingPrice();
                quantity = productVM.getQuantity();
                threshold = productVM.getThreshold();
                stockOnHand = productVM.getStockOnHand();
                onTheWayStock = productVM.getOnTheWayStock();
                stockLocations = productVM.getStockLocations();
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    // ✅ Update UI Fields with Data
                    productIdLabel.setText(String.valueOf(productId));
                    productNameField.setText(productName);
                    buyingPriceField.setText("" + buyingPrice);
                    categoryField.setText(category);
                    supplierField.setText(supplier);
                    quantityField.setText("" + quantity);
                    thresholdField.setText(String.valueOf(threshold));
                    stockOnHandLabel.setText(String.valueOf(stockOnHand));
                    onTheWayStockLabel.setText(String.valueOf(onTheWayStock));

                    // ✅ Refresh UI
                    revalidate();
                    repaint();
                });
            }
        }.execute();
    }

    // ✅ Toggle Edit & Save Mode
    private void toggleEditSaveMode() {
        isEditing = !isEditing;
        boolean editable = isEditing;

        // ✅ Enable/Disable Editing
        productNameField.setEditable(editable);
        buyingPriceField.setEditable(editable);
//        categoryField.setEditable(editable);
//        supplierField.setEditable(editable);
        quantityField.setEditable(editable);
        thresholdField.setEditable(editable);

        // ✅ Change Button Text
        editSaveButton.setText(isEditing ? "Save" : "Edit");

        // ✅ Save Changes on Click
        if (!isEditing) {
            saveProductChanges();
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
        JOptionPane.showMessageDialog(this, success ? "Product updated successfully!" : "Failed to update product.",
                success ? "Success" : "Error",
                success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    // ✅ Helper Method for Editable Text Fields
    private JTextField createEditableTextField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        return field;
    }
}