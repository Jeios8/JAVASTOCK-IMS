package main.java.com.javastock.view;

import main.java.com.javastock.utils.CSVExporter;
import main.java.com.javastock.viewmodel.InventoryVM;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InventoryPanel extends JPanel {
    private JTable inventoryTable;
    private JButton addButton, downloadButton;
    private JComboBox<String> filterDropdown;
    private JProgressBar progressBar;
    private InventoryVM viewModel;

    public InventoryPanel(InventoryVM viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout());

        // Initialize Table
        inventoryTable = new JTable(viewModel.getTableModel());
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        inventoryTable.setDefaultRenderer(Object.class, new StockStatusRenderer());
        inventoryTable.setDefaultEditor(Object.class, null);

        inventoryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = inventoryTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object productIdObj = inventoryTable.getValueAt(selectedRow, 0);
                        if (productIdObj instanceof Integer) {
                            int productId = (Integer) productIdObj;
                            openProductInfoDialog(productId);
                        }
                    }
                }
            }
        });

        // Buttons & Filter
        addButton = new JButton("Add Product");
        downloadButton = new JButton("Export CSV");
        filterDropdown = new JComboBox<>(new String[]{"All", "In-stock", "Low stock", "Out of stock"});

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(filterDropdown);
        topPanel.add(addButton);
        topPanel.add(downloadButton);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Loading...");
        progressBar.setVisible(false);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);

        addButton.addActionListener(e -> new AddProductDialog((JFrame) SwingUtilities.getWindowAncestor(this), viewModel));
        downloadButton.addActionListener(e -> CSVExporter.exportToCSV(inventoryTable));
        filterDropdown.addActionListener(e -> filterInventory());

        viewModel.setOnDataLoaded(() -> {
            SwingUtilities.invokeLater(() -> {
                inventoryTable.setModel(viewModel.getTableModel());
                progressBar.setVisible(false);
            });
        });

        loadInventoryWithProgress();
    }

    private void openProductInfoDialog(int productId) {
        JFrame productFrame = new JFrame("Product Details");
        productFrame.setSize(700, 500);
        productFrame.setLocationRelativeTo(null);
        productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productFrame.add(new ProductInfoPanel(productId));
        productFrame.setVisible(true);
    }

    private void filterInventory() {
        String selected = (String) filterDropdown.getSelectedItem();
        progressBar.setVisible(true);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (!"All".equals(selected)) {
                    viewModel.filterInventory(selected);
                } else {
                    viewModel.loadInventoryAsync();
                }
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    inventoryTable.setModel(viewModel.getTableModel());
                    progressBar.setVisible(false);  // Hides progress bar after filtering completes
                });
            }
        }.execute();
    }


    void loadInventoryWithProgress() {
        progressBar.setVisible(true);
        viewModel.loadInventoryAsync();
    }

    private static class StockStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String stockStatus = table.getValueAt(row, table.getColumnModel().getColumnIndex("Availability")).toString();

            if (stockStatus.equalsIgnoreCase("In-stock")) {
                cell.setBackground(new Color(144, 238, 144));
            } else if (stockStatus.equalsIgnoreCase("Low stock")) {
                cell.setBackground(new Color(255, 204, 102));
            } else if (stockStatus.equalsIgnoreCase("Out of stock")) {
                cell.setBackground(new Color(255, 102, 102));
            } else {
                cell.setBackground(Color.WHITE);
            }

            return cell;
        }
    }
}