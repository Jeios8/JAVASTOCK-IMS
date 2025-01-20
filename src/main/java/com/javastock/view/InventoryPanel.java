package main.java.com.javastock.view;

import main.java.com.javastock.utils.CSVExporter;
import main.java.com.javastock.viewmodel.InventoryVM;

import javax.swing.*;
import java.awt.*;

public class InventoryPanel extends JPanel {
    private JTable inventoryTable;
    private JButton addButton, filterButton, downloadButton;
    private JComboBox<String> filterDropdown;
    private JProgressBar progressBar; // Progress bar for loading indicator
    private InventoryVM viewModel;

    public InventoryPanel(InventoryVM viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout());

        // Initialize Table
        inventoryTable = new JTable(viewModel.getTableModel());
        JScrollPane scrollPane = new JScrollPane(inventoryTable);

        // Buttons
        addButton = new JButton("Add Product");
        filterButton = new JButton("Filters");
        downloadButton = new JButton("Export CSV");

        // Filter Dropdown
        String[] filterOptions = {"All", "In-stock", "Low stock", "Out of stock"};
        filterDropdown = new JComboBox<>(filterOptions);

        // **Progress Bar**
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Continuous animation
        progressBar.setStringPainted(true); // Show loading text
        progressBar.setString("Loading..."); // Default text
        progressBar.setVisible(false); // Hidden by default

        // Top Panel Layout
        JPanel topPanel = new JPanel();
        topPanel.add(addButton);
        topPanel.add(filterDropdown);
        topPanel.add(downloadButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH); // Add progress bar at the bottom

        // **Filter Listener**
        filterDropdown.addActionListener(e -> {
            String selected = (String) filterDropdown.getSelectedItem();
            if (!selected.equals("All")) {
                loadFilteredInventoryWithProgress(selected);
            } else {
                loadInventoryWithProgress();
            }
        });

        // **CSV Export**
        downloadButton.addActionListener(e -> {
            CSVExporter.exportToCSV(inventoryTable);
        });
    }

    public InventoryVM getViewModel() {
        return viewModel;
    }

    // **Load inventory with progress indicator**
    public void loadInventoryWithProgress() {
        SwingUtilities.invokeLater(() -> {
            progressBar.setVisible(true); // Show progress bar before task starts
            progressBar.setString("Loading Inventory...");
            revalidate();
            repaint();
        });

        new SwingWorker<Void, Void>() {
            long startTime; // Track when loading starts

            @Override
            protected Void doInBackground() {
                startTime = System.currentTimeMillis(); // Record start time

                viewModel.loadInventoryAsync();

                // Ensure a minimum display time for the progress bar
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTime = 1000 - elapsedTime; // Minimum 1-second display
                if (remainingTime > 0) {
                    try {
                        Thread.sleep(remainingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    inventoryTable.setModel(viewModel.getTableModel());
                    progressBar.setVisible(false); // Hide progress bar after loading completes
                    revalidate();
                    repaint();
                });
            }
        }.execute();
    }

    // **Load filtered inventory with progress indicator**
    public void loadFilteredInventoryWithProgress(String filter) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setVisible(true);
            progressBar.setString("Filtering: " + filter);
            revalidate();
            repaint();
        });

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    Thread.sleep(100); // **Small delay for UI update**
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                viewModel.filterInventory(filter);
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    inventoryTable.setModel(viewModel.getTableModel());
                    progressBar.setVisible(false);
                    revalidate();
                    repaint();
                });
            }
        }.execute();
    }
}
