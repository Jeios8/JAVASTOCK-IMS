package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.ProductVM;

import javax.swing.*;
import java.awt.*;

public class EditProductDialog extends JDialog {
    private final int productId;
    private JTextField nameField, priceField, quantityField, thresholdField;
    private JButton saveButton, cancelButton;
    private final ProductVM productVM;

    public EditProductDialog(JFrame parent, int productId, String productName, double buyingPrice, int quantity, int threshold) {
        super(parent, "Edit Product", true);
        this.productId = productId;
        this.productVM = new ProductVM(productId);

        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        mainPanel.add(new JLabel("Product Name:"));
        nameField = new JTextField(productName);
        mainPanel.add(nameField);

        mainPanel.add(new JLabel("Buying Price:"));
        priceField = new JTextField(String.valueOf(buyingPrice));
        mainPanel.add(priceField);

        mainPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(String.valueOf(quantity));
        mainPanel.add(quantityField);

        mainPanel.add(new JLabel("Threshold:"));
        thresholdField = new JTextField(String.valueOf(threshold)); // ✅ Added Threshold Field
        mainPanel.add(thresholdField);

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> updateProduct());

        setVisible(true);
    }

    private void updateProduct() {
        String name = nameField.getText();
        double price;
        int quantity, threshold;

        try {
            price = Double.parseDouble(priceField.getText());
            quantity = Integer.parseInt(quantityField.getText());
            threshold = Integer.parseInt(thresholdField.getText()); // ✅ Get threshold from input
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = productVM.updateProduct(name, price, quantity, threshold); // ✅ Now passing 4 parameters
        if (success) {
            JOptionPane.showMessageDialog(this, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update product.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
