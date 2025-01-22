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

        add(createPadding(0, 20), BorderLayout.NORTH);
        add(createPadding(20, 0), BorderLayout.EAST);
        add(createPadding(20, 0), BorderLayout.WEST);
        add(createPadding(0, 20), BorderLayout.SOUTH);
        add(containerPanel(), BorderLayout.CENTER);
    }
        private JPanel containerPanel(){
            JPanel containerPanel = new JPanel();
            containerPanel.setLayout(new BorderLayout());
        // Initialize Table
        inventoryTable = new JTable(viewModel.getTableModel());
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        inventoryTable.setDefaultRenderer(Object.class, new StockStatusRenderer());
        inventoryTable.setDefaultEditor(Object.class, null);

            inventoryTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // Detect double-click
                        int selectedRow = inventoryTable.getSelectedRow();
                        if (selectedRow != -1) {
                            Object productIdObj = inventoryTable.getValueAt(selectedRow, 0);

                            if (productIdObj instanceof Integer) {
                                int productId = (Integer) productIdObj;

                                // ✅ Get Parent Frame
                                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(InventoryPanel.this);

                                // ✅ Open ProductInfoPanel in a JFrame
                                JFrame productFrame = new JFrame("Product Details");
                                productFrame.setSize(515, 515);
                                productFrame.setLocationRelativeTo(null);
                                productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                ProductInfoPanel productInfoPanel = new ProductInfoPanel(parentFrame, productId);
                                productFrame.add(productInfoPanel);
                                productFrame.setVisible(true);

                                // ✅ Load product data asynchronously (No Arguments Needed Now)
                                productInfoPanel.loadProductDataAsync();  // ✅ No argument needed anymore
                            } else {
                                JOptionPane.showMessageDialog(null, "Error: Product ID is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
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

        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        containerPanel.add(progressBar, BorderLayout.SOUTH);

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
        return containerPanel;
    }

    private void openProductInfoDialog(int productId) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this); // ✅ Get parent frame

        JFrame productFrame = new JFrame("Product Details");
        productFrame.setSize(510, 500);
        productFrame.setLocationRelativeTo(null);
        productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ✅ Pass parentFrame and productId to ProductInfoPanel
        productFrame.add(new ProductInfoPanel(parentFrame, productId));

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
    private JPanel createPadding(int width, int height) {
        JPanel padding = new JPanel();
        padding.setBackground(Color.LIGHT_GRAY);
        padding.setPreferredSize(new Dimension(width, height));
        return padding;
    }

}