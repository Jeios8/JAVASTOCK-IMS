package main.java.com.javastock.view;

import main.java.com.javastock.model.Inventory;
import main.java.com.javastock.viewmodel.InventoryVM;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryUI {
    private JPanel panel;
    private InventoryVM viewModel;

    public InventoryUI(InventoryVM viewModel) {
        this.viewModel = viewModel;
        panel = initializeUI();
    }

    private JPanel initializeUI() {
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Inventory ID", "Product ID", "Warehouse ID", "Quantity"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Fetch inventory data
        List<Inventory> inventoryList = viewModel.fetchInventory();
        for (Inventory item : inventoryList) {
            Object[] rowData = {
                    item.getInventoryId(),
                    item.getProductId(),
                    item.getWarehouseId(),
                    item.getQuantity()
            };
            tableModel.addRow(rowData);
        }

        inventoryPanel.add(scrollPane, BorderLayout.CENTER);
        return inventoryPanel;
    }

    public JPanel getPanel() {
        return panel;
    }
}
