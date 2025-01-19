package snippets;

import main.java.com.javastock.viewmodel.InventoryVM;
import main.java.com.javastock.viewmodel.MainVM;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI {
    private MainVM viewModel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainUI(MainVM viewModel) {
        this.viewModel = viewModel;
        initializeUI();
    }

    public void initializeUI() {
        JFrame mainFrame = new JFrame("JAVASTOCK");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setLocationRelativeTo(null);

        // MAIN PANEL
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        // LEFT MENU PANEL
        JPanel menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.WEST);

        // TOP + CARDLAYOUT CONTAINER PANEL
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.LIGHT_GRAY);

        // Add the search panel at the top of the centerPanel
        JPanel searchPanel = createSearchPanel();
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // CARDLAYOUT PANEL (for dynamic switching)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.LIGHT_GRAY);

        // Add cards to the CardLayout
        contentPanel.add(createDashboardPanel(), "Dashboard");
        contentPanel.add(createInventoryPanel(), "Inventory");
        contentPanel.add(createReportsPanel(), "Reports");
        contentPanel.add(createSuppliersPanel(), "Suppliers");
        contentPanel.add(createOrdersPanel(), "Orders");
        contentPanel.add(createManageStorePanel(), "Manage Store");
        contentPanel.add(createLogoutPanel(), "Logout");

        // Add the card layout panel to the center panel
        centerPanel.add(contentPanel, BorderLayout.CENTER);

        // Add the center panel to the main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }


    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setPreferredSize(new Dimension(250, 720));
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

        menuPanel.add(createMenuButton("Dashboard", "Dashboard"));
        menuPanel.add(createMenuButton("Inventory", "Inventory"));
        menuPanel.add(createMenuButton("Reports", "Reports"));
        menuPanel.add(createMenuButton("Suppliers", "Suppliers"));
        menuPanel.add(createMenuButton("Orders", "Orders"));
        menuPanel.add(createMenuButton("Manage Store", "Manage Store"));
        menuPanel.add(createPlaceholder());
        menuPanel.add(createPlaceholder());
        menuPanel.add(createMenuButton("Logout", "Logout"));

        return menuPanel;
    }

    private JButton createMenuButton(String text, String cardName) {
        JButton button = createButton(text);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, cardName);
            }
        });
        return button;
    }

    private JPanel createSearchPanel() {
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
        searchButton.setBackground(new Color(150, 200, 230));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFont(regularFont());
        searchButton.setFocusPainted(true);
        searchButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        return searchPanel;
    }

    // Create individual panels for each card
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.CYAN);
        panel.add(new JLabel("Welcome to the Dashboard"));
        return panel;
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.YELLOW);
        panel.add(new JLabel("Inventory Management Section"));
        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.GREEN);
        panel.add(new JLabel("Reports Section"));
        return panel;
    }

    private JPanel createSuppliersPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.ORANGE);
        panel.add(new JLabel("Suppliers Management Section"));
        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.PINK);
        panel.add(new JLabel("Orders Section"));
        return panel;
    }

    private JPanel createManageStorePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.MAGENTA);
        panel.add(new JLabel("Manage Store Section"));
        return panel;
    }

    private JPanel createLogoutPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.RED);
        panel.add(new JLabel("You have been logged out."));
        return panel;
    }

    // Helper methods
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(regularFont());
        button.setBackground(new Color(150, 200, 230));
        button.setForeground(Color.BLACK);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return button;
    }

    public static JPanel createPlaceholder() {
        JPanel placeholder = new JPanel();
        placeholder.setBackground(Color.WHITE);
        placeholder.setAlignmentX(Component.CENTER_ALIGNMENT);
        placeholder.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return placeholder;
    }

    public static Font regularFont() {
        return new Font("Arial", Font.PLAIN, 15);
    }
}
