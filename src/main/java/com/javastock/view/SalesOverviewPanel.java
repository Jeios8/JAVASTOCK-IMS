package main.java.com.javastock.view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SalesOverviewPanel extends JPanel {
    public SalesOverviewPanel(Map<String, Integer> salesData) {
        setLayout(new GridLayout(1, 4, 10, 10));
        setBackground(new Color(240, 240, 240));

        add(createCard("Sales Transaction", salesData.getOrDefault("Total Sales", 0), new Color(0, 123, 255)));
        add(createCard("Revenue", salesData.getOrDefault("Revenue", 0), new Color(40, 167, 69)));
        add(createCard("Profit", salesData.getOrDefault("Profit", 0), new Color(255, 193, 7)));
        add(createCard("Cancelled Orders", salesData.getOrDefault("Canceled Orders", 0), new Color(220, 53, 69)));
    }

    private JPanel createCard(String title, int value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(150, 100));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(color);

        String s = String.valueOf(value);
        if (!title.equals("Sales Transaction") && !title.equals("Cancelled Orders"))
            s = "₱ " + s;
        JLabel valueLabel = new JLabel(s);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }
}
