package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.SupplierVM;

import javax.swing.*;
import java.awt.*;

public class SupplierUI extends JPanel {
    private SupplierVM supplierVM;
    private static final Font TITLE_FONT = new Font("Helvetica", Font.BOLD, 16);

    public SupplierUI(SupplierVM viewModel) {
        this.supplierVM = viewModel;
        setLayout(new BorderLayout());

        //Add components to main Panel
        add(createPadding(0,20), BorderLayout.NORTH);
        add(createPadding(20,0), BorderLayout.EAST);
        add(createPadding(20,0), BorderLayout.WEST);
        add(createPadding(0,20), BorderLayout.SOUTH);
        add(contentPanel(), BorderLayout.CENTER);
    }
    private JPanel createPadding(int width, int height) {
        JPanel padding = new JPanel();
        padding.setBackground(Color.LIGHT_GRAY);
        padding.setPreferredSize(new Dimension(width, height));
        return padding;
    }
    private JPanel contentPanel() {
        //Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Suppliers");
        titleLabel.setFont(TITLE_FONT);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(titleLabel);
        return contentPanel;
    }
}
