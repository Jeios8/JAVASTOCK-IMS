package main.java.com.javastock.utils;

import main.java.com.javastock.view.InventoryPanel;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

/**
 * Utility class for exporting data from a JTable to a CSV file.
 */
public class CSVExporter {

    /**
     * Exports the contents of a JTable to a CSV file.
     *
     * @param table the JTable containing data to be exported.
     *              Each column header and row data will be written to the CSV file.
     */
    public static void exportToCSV(JTable table) {
        // Generate a filename with a timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String suggestedFilename = "javastock_report_" + timestamp + ".csv";

        // Configure file chooser for saving the CSV
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        fileChooser.setSelectedFile(new File(suggestedFilename)); // Suggest a default filename

        // Show save dialog to user and capture their selection
        int userSelection = fileChooser.showSaveDialog(null);

        // If the user cancels, exit the method
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        // Get the selected file path
        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();

        // Ensure the file has a .csv extension
        if (!filePath.toLowerCase().endsWith(".csv")) {
            filePath += ".csv";
        }

        // Attempt to write the JTable data to the CSV file
        try (FileWriter writer = new FileWriter(filePath)) {
            TableModel model = table.getModel();

            // Write column headers to the CSV
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.write(model.getColumnName(i) + ",");
            }
            writer.write("\n");

            // Write each row of data to the CSV
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.write(model.getValueAt(i, j).toString() + ",");
                }
                writer.write("\n");
            }

            // Show a success message to the user
            JOptionPane.showMessageDialog(null, "Report exported successfully to:\n" + filePath);
        } catch (IOException e) {
            // Show an error message if something goes wrong
            JOptionPane.showMessageDialog(null, "Error exporting file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}