package main.java.com.javastock.view;

import main.java.com.javastock.utils.CSVExporter;
import main.java.com.javastock.viewmodel.SupplierVM;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SupplierPanel extends JPanel {
    private JTable supplierTable;
    private JButton addButton, downloadButton, refreshButton;
    private JProgressBar progressBar;
    private SupplierVM viewModel;
    private static final Color BUTTON_COLOR = new Color(153, 204, 255);

    public SupplierPanel(SupplierVM viewModel) {
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
        supplierTable = new JTable(viewModel.getTableModel());
        JScrollPane scrollPane = new JScrollPane(supplierTable);
        supplierTable.setDefaultRenderer(Object.class, new SupplierStatusRenderer());
        supplierTable.setDefaultEditor(Object.class, null);

        supplierTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double-click
                    int selectedRow = supplierTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object supplierIdObj = supplierTable.getValueAt(selectedRow, 0);
                        if (supplierIdObj instanceof Integer) {
                            int supplierId = (Integer) supplierIdObj;

                            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(SupplierPanel.this);
                            JFrame supplierFrame = new JFrame("Supplier Details");
                            supplierFrame.setSize(380, 450);
                            supplierFrame.setLocationRelativeTo(null);
                            supplierFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                            SupplierInfoPanel supplierInfoPanel = new SupplierInfoPanel(parentFrame, supplierId);
                            supplierFrame.add(supplierInfoPanel);
                            supplierFrame.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: Supplier ID is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Buttons
        refreshButton = createButton("Refresh", BUTTON_COLOR);
        addButton = createButton("➕ Add Supplier", BUTTON_COLOR);
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

        refreshButton.addActionListener(e -> refreshSuppliers());
        addButton.addActionListener(e -> new AddSupplierDialog((JFrame) SwingUtilities.getWindowAncestor(this), viewModel));
        downloadButton.addActionListener(e -> CSVExporter.exportToCSV(supplierTable));

        viewModel.setOnDataLoaded(() -> {
            SwingUtilities.invokeLater(() -> {
                supplierTable.setModel(viewModel.getTableModel());
                supplierTable.setAutoCreateRowSorter(true);
                progressBar.setVisible(false);
            });
        });

        loadSuppliersWithProgress();
        return containerPanel;
    }

    private void refreshSuppliers() {
        progressBar.setVisible(true);
        SwingUtilities.invokeLater(this::loadSuppliersWithProgress);
    }

    private void loadSuppliersWithProgress() {
        progressBar.setVisible(true);
        viewModel.loadSuppliersAsync();
    }

    private static class SupplierStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            int statusColumnIndex = table.getColumnModel().getColumnIndex("Status");
            if (statusColumnIndex == -1 || table.getValueAt(row, statusColumnIndex) == null) {
                cell.setBackground(Color.WHITE);
                return cell;
            }

            String supplierStatus = table.getValueAt(row, statusColumnIndex).toString();

            if ("Active".equalsIgnoreCase(supplierStatus)) {
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
