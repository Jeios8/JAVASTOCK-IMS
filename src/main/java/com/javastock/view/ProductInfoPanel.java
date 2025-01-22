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
    private JTextField productNameField, buyingPriceField, categoryField, supplierField, quantityField, thresholdField, itemStatusField;
    private JLabel productIdLabel;
    private JButton editSaveButton;
    private boolean isEditing = false; // Toggle state for edit mode

    public ProductInfoPanel(JFrame parent, int productId) {
        this.parentFrame = parent;
        this.productId = productId;
        this.productVM = new ProductVM(productId);

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding

        // ✅ Initialize Fields
        initializeFields();

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

    // ✅ Initialize UI Fields
    private void initializeFields() {
        productIdLabel = new JLabel("Loading...");
        productNameField = createEditableTextField("Loading...");
        buyingPriceField = createEditableTextField("Loading...");
        categoryField = createEditableTextField("Loading...");
        supplierField = createEditableTextField("Loading...");
        quantityField = createEditableTextField("Loading...");
        thresholdField = createEditableTextField("Loading...");
        itemStatusField = createEditableTextField("Loading...");
    }

    private JPanel createOverviewPanel() {
        JPanel overviewPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // ✅ Left Panel - Product & Stock Summary Details
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Primary Details"));

        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(5, 5, 5, 5);
        leftGbc.anchor = GridBagConstraints.WEST;
        leftGbc.gridx = 0;
        leftGbc.gridy = 0;

        // ✅ Product Information
        leftPanel.add(new JLabel("Product ID:"), leftGbc);
        leftGbc.gridx = 1;
        productIdLabel = new JLabel("Loading...");
        leftPanel.add(productIdLabel, leftGbc);

        leftGbc.gridx = 0;
        leftGbc.gridy++;
        leftPanel.add(new JLabel("Product Name:"), leftGbc);
        leftGbc.gridx = 1;
        leftPanel.add(productNameField, leftGbc);

        leftGbc.gridx = 0;
        leftGbc.gridy++;
        leftPanel.add(new JLabel("Buying Price:"), leftGbc);
        leftGbc.gridx = 1;
        leftPanel.add(buyingPriceField, leftGbc);

        leftGbc.gridx = 0;
        leftGbc.gridy++;
        leftPanel.add(new JLabel("Category:"), leftGbc);
        leftGbc.gridx = 1;
        leftPanel.add(categoryField, leftGbc);

        leftGbc.gridx = 0;
        leftGbc.gridy++;
        leftPanel.add(new JLabel("Supplier:"), leftGbc);
        leftGbc.gridx = 1;
        leftPanel.add(supplierField, leftGbc);

        leftGbc.gridx = 0;
        leftGbc.gridy++;
        leftPanel.add(new JLabel("Quantity:"), leftGbc);
        leftGbc.gridx = 1;
        leftPanel.add(quantityField, leftGbc);

//        leftGbc.gridx = 0;
//        leftGbc.gridy++;
//        leftPanel.add(new JLabel("Stock On Hand:"), leftGbc);
//        leftGbc.gridx = 1;
//        stockOnHandLabel = new JLabel("Loading...");
//        leftPanel.add(stockOnHandLabel, leftGbc);
//
//        leftGbc.gridx = 0;
//        leftGbc.gridy++;
//        leftPanel.add(new JLabel("On the Way:"), leftGbc);
//        leftGbc.gridx = 1;
//        onTheWayStockLabel = new JLabel("Loading...");
//        leftPanel.add(onTheWayStockLabel, leftGbc);

        leftGbc.gridx = 0;
        leftGbc.gridy++;
        leftPanel.add(new JLabel("Re-order Level:"), leftGbc);
        leftGbc.gridx = 1;
        leftPanel.add(thresholdField, leftGbc);

        leftGbc.gridx = 0;
        leftGbc.gridy++;
        leftPanel.add(new JLabel("Status:"), leftGbc);
        leftGbc.gridx = 1;
        leftPanel.add(itemStatusField, leftGbc);

        // ✅ Right Panel - Product Image Only
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Product Image"));

        JLabel productImageLabel = new JLabel();
        productImageLabel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        productImageLabel.setHorizontalAlignment(JLabel.CENTER);
        productImageLabel.setPreferredSize(new Dimension(150, 150));

        // ✅ Load and Scale Image to Fit with Aspect Ratio
        String imagePath = "src\\main\\resources\\img\\no-image-available.jpg"; // Replace with actual path
        ImageIcon originalIcon = new ImageIcon(imagePath);

        if (originalIcon.getIconWidth() > 0 && originalIcon.getIconHeight() > 0) {
            Image scaledImage = getScaledImage(originalIcon.getImage(), 150, 150);
            productImageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            productImageLabel.setText("No Image");
            productImageLabel.setHorizontalAlignment(JLabel.CENTER);
        }

        // ✅ Add Image Label to Panel
        rightPanel.add(productImageLabel, BorderLayout.CENTER);

//        JLabel productImageLabel = new JLabel();
//        productImageLabel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
//        productImageLabel.setHorizontalAlignment(JLabel.CENTER);
//        productImageLabel.setPreferredSize(new Dimension(150, 150));
//        productImageLabel.setIcon(new ImageIcon("src\\main\\resources\\img\\no-image-available.jpg")); // Placeholder
//        rightPanel.add(productImageLabel, BorderLayout.CENTER);

        // ✅ Add Left and Right Panels to Overview Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4; // Left Panel takes 70% width
        overviewPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6; // Right Panel takes 30% width
        overviewPanel.add(rightPanel, gbc);

        return overviewPanel;
    }


    private JPanel createStockLocationsPanel() {
        JPanel stockLocationsPanel = new JPanel(new BorderLayout(10, 10));
        stockLocationsPanel.setBorder(BorderFactory.createTitledBorder("Stock Locations"));

        String[] columnNames = {"Store Name", "Stock in Hand"};
        Object[][] data = {{"Loading...","Loading..."}};

        JTable stockLocationsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(stockLocationsTable);
        stockLocationsPanel.add(scrollPane, BorderLayout.CENTER);

        return stockLocationsPanel;
    }

    // ✅ Toggle Edit & Save Mode
    private void toggleEditSaveMode() {
        isEditing = !isEditing;
        boolean editable = isEditing;

        productNameField.setEditable(editable);
        buyingPriceField.setEditable(editable);
        quantityField.setEditable(editable);
        thresholdField.setEditable(editable);

        editSaveButton.setText(isEditing ? "Save" : "Edit");

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

    // ✅ Load Product Data Asynchronously
    void loadProductDataAsync() {
        new SwingWorker<Void, Void>() {
            private String productName, category, supplier;
            private double buyingPrice;
            private int quantity, threshold;
            private boolean itemStatus;

            @Override
            protected Void doInBackground() {
                productName = productVM.getProductName();
                category = productVM.getCategory();
                supplier = productVM.getSupplier();
                buyingPrice = productVM.getBuyingPrice();
                quantity = productVM.getQuantity();
                threshold = productVM.getThreshold();
                itemStatus = productVM.getItemStatus();
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    productIdLabel.setText(String.valueOf(productId));
                    productNameField.setText(productName);
                    buyingPriceField.setText("" + buyingPrice);
                    categoryField.setText(category);
                    supplierField.setText(supplier);
                    quantityField.setText("" + quantity);
                    thresholdField.setText(String.valueOf(threshold));
                    itemStatusField.setText(String.valueOf(itemStatus));
                });
            }
        }.execute();
    }

    // ✅ Helper Method for Editable Text Fields
    private JTextField createEditableTextField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setColumns(15); // Ensures consistent width
        return field;
    }
    private Image getScaledImage(Image srcImg, int maxWidth, int maxHeight) {
        int originalWidth = srcImg.getWidth(null);
        int originalHeight = srcImg.getHeight(null);

        // Compute aspect ratio
        double aspectRatio = (double) originalWidth / originalHeight;

        int newWidth = maxWidth;
        int newHeight = (int) (maxWidth / aspectRatio);

        // If height is too big, adjust width instead
        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (int) (maxHeight * aspectRatio);
        }

        return srcImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }
}
