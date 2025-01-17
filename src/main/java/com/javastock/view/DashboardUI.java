package main.java.com.javastock.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import main.java.com.javastock.viewmodel.DashboardVM;

public class DashboardUI {
    private DashboardVM viewModel;

    public DashboardUI(DashboardVM viewModel) {
        this.viewModel = viewModel;
        initializeUI();
    }

    private void initializeUI() {
        JFrame mainFrame = new JFrame("JAVASTOCK");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setLocationRelativeTo(null);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        // LEFT MENU PANEL
        JPanel menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.WEST);

        // RIGHT PANEL (Search and Content)
        JPanel rightPanel = createRightPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(10, 1, 0, 5));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setPreferredSize(new Dimension(250, 720));

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(menuPanel.getBackground());
        JLabel logoLabel = new JLabel("JAVASTOCK", JLabel.CENTER);
        logoLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        logoPanel.add(logoLabel);

        JLabel sublogoLabel = new JLabel("Inventory Management System", JLabel.CENTER);
        sublogoLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        logoPanel.add(sublogoLabel);
        menuPanel.add(logoPanel);

        // Buttons linked to ViewModel actions
        menuPanel.add(createButton("Dashboard", "dashboard"));
        menuPanel.add(createButton("Inventory", "inventory"));
        menuPanel.add(createButton("Reports", "reports"));
        menuPanel.add(createButton("Suppliers", "suppliers"));
        menuPanel.add(createButton("Orders", "orders"));
        menuPanel.add(createButton("Manage Store", "manageStore"));
        menuPanel.add(createPlaceholder());
        menuPanel.add(createPlaceholder());
        menuPanel.add(createButton("Logout", "logout"));

        return menuPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.LIGHT_GRAY);

        // SEARCH PANEL
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
        searchButton.setFont(regularFont());
        searchButton.setBackground(new Color(150, 200, 230));
        searchButton.addActionListener(e -> viewModel.searchAction(searchField.getText()));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // CONTENT PANEL
        JLabel contentHolder = new JLabel("CONTENT GOES HERE", JLabel.CENTER);
        contentHolder.setFont(regularFont());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);
        contentPanel.add(contentHolder, BorderLayout.CENTER);

        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(contentPanel, BorderLayout.CENTER);

        return rightPanel;
    }

    private JButton createButton(String text, String action) {
        JButton button = new JButton(text);
        button.setFont(regularFont());
        button.setBackground(new Color(150, 200, 230));
        button.addActionListener(e -> viewModel.handleMenuAction(action));
        return button;
    }

    private JPanel createPlaceholder() {
        JPanel placeholder = new JPanel();
        placeholder.setBackground(Color.WHITE);
        return placeholder;
    }

    private Font regularFont() {
        return new Font("Arial", Font.PLAIN, 15);
    }
}
