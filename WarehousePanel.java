package main.java.com.javastock.view;

import main.java.com.javastock.utils.CSVExporter;
import main.java.com.javastock.viewmodel.WarehouseVM;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WarehousePanel extends JPanel {
    private JTable warehouseTable;
    private JButton addButton, downloadButton;
    //  private JComboBox<String> filterDropdown;
    private JProgressBar progressBar;
    private WarehouseVM viewModel;

    public WarehousePanel(WarehouseVM viewModel) {
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
        warehouseTable = new JTable(viewModel.getTableModel());
        JScrollPane scrollPane = new JScrollPane(warehouseTable);
        warehouseTable.setDefaultRenderer(Object.class, new StockStatusRenderer());
        warehouseTable.setDefaultEditor(Object.class, null);

        warehouseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = warehouseTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object warehouseIdObj = warehouseTable.getValueAt(selectedRow, 0);
                        if (warehouseIdObj instanceof Integer) {
                            int warehouseId = (Integer) warehouseIdObj;
                            openWarehouseInfoDialog(warehouseId);
                        }
                    }
                }
            }
        });

        // Buttons & Filter
        addButton = new JButton("Add Warehouse");
        downloadButton = new JButton("Export CSV");
        //    filterDropdown = new JComboBox<>(new String[]{"All", "In-stock", "Low stock", "Out of stock"});

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        //   topPanel.add(filterDropdown);
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

        addButton.addActionListener(e -> new AddWarehouseDialog((JFrame) SwingUtilities.getWindowAncestor(this), viewModel));
        downloadButton.addActionListener(e -> CSVExporter.exportToCSV(warehouseTable));
        // filterDropdown.addActionListener(e -> filterWarehouse());

        viewModel.setOnDataLoaded(() -> {
            SwingUtilities.invokeLater(() -> {
                warehouseTable.setModel(viewModel.getTableModel());
                progressBar.setVisible(false);
            });
        });

        loadWarehouseWithProgress();
        return containerPanel;
    }

    private void openWarehouseInfoDialog(int warehouseId) {
        JFrame warehouseFrame = new JFrame("Warehouse Details");
        warehouseFrame.setSize(510, 500);
        warehouseFrame.setLocationRelativeTo(null);
        warehouseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        warehouseFrame.add(new WarehouseInfoPanel(warehouseId));
        warehouseFrame.setVisible(true);
    }

//    private void filterWarehouse() {
//        String selected = (String) filterDropdown.getSelectedItem();
//        progressBar.setVisible(true);
//
//        new SwingWorker<Void, Void>() {
//            @Override
//            protected Void doInBackground() {
//                if (!"All".equals(selected)) {
//                    viewModel.filterWarehouse(selected);
//                } else {
//                    viewModel.loadWarehouseAsync();
//                }
//                return null;
//            }
//
//            @Override
//            protected void done() {
//                SwingUtilities.invokeLater(() -> {
//                    warehouseTable.setModel(viewModel.getTableModel());
//                    progressBar.setVisible(false);  // Hides progress bar after filtering completes
//                });
//            }
//        }.execute();
//    }


    void loadWarehouseWithProgress() {
        progressBar.setVisible(true);
        viewModel.loadWarehouseAsync();
    }

    private static class StockStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            //  String stockStatus = table.getValueAt(row, table.getColumnModel().getColumnIndex("Availability")).toString();

//            if (stockStatus.equalsIgnoreCase("In-stock")) {
//                cell.setBackground(new Color(144, 238, 144));
//            } else if (stockStatus.equalsIgnoreCase("Low stock")) {
//                cell.setBackground(new Color(255, 204, 102));
//            } else if (stockStatus.equalsIgnoreCase("Out of stock")) {
//                cell.setBackground(new Color(255, 102, 102));
//            } else {
//                cell.setBackground(Color.WHITE);
//            }

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