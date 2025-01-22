package main.java.com.javastock.view;

import main.java.com.javastock.utils.DataPreloader;
import main.java.com.javastock.viewmodel.InventoryVM;
import main.java.com.javastock.viewmodel.WarehouseVM;

import javax.swing.*;
import java.awt.*;

public class AddStoreDialog extends JDialog {
    private JTextField branchNameField, storeNameField, addressField, contactField, phoneField;
    private JButton addButton, cancelButton;
    private WarehouseVM viewModel;

    public AddStoreDialog(JFrame parent, WarehouseVM viewModel) {
        super(parent, "Add Store", true);
        this.viewModel = viewModel;
        setLayout(new BorderLayout());
        setSize(420, 440);
        setLocationRelativeTo(parent);
        setResizable(false);

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

        // **Create Rounded Text Fields**
        branchNameField = createRoundedTextField("Enter branch name...");
        storeNameField = createRoundedTextField("Enter store name...");
        addressField = createRoundedTextField("Enter address...");
        contactField = createRoundedTextField("Enter contact info...");
        phoneField = createRoundedTextField("Enter phone Number...");


        // **Add Labels & Fields**
        mainPanel.add(new JLabel("Branch Name:"), gbc);
        mainPanel.add(branchNameField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Store Name:"), gbc);
        mainPanel.add(storeNameField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Address:"), gbc);
        mainPanel.add(addressField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Contact:"), gbc);
        mainPanel.add(contactField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        mainPanel.add(new JLabel("Phone Number:"), gbc);
        mainPanel.add(phoneField, inputGbc);
        gbc.gridy++;
        inputGbc.gridy++;

        add(mainPanel, BorderLayout.CENTER);

        // **Buttons Panel**
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        addButton = createRoundedButton("Add Store", new Color(0, 123, 255)); // Blue
        cancelButton = createRoundedButton("Cancel", new Color(220, 53, 69)); // Red

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // **Button Actions**
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    // **Rounded JTextField with Placeholder Support**
    private JTextField createRoundedTextField(String placeholder) {
        JTextField field = new PlaceholderTextField(placeholder);
        field.setBorder(new RoundedBorder(10));
        field.setPreferredSize(new Dimension(200, 30));
        return field;
    }

    // **Custom JTextField with Placeholder**
    class PlaceholderTextField extends JTextField {
        private String placeholder;
        private boolean showingPlaceholder = true;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setText(placeholder);
            setForeground(Color.GRAY);

            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (showingPlaceholder) {
                        setText("");
                        setForeground(Color.BLACK);
                        showingPlaceholder = false;
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
                        showingPlaceholder = true;
                    }
                }
            });
        }

        @Override
        public String getText() {
            return showingPlaceholder ? "" : super.getText();
        }
    }
    // **Rounded Buttons**
    private JButton createRoundedButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(new RoundedBorder(15));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    // **Rounded Border for UI Components**
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
}
