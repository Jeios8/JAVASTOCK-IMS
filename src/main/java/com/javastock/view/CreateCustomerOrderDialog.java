package main.java.com.javastock.view;

import main.java.com.javastock.utils.DataPreloader;
import main.java.com.javastock.viewmodel.OrdersVM;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class CreateCustomerOrderDialog extends JDialog {
    private JTextField firstNameField, lastNameField, phoneField, emailField;
    private JComboBox<String> itemDropdown;
    private JTextField quantityField;
    private JDatePickerImpl orderDatePicker;
    private JButton addButton, cancelButton;
    private OrdersVM ordersVM;

    public CreateCustomerOrderDialog(JFrame parent, OrdersVM ordersVM) {
        super(parent, "Create Customer Order", true);
        this.ordersVM = ordersVM;
        setLayout(new BorderLayout());
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(false);

        ImageIcon img = new ImageIcon("src/main/resources/icons/icon_app.png");
        setIconImage(img.getImage());

        // **Main Panel**
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.4;

        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.gridx = 1;
        inputGbc.gridy = 0;
        inputGbc.weightx = 0.6;
        inputGbc.fill = GridBagConstraints.HORIZONTAL;

        // **Customer Information Panel**
        firstNameField = createTextField("Enter First Name");
        lastNameField = createTextField("Enter Last Name");
        phoneField = createTextField("Enter Phone");
        emailField = createTextField("Enter Email");

        mainPanel.add(new JLabel("First Name:"), gbc);
        mainPanel.add(firstNameField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Last Name:"), gbc);
        mainPanel.add(lastNameField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Phone:"), gbc);
        mainPanel.add(phoneField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Email:"), gbc);
        mainPanel.add(emailField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        // **Order Details Panel**
        itemDropdown = createRoundedDropdown(DataPreloader.getData("products"));
        quantityField = createTextField("Enter Quantity");

        mainPanel.add(new JLabel("Item Name:"), gbc);
        mainPanel.add(itemDropdown, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Quantity:"), gbc);
        mainPanel.add(quantityField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        add(mainPanel, BorderLayout.CENTER);

        // **Buttons Panel**
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        addButton = createRoundedButton("Create Order", new Color(0, 123, 255)); // Blue
        cancelButton = createRoundedButton("Cancel", new Color(220, 53, 69)); // Red

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // **Button Actions**
        addButton.addActionListener(e -> createOrder());
        cancelButton.addActionListener(e -> dispose());

        // **Load dropdown data asynchronously**
        loadDropdownData();

        setVisible(true);
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setColumns(15);
        return field;
    }

    private JComboBox<String> createRoundedDropdown(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBorder(new RoundedBorder(10));
        comboBox.setPreferredSize(new Dimension(200, 30));
        return comboBox;
    }

    private JButton createRoundedButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(new RoundedBorder(15));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private final int radius;
        public RoundedBorder(int radius) {
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.GRAY);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }
    }

    private void loadDropdownData() {
        new SwingWorker<String[], Void>() {
            @Override
            protected String[] doInBackground() {
                return ordersVM.getProductNames();
            }
            @Override
            protected void done() {
                try {
                    String[] data = get();
                    itemDropdown.setModel(new DefaultComboBoxModel<>(data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void createOrder() {
        boolean success = ordersVM.addCustomerOrder(
                firstNameField.getText(),
                lastNameField.getText(),
                phoneField.getText(),
                emailField.getText(),
                (String) itemDropdown.getSelectedItem(),
                Integer.parseInt(quantityField.getText())
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Customer order created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
