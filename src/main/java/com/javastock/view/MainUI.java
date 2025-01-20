package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.InventoryVM;
import main.java.com.javastock.viewmodel.MainVM;
import main.java.com.javastock.viewmodel.LoginVM;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;

public class MainUI {
    private MainVM viewModel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainUI(MainVM viewModel) {
        if (viewModel == null) throw new IllegalArgumentException("MainVM cannot be null");
        this.viewModel = viewModel;
        initializeUI();
    }

    public void initializeUI() {
        JFrame mainFrame = new JFrame("JAVASTOCK");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1280, 720);
        mainFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        // Menu Panel (Left)
        JPanel menuPanel = new MenuPanel(viewModel, mainFrame);
        mainPanel.add(menuPanel, BorderLayout.WEST);

        // Center Panel (Search + Card Layout)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new SearchPanel(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Add sections dynamically
        for (String section : viewModel.getSections().keySet()) {
            JPanel panel = new JPanel();
            panel.setBackground(viewModel.getSectionColor(section));
            panel.add(new JLabel(section + " Content"));
            contentPanel.add(panel, section);
        }

        centerPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    /** Sidebar Menu */
    private class MenuPanel extends JPanel {
        private InventoryPanel inventoryPanel = new InventoryPanel(new InventoryVM()); // Create once
        private HashMap<String, JButton> menuButtons = new HashMap<>();
        private JButton activeButton;
        private MainVM viewModel;
        private JFrame mainFrame;

        public MenuPanel(MainVM viewModel, JFrame mainFrame) {
            if (viewModel == null) throw new IllegalArgumentException("MainVM cannot be null");
            this.viewModel = viewModel;
            this.mainFrame = mainFrame;

            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(250, 720));
            setLayout(new BorderLayout());

            JPanel menuContainer = new JPanel();
            menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
            menuContainer.setBackground(getBackground());

            menuContainer.add(createLogoPanel());
            menuContainer.add(Box.createVerticalStrut(20));

            for (String section : viewModel.getSections().keySet()) {
                JButton button = createMenuButton(section);
                menuButtons.put(section, button);
                menuContainer.add(button);
                menuContainer.add(Box.createVerticalStrut(5));
            }
            // ** Fix Logout Button Height **
            JButton logoutButton = new JButton("Logout");
            logoutButton.setBackground(Color.RED);
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setFont(new Font("Arial", Font.BOLD, 16));

            // Set button size
            logoutButton.setPreferredSize(new Dimension(200, 50)); // Fixed height
            logoutButton.setMinimumSize(new Dimension(200, 50));   // Prevent shrinking
            logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Expand width

            logoutButton.addActionListener(e -> handleLogout());

            // Wrap in a panel with BoxLayout for proper height control
            JPanel logoutPanel = new JPanel();
            logoutPanel.setLayout(new BoxLayout(logoutPanel, BoxLayout.Y_AXIS));
            logoutPanel.setBackground(getBackground());
            logoutPanel.add(Box.createVerticalGlue()); // Push logout button to the bottom
            logoutPanel.add(logoutButton);

            add(menuContainer, BorderLayout.NORTH);
            add(logoutPanel, BorderLayout.SOUTH);

            setActiveButton(menuButtons.get(viewModel.getActiveSection()));
        }

        private void handleLogout() {
            int choice = JOptionPane.showConfirmDialog(
                    null,  // Parent component (null centers on screen)
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                mainFrame.dispose();
                new LoginUI(new LoginVM());
            }
        }


        private JButton createMenuButton(String section) {
            JButton button = new JButton(section);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setFont(new Font("Arial", Font.PLAIN, 15));
            button.setBackground(new Color(150, 200, 230));
            button.setPreferredSize(new Dimension(200, 50));
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

//            button.addActionListener(e -> {
//                if (section.equals("Inventory")) {
//                    contentPanel.add(new InventoryPanel(new InventoryVM()), "Inventory"); // Reload InventoryPanel
//                }
//                viewModel.setActiveSection(section);
//                cardLayout.show(contentPanel, section);
//                setActiveButton(button);
//            });

            button.addActionListener(e -> {
                if (section.equals("Inventory")) {
                    if (!isInventoryAdded()) {
                        contentPanel.add(inventoryPanel, "Inventory"); // Add it only once
                    }
                    inventoryPanel.loadInventoryWithProgress(); // **Show progress bar while loading**
                }

                viewModel.setActiveSection(section);
                cardLayout.show(contentPanel, section);
                setActiveButton(button);
            });

            return button;
        }

        private void setActiveButton(JButton selectedButton) {
            if (selectedButton == null) return;

            for (JButton button : menuButtons.values()) {
                button.setBackground(new Color(150, 200, 230));
                button.setForeground(Color.BLACK);
            }

            selectedButton.setBackground(Color.DARK_GRAY);
            selectedButton.setForeground(Color.WHITE);
            activeButton = selectedButton;
        }

        private JPanel createLogoPanel() {
            JPanel logoPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for centering
            logoPanel.setBackground(getBackground());

            JLabel logoLabel = new JLabel("JAVASTOCK");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 30));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel subLogoLabel = new JLabel("Inventory Management System");
            subLogoLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            subLogoLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Add labels with GridBagConstraints to center them
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            logoPanel.add(logoLabel, gbc);

            gbc.gridy = 1;
            logoPanel.add(subLogoLabel, gbc);

            return logoPanel;
        }
    }

    private class SearchPanel extends JPanel {
        public SearchPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(15, 5, 15, 650));
            setBackground(Color.WHITE);

            JTextField searchField = new JTextField("Search product, supplier, order", 20);
            searchField.setFont(new Font("Arial", Font.PLAIN, 15));

            JButton searchButton = new JButton("Search");
            searchButton.setBackground(new Color(150, 200, 230));

            add(searchField, BorderLayout.CENTER);
            add(searchButton, BorderLayout.EAST);
        }
    }

    // Helper method to check if InventoryPanel is already added
    private boolean isInventoryAdded() {
        for (Component comp : contentPanel.getComponents()) {
            if (comp instanceof InventoryPanel) {
                return true;
            }
        }
        return false;
    }
}
