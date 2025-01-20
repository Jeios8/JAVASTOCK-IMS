package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.ProductVM;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProductInfoPanel extends JPanel {
    private final ProductVM productVM;

    public ProductInfoPanel(int productId) {
        this.productVM = new ProductVM(productId);

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding

        // Fetch Product Data using ProductVM
        String productName = productVM.getProductName();
        String category = productVM.getCategory();
        String supplierName = productVM.getSupplier();
        int thresholdValue = productVM.getThreshold();
        int stockOnHand = productVM.getStockOnHand();
        int onTheWayStock = productVM.getOnTheWayStock();

        // Top Section - Tabs & Buttons
        JPanel topPanel = new JPanel(new BorderLayout());

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Overview", createOverviewPanel(productName, category, supplierName, thresholdValue, stockOnHand, onTheWayStock));
        tabbedPane.addTab("Purchases", new JPanel()); // Placeholder
        tabbedPane.addTab("Adjustments", new JPanel()); // Placeholder
        tabbedPane.addTab("History", new JPanel()); // Placeholder

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton downloadButton = new JButton("Download");
        buttonPanel.add(editButton);
        buttonPanel.add(downloadButton);

        topPanel.add(tabbedPane, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(createStockLocationsPanel(), BorderLayout.CENTER);
    }

    private JPanel createOverviewPanel(String productName, String category, String supplierName, int thresholdValue, int stockOnHand, int onTheWayStock) {
        JPanel overviewPanel = new JPanel(new BorderLayout(10, 10));

        // Left Side - Product & Supplier Details
        JPanel leftPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Primary Details"));

        leftPanel.add(new JLabel("Product Name:"));
        leftPanel.add(new JLabel(productName));

        leftPanel.add(new JLabel("Category:"));
        leftPanel.add(new JLabel(category));

        leftPanel.add(new JLabel("Supplier:"));
        leftPanel.add(new JLabel(supplierName));

        leftPanel.add(new JLabel("Threshold Value:"));
        leftPanel.add(new JLabel(String.valueOf(thresholdValue)));

        overviewPanel.add(leftPanel, BorderLayout.WEST);

        // Right Side - Stock Summary & Image
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Product Image
        JLabel productImageLabel = new JLabel();
        productImageLabel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        productImageLabel.setHorizontalAlignment(JLabel.CENTER);
        productImageLabel.setPreferredSize(new Dimension(150, 150));
        productImageLabel.setIcon(new ImageIcon("path/to/product-image.jpg")); // Placeholder

        // Stock Summary Panel
        JPanel stockPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        stockPanel.setBorder(BorderFactory.createTitledBorder("Stock Summary"));

        stockPanel.add(new JLabel("Stock On Hand:"));
        stockPanel.add(new JLabel(String.valueOf(stockOnHand)));

        stockPanel.add(new JLabel("On the Way:"));
        stockPanel.add(new JLabel(String.valueOf(onTheWayStock)));

        stockPanel.add(new JLabel("Threshold Value:"));
        stockPanel.add(new JLabel(String.valueOf(thresholdValue)));

        rightPanel.add(productImageLabel, BorderLayout.NORTH);
        rightPanel.add(stockPanel, BorderLayout.SOUTH);

        overviewPanel.add(rightPanel, BorderLayout.EAST);

        return overviewPanel;
    }

    private JPanel createStockLocationsPanel() {
        JPanel stockLocationsPanel = new JPanel(new BorderLayout(10, 10));
        stockLocationsPanel.setBorder(BorderFactory.createTitledBorder("Stock Locations"));

        // Fetch stock location data from ProductVM
        String[] columnNames = {"Store Name", "Stock in Hand"};
        Object[][] data = productVM.getStockLocations();

        JTable stockLocationsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(stockLocationsTable);
        stockLocationsPanel.add(scrollPane, BorderLayout.CENTER);

        return stockLocationsPanel;
    }
}
