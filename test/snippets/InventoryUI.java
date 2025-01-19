package snippets;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InventoryUI {
    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("JAVASTOCK");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setLocationRelativeTo(null);
//MAIN PANEL(BACKGROUND)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.LIGHT_GRAY);

//LEFT MENU PANEL
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setPreferredSize(new Dimension(250, mainFrame.getHeight()));
        menuPanel.setLayout(new GridLayout(10, 1, 0, 5));

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(menuPanel.getBackground());
        menuPanel.add(logoPanel);
        JLabel logoLabel = new JLabel("JAVASTOCK");
        logoLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        logoPanel.add(logoLabel);
        JLabel sublogoLabel = new JLabel("Inventory Management System");
        sublogoLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        logoPanel.add(sublogoLabel);

        menuPanel.add(createButton("Dashboard"));
        menuPanel.add(createButton("Inventory"));
        menuPanel.add(createButton("Reports"));
        menuPanel.add(createButton("Suppliers"));
        menuPanel.add(createButton("Orders"));
        menuPanel.add(createButton("Manage Store"));
        menuPanel.add(createPlaceholder());
        menuPanel.add(createPlaceholder());
        menuPanel.add(createButton("Logout"));

//TOP SEARCH PANEL
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(new EmptyBorder(15, 5, 15, 650));
        searchPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField("Search product, supplier, order", 20);
        searchField.setFont(regularFont());
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(150,200,230));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFont(regularFont());
        searchButton.setFocusPainted(true);
        searchButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        JPanel rightTopPanel = new JPanel(new BorderLayout());
        rightTopPanel.setBackground(Color.LIGHT_GRAY);
        rightTopPanel.add(searchPanel, BorderLayout.NORTH);

//CONTENT PANEL
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);

        JLabel contentHolder = new JLabel("INVENTORY CONTENT GOES HERE", JLabel.CENTER);
        contentHolder.setFont(regularFont());
        contentHolder.setBackground(Color.LIGHT_GRAY);
        contentPanel.add(contentHolder, BorderLayout.CENTER);


        rightTopPanel.add(contentPanel, BorderLayout.CENTER);
//ADD ELEMENTS HERE
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(rightTopPanel, BorderLayout.CENTER);
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    //METHODS
    public static JButton createButton(String text){
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(regularFont());
        button.setBackground(new Color(150,200,230));
        button.setForeground(Color.BLACK);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return button;
    }
    public static JPanel createPlaceholder(){
        JPanel placeholder = new JPanel();
        placeholder.setBackground(Color.WHITE);
        placeholder.setAlignmentX(Component.CENTER_ALIGNMENT);
        placeholder.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return placeholder;
    }
    public static Font regularFont(){
        return new Font("Arial", Font.PLAIN, 15);
    }
}