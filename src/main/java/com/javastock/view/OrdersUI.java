package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.OrdersVM;

import javax.swing.*;
import java.awt.*;

public class OrdersUI extends JPanel {
    private OrdersVM ordersVM;
    private static final Font TITLE_FONT = new Font("Helvetica", Font.BOLD, 16);

    public OrdersUI(OrdersVM viewModel) {
        this.ordersVM = viewModel;
        setLayout(new BorderLayout());

        //Add components to main Panel
        add(createPadding(0,20), BorderLayout.NORTH);
        add(createPadding(20,0), BorderLayout.EAST);
        add(createPadding(20,0), BorderLayout.WEST);
        add(createPadding(0,20), BorderLayout.SOUTH);
        add(contentPanel(), BorderLayout.CENTER);
    }
    private JPanel contentPanel() {
        //Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.LIGHT_GRAY);
        contentPanel.setLayout(new BorderLayout(0,20));
        contentPanel.add(topPanel(), BorderLayout.NORTH);
        contentPanel.add(tablePanel(), BorderLayout.CENTER);
        return contentPanel;
    }
    private JPanel topPanel(){
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setPreferredSize(new Dimension(500,100));
        topPanel.setBackground(Color.WHITE);

        return topPanel;
    }
    private JPanel tablePanel(){
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(new JLabel("SET ORDER TABLE HERE"));

        // PUT TABLE HERE

        return tablePanel;
    }
    private JPanel createPadding(int width, int height) {
        JPanel padding = new JPanel();
        padding.setBackground(Color.LIGHT_GRAY);
        padding.setPreferredSize(new Dimension(width, height));
        return padding;
    }
}
