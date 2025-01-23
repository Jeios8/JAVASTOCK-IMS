package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.ProductVM;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WarehouseInfoPanel extends JPanel {
    private final ProductVM productVM;

    public WarehouseInfoPanel(int productId) {
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

        // Buttons Panel (Anchored to the Top Right)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton downloadButton = new JButton("Download");
        buttonPanel.add(editButton);
        buttonPanel.add(downloadButton);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Overview", createOverviewPanel(productName, category, supplierName, thresholdValue, stockOnHand, onTheWayStock));
        tabbedPane.addTab("Purchases", new JPanel()); // Placeholder
        tabbedPane.addTab("Adjustments", new JPanel()); // Placeholder
        tabbedPane.addTab("History", new JPanel()); // Placeholder

        // Main Panel (Contains Tabs & Buttons)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.NORTH); // 🔹 Add Buttons Above Tabs
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.NORTH);
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


















//package main.java.com.javastock.view;
//
//import main.java.com.javastock.viewmodel.WarehouseVM;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import java.awt.*;
//
//public class WarehouseInfoPanel extends JPanel {
//    private final WarehouseVM warehouseVM;
//
//    public WarehouseInfoPanel(int warehouseId) {
//        this.warehouseVM = new WarehouseVM(warehouseId);
//
//        setLayout(new BorderLayout(20, 20));
//        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding
//
//        // Fetch Warehouse Data using WarehouseVM
////        String warehouseName = warehouseVM.getWarehouseName();
////        String location = warehouseVM.getLocation();
////        String contactName = warehouseVM.getContactName();
////        String phone = warehouseVM.getPhone();
//
//        String warehouseName = "sample";
//        String location = "sample";
//        String contactName = "sample";
//        String phone = "sample";
//
//        // Buttons Panel (Anchored to the Top Right)
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton editButton = new JButton("Edit");
//        JButton downloadButton = new JButton("Download");
//        buttonPanel.add(editButton);
//        buttonPanel.add(downloadButton);
//
//        // Tabs
//        JTabbedPane tabbedPane = new JTabbedPane();
//        tabbedPane.addTab("Overview", createOverviewPanel(warehouseName, location, contactName, phone));
//        tabbedPane.addTab("History", new JPanel()); // Placeholder
//
//        // Main Panel (Contains Tabs & Buttons)
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.add(buttonPanel, BorderLayout.NORTH); // 🔹 Add Buttons Above Tabs
//        mainPanel.add(tabbedPane, BorderLayout.CENTER);
//
//        add(mainPanel, BorderLayout.NORTH);
//    }
//
//    private JPanel createOverviewPanel(String warehouseName, String location, String contactName, String phone) {
//        JPanel overviewPanel = new JPanel(new BorderLayout(10, 10));
//
//        // Left Side - Warehouse & Supplier Details
//        JPanel leftPanel = new JPanel(new GridLayout(4, 2, 5, 5));
//        leftPanel.setBorder(BorderFactory.createTitledBorder("Primary Details"));
//
//        leftPanel.add(new JLabel("Warehouse Name:"));
//        leftPanel.add(new JLabel(warehouseName));
//
//        leftPanel.add(new JLabel("Location:"));
//        leftPanel.add(new JLabel(location));
//
//        leftPanel.add(new JLabel("Contact Name:"));
//        leftPanel.add(new JLabel(contactName));
//
//        leftPanel.add(new JLabel("Phone Number:"));
//        leftPanel.add(new JLabel(phone));
//
//        overviewPanel.add(leftPanel, BorderLayout.WEST);
//
//        // Right Side - Stock Summary & Image
//        JPanel rightPanel = new JPanel(new BorderLayout());
//
//        // Warehouse Image
//        JLabel warehouseImageLabel = new JLabel();
//        warehouseImageLabel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
//        warehouseImageLabel.setHorizontalAlignment(JLabel.CENTER);
//        warehouseImageLabel.setPreferredSize(new Dimension(150, 150));
//        warehouseImageLabel.setIcon(new ImageIcon("path/to/warehouse-image.jpg")); // Placeholder
//
//        // Stock Summary Panel
//        JPanel stockPanel = new JPanel(new GridLayout(3, 2, 5, 5));
//        stockPanel.setBorder(BorderFactory.createTitledBorder("Stock Summary"));
//
//        rightPanel.add(warehouseImageLabel, BorderLayout.NORTH);
//        rightPanel.add(stockPanel, BorderLayout.SOUTH);
//
//        overviewPanel.add(rightPanel, BorderLayout.EAST);
//
//        return overviewPanel;
//    }
//}
