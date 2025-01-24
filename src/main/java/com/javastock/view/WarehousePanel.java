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
    private JButton addButton, downloadButton, refreshButton;
    private JProgressBar progressBar;
    private WarehouseVM viewModel;
    private static final Color BUTTON_COLOR = new Color(153, 204, 255);

    public WarehousePanel(WarehouseVM viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout());

        add(createPadding(0, 10), BorderLayout.NORTH);
        add(createPadding(10, 0), BorderLayout.EAST);
        add(createPadding(10, 0), BorderLayout.WEST);
        add(createPadding(0, 10), BorderLayout.SOUTH);
        add(containerPanel(), BorderLayout.CENTER);
    }

    private JPanel containerPanel(){
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());

        // Initialize Table
        warehouseTable = new JTable(viewModel.getTableModel());
        JScrollPane scrollPane = new JScrollPane(warehouseTable);
        warehouseTable.setDefaultRenderer(Object.class, new WarehouseStatusRenderer());
        warehouseTable.setDefaultEditor(Object.class, null);

        warehouseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double-click
                    int selectedRow = warehouseTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object warehouseIdObj = warehouseTable.getValueAt(selectedRow, 0);
                        if (warehouseIdObj instanceof Integer) {
                            int warehouseId = (Integer) warehouseIdObj;

                            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(WarehousePanel.this);
                            JFrame warehouseFrame = new JFrame("Warehouse Details");
                            warehouseFrame.setSize(380, 450);
                            warehouseFrame.setLocationRelativeTo(null);
                            warehouseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                            WarehouseInfoPanel warehouseInfoPanel = new WarehouseInfoPanel(parentFrame, warehouseId);
                            warehouseFrame.add(warehouseInfoPanel);
                            warehouseFrame.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: Warehouse ID is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Buttons
        refreshButton = createButton("Refresh", BUTTON_COLOR);
        addButton = createButton("➕ Add Warehouse", BUTTON_COLOR);
        downloadButton = createButton("📜 Save Report", BUTTON_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        leftPanel.add(refreshButton);
        rightPanel.add(addButton);
        rightPanel.add(downloadButton);

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

        refreshButton.addActionListener(e -> refreshWarehouses());
        addButton.addActionListener(e -> new AddWarehouseDialog((JFrame) SwingUtilities.getWindowAncestor(this), viewModel));
        downloadButton.addActionListener(e -> CSVExporter.exportToCSV(warehouseTable));

        viewModel.setOnDataLoaded(() -> {
            SwingUtilities.invokeLater(() -> {
                warehouseTable.setModel(viewModel.getTableModel());
                warehouseTable.setAutoCreateRowSorter(true);
                progressBar.setVisible(false);
            });
        });

        loadWarehousesWithProgress();
        return containerPanel;
    }

    private void refreshWarehouses() {
        progressBar.setVisible(true);
        SwingUtilities.invokeLater(this::loadWarehousesWithProgress);
    }

    private void loadWarehousesWithProgress() {
        progressBar.setVisible(true);
        viewModel.loadWarehousesAsync();
    }

    private static class WarehouseStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            int statusColumnIndex = table.getColumnModel().getColumnIndex("Status");
            if (statusColumnIndex == -1 || table.getValueAt(row, statusColumnIndex) == null) {
                cell.setBackground(Color.WHITE);
                return cell;
            }

            String warehouseStatus = table.getValueAt(row, statusColumnIndex).toString();

            if ("Active".equalsIgnoreCase(warehouseStatus)) {
                cell.setBackground(new Color(212, 237, 188, 255)); // Green
            } else {
                cell.setBackground(new Color(255, 207, 201, 255)); // Red
            }

            cell.setForeground(Color.black);
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

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Helvetica", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.DARK_GRAY);
        return button;
    }
}
