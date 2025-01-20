package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.WarehouseVM;

import javax.swing.*;
import java.awt.*;

public class WarehousePanel extends JPanel {
    private WarehouseVM warehouseVM;
    private JTable warehouseTable;

    public WarehousePanel(WarehouseVM warehouseVM) {
        this.warehouseVM = warehouseVM;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Store Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        warehouseTable = new JTable();
        loadWarehouseData();

        JScrollPane scrollPane = new JScrollPane(warehouseTable);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadWarehouseData() {
        warehouseTable.setModel(warehouseVM.getWarehouseTableModel());
    }
}
