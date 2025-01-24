package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.DashboardVM;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class DashboardUI extends JPanel {
    private DashboardVM viewModel;
    private JPanel salesChartPanel;
    private JPanel orderChartPanel;
    private JPanel salesOverviewPanel;
    private JPanel topSellingPanel;
    private JPanel lowStockPanel;

    public DashboardUI(DashboardVM viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout());

        // 📌 Main Dashboard Panel (to be wrapped inside ScrollPane)
        JPanel dashboardContent = new JPanel(new GridLayout(3, 1, 10, 10)); // Grid Layout back in action
        dashboardContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 📌 Sales Overview Section
        salesOverviewPanel = createLoadingPanel("Sales Overview...");
        dashboardContent.add(salesOverviewPanel);

        // 📌 Sales & Purchase Charts (Same Row)
        JPanel chartsRowPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        salesChartPanel = createLoadingPanel("Sales & Purchase Trends...");
        orderChartPanel = createLoadingPanel("Order Summary...");
        chartsRowPanel.add(salesChartPanel);
        chartsRowPanel.add(orderChartPanel);
        dashboardContent.add(chartsRowPanel);

        // 📌 Top Selling & Low Stock Panels (Same Row)
        JPanel stockRowPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        topSellingPanel = createLoadingPanel("Top Selling Stocks");
        lowStockPanel = createLoadingPanel("Low Quantity Stocks");
        stockRowPanel.add(topSellingPanel);
        stockRowPanel.add(lowStockPanel);
        dashboardContent.add(stockRowPanel);

        // 📌 Wrap Dashboard Content in ScrollPane
        JScrollPane scrollPane = new JScrollPane(dashboardContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // 📌 Load Content Asynchronously
        new LoadSalesOverviewWorker().execute();
        new LoadChartsWorker().execute();
        new LoadStockDataWorker().execute(); // Load Stock Data Asynchronously
    }

    private JPanel createLoadingPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(text));
        panel.add(new JLabel("Loading...", SwingConstants.CENTER), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(400, 250)); // Ensure proper size
        return panel;
    }

    /**
     * 🛠 Loads Sales Overview Asynchronously
     */
    private class LoadSalesOverviewWorker extends SwingWorker<Map<String, Integer>, Void> {
        @Override
        protected Map<String, Integer> doInBackground() {
            return viewModel.getSalesOverview();
        }

        @Override
        protected void done() {
            try {
                Map<String, Integer> salesData = get();
                salesOverviewPanel.removeAll();
                salesOverviewPanel.add(new SalesOverviewPanel(salesData));
                salesOverviewPanel.revalidate();
                salesOverviewPanel.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 🛠 Loads Charts Asynchronously
     */
    private class LoadChartsWorker extends SwingWorker<Map<String, Map<String, Integer>>, Void> {
        @Override
        protected Map<String, Map<String, Integer>> doInBackground() {
            return Map.of(
                    "sales", viewModel.getSalesOverview(),
                    "purchase", viewModel.getPurchaseOverview()
            );
        }

        @Override
        protected void done() {
            try {
                Map<String, Map<String, Integer>> result = get();
                JPanel newSalesChartPanel = createBarChartPanel("Sales & Purchase Trends", result.get("sales"), result.get("purchase"));
                JPanel newOrderChartPanel = createLineChartPanel("Order Summary");

                replacePanel(salesChartPanel, newSalesChartPanel);
                replacePanel(orderChartPanel, newOrderChartPanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 🛠 Loads Top Selling & Low Stock Data Asynchronously
     */
    private class LoadStockDataWorker extends SwingWorker<Map<String, List<Map<String, Object>>>, Void> {
        private List<Map<String, Object>> topSelling;
        private List<Map<String, Object>> lowStock;

        @Override
        protected Map<String, List<Map<String, Object>>> doInBackground() {
            topSelling = viewModel.getTopSellingProducts();
            lowStock = viewModel.getLowStockProducts();
            return Map.of(
                    "topSelling", topSelling,
                    "lowStock", lowStock
            );
        }

        @Override
        protected void done() {
            try {
                Map<String, List<Map<String, Object>>> result = get();
                JPanel newTopSellingPanel = createStockTable("", result.get("topSelling"));
                JPanel newLowStockPanel = createStockTable("", result.get("lowStock"));

                replacePanel(topSellingPanel, newTopSellingPanel);
                replacePanel(lowStockPanel, newLowStockPanel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void replacePanel(JPanel oldPanel, JPanel newPanel) {
        oldPanel.removeAll();
        oldPanel.add(newPanel);
        oldPanel.revalidate();
        oldPanel.repaint();
    }

    private DefaultTableModel convertToTableModel(List<Map<String, Object>> stockData) {
        String[] columnNames = {"Product", "Sold", "Remaining", "Price"};
        Object[][] data = new Object[stockData.size()][4];

        for (int i = 0; i < stockData.size(); i++) {
            Map<String, Object> row = stockData.get(i);
            data[i][0] = row.get("product_name");
            data[i][1] = row.get("sold_quantity");
            data[i][2] = row.get("remaining_quantity");
            data[i][3] = "₱" + row.get("unit_price");
        }

        return new DefaultTableModel(data, columnNames);
    }
    private JPanel createBarChartPanel(String title, Map<String, Integer> salesData, Map<String, Integer> purchaseData) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (salesData != null) {
            salesData.forEach((key, value) -> dataset.addValue(value, "Sales", key));
        }

        if (purchaseData != null) {
            purchaseData.forEach((key, value) -> dataset.addValue(value, "Purchases", key));
        }

        JFreeChart barChart = ChartFactory.createBarChart(title, "Category", "Amount", dataset);
        return new ChartPanel(barChart);
    }

    private JPanel createLineChartPanel(String title) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(3000, "Ordered", "January");
        dataset.addValue(3500, "Delivered", "January");
        dataset.addValue(2500, "Ordered", "February");
        dataset.addValue(4000, "Delivered", "February");

        JFreeChart lineChart = ChartFactory.createLineChart(title, "Month", "Orders", dataset);
        return new ChartPanel(lineChart);
    }

    private JPanel createStockTable(String title, List<Map<String, Object>> stockData) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        DefaultTableModel model = convertToTableModel(stockData);

        JTable table = new JTable(model);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(600, 200));

        return panel;
    }
}
