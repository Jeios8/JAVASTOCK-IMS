package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.InventoryVM;

import javax.swing.*;
import java.awt.*;

public class InventoryPanel extends JPanel {
    private InventoryVM inventoryVM;
    private JTable productTable;

    public InventoryPanel(InventoryVM inventoryVM) {
        this.inventoryVM = inventoryVM;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Inventory Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        productTable = new JTable();
        loadProductData();

        JScrollPane scrollPane = new JScrollPane(productTable);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadProductData() {
        productTable.setModel(inventoryVM.getProductTableModel());
    }
}
