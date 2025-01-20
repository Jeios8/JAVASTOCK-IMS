package main.java.com.javastock.utils;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

public class CSVExporter {
    public static void exportToCSV(JTable table) {
        // Generate timestamped filename
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String suggestedFilename = "inventory_report_" + timestamp + ".csv";

        // Create File Chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Inventory Report");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        fileChooser.setSelectedFile(new File(suggestedFilename)); // Set default filename

        int userSelection = fileChooser.showSaveDialog(null);

        // If user cancels, exit
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();

        // Ensure the file has a .csv extension
        if (!filePath.toLowerCase().endsWith(".csv")) {
            filePath += ".csv";
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            TableModel model = table.getModel();

            // Write column headers
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.write(model.getColumnName(i) + ",");
            }
            writer.write("\n");

            // Write data rows
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.write(model.getValueAt(i, j).toString() + ",");
                }
                writer.write("\n");
            }

            JOptionPane.showMessageDialog(null, "Inventory exported successfully to:\n" + filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error exporting file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
