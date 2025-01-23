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
    private JButton addButton, downloadButton, refreshButton;
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
                        System.out.println("Double-click detected on row");
                        int selectedRow = inventoryTable.getSelectedRow();
                        if (selectedRow != -1) {
                            Object productIdObj = inventoryTable.getValueAt(selectedRow, 0);

                            if (productIdObj instanceof Integer) {
                                int productId = (Integer) productIdObj;

                                // ✅ Get Parent Frame
                                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(InventoryPanel.this);

                                // ✅ Open ProductInfoPanel in a JFrame
                                JFrame productFrame = new JFrame("Product Details");
                                productFrame.setSize(600, 540);
                                productFrame.setLocationRelativeTo(null);
                                productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                                ProductInfoPanel productInfoPanel = new ProductInfoPanel(parentFrame, productId);
                                productFrame.add(productInfoPanel);
                                productFrame.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "Error: Product ID is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            });

            // Buttons & Filter
            refreshButton = new JButton("🔄 Refresh");
            addButton = new JButton("📦 Add Product");
            downloadButton = new JButton("📜 Save Report");
            filterDropdown = new JComboBox<>(new String[]{"All", "In-stock", "Low stock", "Out of stock"});

            JPanel topPanel = new JPanel(new BorderLayout());

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  // Left side
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Right side

            // Add refresh button to left panel
            leftPanel.add(refreshButton);

            // Add other buttons to right panel
            rightPanel.add(filterDropdown);
            rightPanel.add(addButton);
            rightPanel.add(downloadButton);

            // Add both panels to the topPanel
            topPanel.add(leftPanel, BorderLayout.WEST);
            topPanel.add(rightPanel, BorderLayout.EAST);

            progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setStringPainted(true);
            progressBar.setString("Loading...");
            progressBar.setVisible(false);

            containerPanel.add(topPanel, BorderLayout.NORTH);
            containerPanel.add(scrollPane, BorderLayout.CENTER);
            containerPanel.add(progressBar, BorderLayout.SOUTH);

            refreshButton.addActionListener(e -> refreshInventory());
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

    private void refreshInventory() {
        progressBar.setVisible(true);
        SwingUtilities.invokeLater(this::loadInventoryWithProgress);
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

            // ✅ Fix: Check if column exists before using its value
            int availabilityColumnIndex = table.getColumnModel().getColumnIndex("Availability");
            if (availabilityColumnIndex == -1 || table.getValueAt(row, availabilityColumnIndex) == null) {
                cell.setBackground(Color.WHITE); // Default if "Availability" column is missing
                return cell;
            }

            String stockStatus = table.getValueAt(row, availabilityColumnIndex).toString();

            // ✅ Fix: Null safety & background color handling
            if ("In-stock".equalsIgnoreCase(stockStatus)) {
                cell.setBackground(new Color(212, 237, 188, 255)); // Green
            } else if ("Low stock".equalsIgnoreCase(stockStatus)) {
                cell.setBackground(new Color(255, 229, 160, 255)); // Yellow
            } else if ("Out of stock".equalsIgnoreCase(stockStatus)) {
                cell.setBackground(new Color(255, 207, 201, 255)); // Red
            } else {
                cell.setBackground(Color.WHITE); // Default color
            }

            // Preserve selection highlight
            if (isSelected) {
                cell.setBackground(table.getSelectionBackground());
                cell.setForeground(table.getSelectionForeground());
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