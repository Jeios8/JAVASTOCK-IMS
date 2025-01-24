package main.java.com.javastock.view;

import main.java.com.javastock.utils.CSVExporter;
import main.java.com.javastock.viewmodel.OrdersVM;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OrdersPanel extends JPanel {
    private JTable salesOrdersTable, purchaseOrdersTable;
    private JButton refreshButton, downloadButton;
    private JProgressBar progressBar;
    private OrdersVM viewModel;
    private static final Color BUTTON_COLOR = new Color(153, 204, 255);

    public OrdersPanel(OrdersVM viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout());

        add(createPadding(0, 10), BorderLayout.NORTH);
        add(createPadding(10, 0), BorderLayout.EAST);
        add(createPadding(10, 0), BorderLayout.WEST);
        add(createPadding(0, 10), BorderLayout.SOUTH);
        add(containerPanel(), BorderLayout.CENTER);
    }

    private JPanel containerPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());

        // **Customer Orders Table**
        JLabel customerOrdersLabel = new JLabel("📦 Customer Orders");
        customerOrdersLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        customerOrdersLabel.setHorizontalAlignment(SwingConstants.CENTER);

        salesOrdersTable = new JTable(viewModel.getSalesOrdersTableModel());
        JScrollPane salesScrollPane = new JScrollPane(salesOrdersTable);
        salesOrdersTable.setDefaultRenderer(Object.class, new OrderStatusRenderer());
        salesOrdersTable.setDefaultEditor(Object.class, null);
        addOrderClickListener(salesOrdersTable, "Sales Order");

        JPanel customerPanel = new JPanel(new BorderLayout());
        customerPanel.add(customerOrdersLabel, BorderLayout.NORTH);
        customerPanel.add(salesScrollPane, BorderLayout.CENTER);

        // **Supplier Orders Table**
        JLabel supplierOrdersLabel = new JLabel("🚚 Supplier Orders");
        supplierOrdersLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        supplierOrdersLabel.setHorizontalAlignment(SwingConstants.CENTER);

        purchaseOrdersTable = new JTable(viewModel.getPurchaseOrdersTableModel());
        JScrollPane purchaseScrollPane = new JScrollPane(purchaseOrdersTable);
        purchaseOrdersTable.setDefaultRenderer(Object.class, new OrderStatusRenderer());
        purchaseOrdersTable.setDefaultEditor(Object.class, null);
        addOrderClickListener(purchaseOrdersTable, "Purchase Order");

        JPanel supplierPanel = new JPanel(new BorderLayout());
        supplierPanel.add(supplierOrdersLabel, BorderLayout.NORTH);
        supplierPanel.add(purchaseScrollPane, BorderLayout.CENTER);

        // **JSplitPane for Tables**
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, customerPanel, supplierPanel);
        splitPane.setDividerLocation(300); // Adjust height allocation
        splitPane.setResizeWeight(0.5); // Divide evenly between both tables

        // **Buttons**
        refreshButton = createButton("🔄 Refresh", BUTTON_COLOR);
        downloadButton = createButton("📜 Save Report", BUTTON_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        leftPanel.add(refreshButton);
        rightPanel.add(downloadButton);
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Loading...");
        progressBar.setVisible(false);

        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(splitPane, BorderLayout.CENTER);
        containerPanel.add(progressBar, BorderLayout.SOUTH);

        // **Button Actions**
        refreshButton.addActionListener(e -> refreshOrders());
        downloadButton.addActionListener(e -> CSVExporter.exportToCSV(salesOrdersTable));

        viewModel.setOnDataLoaded(() -> {
            SwingUtilities.invokeLater(() -> {
                salesOrdersTable.setModel(viewModel.getSalesOrdersTableModel());
                purchaseOrdersTable.setModel(viewModel.getPurchaseOrdersTableModel());
                salesOrdersTable.setAutoCreateRowSorter(true);
                purchaseOrdersTable.setAutoCreateRowSorter(true);
                progressBar.setVisible(false);
            });
        });

        loadOrdersWithProgress();
        return containerPanel;
    }

    private void refreshOrders() {
        progressBar.setVisible(true);
        SwingUtilities.invokeLater(this::loadOrdersWithProgress);
    }

    private void loadOrdersWithProgress() {
        progressBar.setVisible(true);
        viewModel.loadOrdersAsync();
    }

    private void addOrderClickListener(JTable table, String orderType) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double-click
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        Object orderIdObj = table.getValueAt(selectedRow, 0);
                        if (orderIdObj instanceof Integer) {
                            int orderId = (Integer) orderIdObj;

                            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(OrdersPanel.this);
                            JFrame orderFrame = new JFrame(orderType + " Details");
                            orderFrame.setSize(380, 450);
                            orderFrame.setLocationRelativeTo(null);
                            orderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                            OrderInfoPanel orderInfoPanel = new OrderInfoPanel(parentFrame, orderId);
                            orderFrame.add(orderInfoPanel);
                            orderFrame.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: Order ID is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private static class OrderStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            int statusColumnIndex = table.getColumnModel().getColumnIndex("Status");
            if (statusColumnIndex == -1 || table.getValueAt(row, statusColumnIndex) == null) {
                cell.setBackground(Color.WHITE);
                return cell;
            }

            String orderStatus = table.getValueAt(row, statusColumnIndex).toString();

            if ("Delivered".equalsIgnoreCase(orderStatus)) {
                cell.setBackground(new Color(212, 237, 188, 255)); // Green
            } else if ("Pending".equalsIgnoreCase(orderStatus)) {
                cell.setBackground(new Color(255, 244, 184, 255)); // Yellow
            } else if ("Cancelled".equalsIgnoreCase(orderStatus)) {
                cell.setBackground(new Color(255, 207, 201, 255)); // Red
            } else {
                cell.setBackground(Color.WHITE);
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
