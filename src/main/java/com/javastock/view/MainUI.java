package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainUI {
    private MainVM viewModel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private static final Color BUTTON_COLOR = new Color(153, 204, 255);

    // Map of dynamic panels keyed by their section names
    private Map<String, JPanel> dynamicPanels = new HashMap<>();

    // Paths to your icons
    String[] sourceIcons = {
            "src\\main\\resources\\icons\\Icon_Dashboard.png",
            "src\\main\\resources\\icons\\Icon_Inventory.png",
            "src\\main\\resources\\icons\\Icon_Reports.png",
            "src\\main\\resources\\icons\\Icon_Suppliers.png",
            "src\\main\\resources\\icons\\Icon_Orders.png",
            "src\\main\\resources\\icons\\Icon_Store.png",
            "src\\main\\resources\\icons\\Icon_Logout.png"
    };
    ImageIcon[] menuIcons = resizeIcons(sourceIcons, 20, 20); // Resized icons to 20x20 pixels

    public MainUI(MainVM viewModel) {
        if (viewModel == null) throw new IllegalArgumentException("MainVM cannot be null");
        this.viewModel = viewModel;

        // 1. Initialize and store your custom panels in the map
        DashboardUI dashboardUI = new DashboardUI(new DashboardVM());
        InventoryPanel inventoryPanel = new InventoryPanel(new InventoryVM());
        ReportsUI reportsUI = new ReportsUI(new ReportsVM());
        SupplierUI supplierUI = new SupplierUI(new SupplierVM());
        OrdersUI ordersUI = new OrdersUI(new OrdersVM());
        WarehousePanel warehousePanel = new WarehousePanel(new WarehouseVM());

        // Map keys match the ViewModel sections:
        dynamicPanels.put("Dashboard", dashboardUI);
        dynamicPanels.put("Inventory", inventoryPanel);
        dynamicPanels.put("Reports", reportsUI);
        dynamicPanels.put("Suppliers", supplierUI);
        dynamicPanels.put("Orders", ordersUI);
        dynamicPanels.put("Manage Store", warehousePanel);

        // 2. Proceed with standard UI initialization
        initializeUI();
    }

    private void initializeUI() {
        JFrame mainFrame = new JFrame("JAVASTOCK");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setUndecorated(false);
        mainFrame.setSize(1280, 720);
        mainFrame.setLocationRelativeTo(null);
        // mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // for fullscreen

        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        // Menu Panel (Left)
        JPanel menuPanel = new MenuPanel(viewModel, mainFrame);
        mainPanel.add(menuPanel, BorderLayout.WEST);

        // Center Panel (Search + Card Layout)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new SearchPanel(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // 3. Add sections from the ViewModel (default content panels)
        // Ensure "Dashboard" is added first
        DashboardVM dashboardVM = new DashboardVM(); // Single Instance
        contentPanel.add("Dashboard", new DashboardUI(dashboardVM));

        for (String section : viewModel.getSections().keySet()) {
            if (!section.equals("Dashboard")) {
                JPanel panel = new JPanel();
                panel.setBackground(viewModel.getSectionColor(section));
                panel.add(new JLabel(section + " Content"));
                contentPanel.add(panel, section);
            }
        }

        centerPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    /**
     * Helper method to add a panel to the contentPanel if it hasn't been added before.
     * @param sectionKey The key used to retrieve the panel from dynamicPanels
     */
    private void addPanelIfNeeded(String sectionKey) {
        JPanel panel = dynamicPanels.get(sectionKey);
        if (panel != null && !isPanelAdded(panel)) {
            contentPanel.add(panel, sectionKey);
        }
    }

    /**
     * Checks if the given panel is already in contentPanel.
     */
    private boolean isPanelAdded(JPanel panel) {
        for (Component comp : contentPanel.getComponents()) {
            if (comp == panel) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the appropriate icon for each section.
     */
    public ImageIcon getIconForSection(String section) {
        return switch (section) {
            case "Dashboard" -> new ImageIcon(menuIcons[0].getImage());
            case "Inventory" -> new ImageIcon(menuIcons[1].getImage());
            case "Reports" -> new ImageIcon(menuIcons[2].getImage());
            case "Suppliers" -> new ImageIcon(menuIcons[3].getImage());
            case "Orders" -> new ImageIcon(menuIcons[4].getImage());
            case "Manage Store" -> new ImageIcon(menuIcons[5].getImage());
            default -> null;
        };
    }

    /**
     * Helper method to resize icons.
     */
    public static ImageIcon[] resizeIcons(String[] sourceIcons, int targetWidth, int targetHeight) {
        ImageIcon[] menuIcons = new ImageIcon[sourceIcons.length];
        for (int i = 0; i < sourceIcons.length; i++) {
            try {
                BufferedImage originalImage = ImageIO.read(new File(sourceIcons[i]));
                Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                menuIcons[i] = new ImageIcon(resizedImage);
            } catch (IOException e) {
                System.out.println("Error loading image: " + sourceIcons[i]);
            }
        }
        return menuIcons;
    }

    // -------------------------------------------
    // Sidebar Menu Inner Class
    // -------------------------------------------
    private class MenuPanel extends JPanel {
        private HashMap<String, JButton> menuButtons = new HashMap<>();
        private JButton activeButton;
        private MainVM viewModel;
        private JFrame mainFrame;

        public MenuPanel(MainVM viewModel, JFrame mainFrame) {
            if (viewModel == null) throw new IllegalArgumentException("MainVM cannot be null");
            this.viewModel = viewModel;
            this.mainFrame = mainFrame;

            ImageIcon img = new ImageIcon("src/main/resources/icons/icon_app.png");
            mainFrame.setIconImage(img.getImage());

            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(250, 720));
            setLayout(new BorderLayout());

            JPanel menuContainer = new JPanel();
            menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
            menuContainer.setBackground(getBackground());

            // Logo & spacing
            menuContainer.add(createLogoPanel());
            menuContainer.add(Box.createVerticalStrut(20));

            // Create buttons for each section
            for (String section : viewModel.getSections().keySet()) {
                JButton button = createMenuButton(section);
                menuButtons.put(section, button);
                menuContainer.add(button);
                menuContainer.add(Box.createVerticalStrut(5));
            }

            // Logout button
            JButton logoutButton = new JButton("Logout");
            logoutButton.setIcon(new ImageIcon(menuIcons[6].getImage()));
            logoutButton.setIconTextGap(20);
            logoutButton.setBackground(Color.WHITE);
            logoutButton.setForeground(Color.DARK_GRAY);
            logoutButton.setFocusPainted(false);
            logoutButton.setBorderPainted(false);
            logoutButton.setFont(new Font("Helvetica", Font.PLAIN, 17));
            logoutButton.setHorizontalAlignment(SwingConstants.LEFT);

            logoutButton.setPreferredSize(new Dimension(200, 50));
            logoutButton.setMinimumSize(new Dimension(200, 50));
            logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            logoutButton.addActionListener(e -> handleLogout());

            JPanel logoutPanel = new JPanel();
            logoutPanel.setLayout(new BoxLayout(logoutPanel, BoxLayout.Y_AXIS));
            logoutPanel.setBackground(getBackground());
            logoutPanel.add(Box.createVerticalGlue());
            logoutPanel.add(logoutButton);

            add(menuContainer, BorderLayout.NORTH);
            add(logoutPanel, BorderLayout.SOUTH);

            // Highlight the active section’s button
            setActiveButton(menuButtons.get(viewModel.getActiveSection()));

            // Set Dashboard as active button when UI loads
            setActiveButton(menuButtons.get("Dashboard"));
        }

        private JButton createMenuButton(String section) {
            JButton button = new JButton(section);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setFont(new Font("Helvetica", Font.PLAIN, 15));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setBackground(new Color(150, 200, 230));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setPreferredSize(new Dimension(200, 50));
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            button.setIcon(getIconForSection(section));
            button.setIconTextGap(20);

            // Handle panel addition and switching
            button.addActionListener(e -> {
                // For sections that map to dynamic panels
                if (section.equals("Inventory")) {
                    addPanelIfNeeded("Inventory");
                    // ((InventoryPanel) dynamicPanels.get("Inventory")).loadInventoryWithProgress();
                }
                else if (section.equals("Dashboard")) {
                    addPanelIfNeeded("Dashboard");
                }
                else if (section.equals("Suppliers")) {
                    addPanelIfNeeded("Suppliers");
                }
                else if (section.equals("Reports")) {
                    addPanelIfNeeded("Reports");
                }
                else if (section.equals("Orders")) {
                    addPanelIfNeeded("Orders");
                }
                else if (section.equals("Manage Store")) {
                    addPanelIfNeeded("Manage Store");
                }
                // ... More sections with dynamic panels could go here ...

                // Switch to the chosen section
                viewModel.setActiveSection(section);
                cardLayout.show(contentPanel, section);
                setActiveButton(button);
            });
            return button;
        }

        private void handleLogout() {
            int choice = JOptionPane.showConfirmDialog(
                    null,
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

        private void setActiveButton(JButton selectedButton) {
            if (selectedButton == null) return;
            for (JButton button : menuButtons.values()) {
                button.setBackground(Color.WHITE);
                button.setFont(new Font("Helvetica", Font.PLAIN, 16));
                button.setForeground(Color.DARK_GRAY);
            }
            selectedButton.setBackground(Color.WHITE);
            selectedButton.setForeground(new Color(0, 0, 150));
            activeButton = selectedButton;
        }

        private JPanel createLogoPanel() {
            JPanel logoPanel = new JPanel(new GridBagLayout());
            logoPanel.setBackground(getBackground());

            JLabel logoLabel = new JLabel("JAVASTOCK");
            logoLabel.setFont(new Font("Helvetica", Font.PLAIN, 30));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel subLogoLabel = new JLabel("Inventory Management System");
            subLogoLabel.setFont(new Font("Helvetica", Font.PLAIN, 15));
            subLogoLabel.setHorizontalAlignment(SwingConstants.CENTER);

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
            searchField.setFont(new Font("Helvetica", Font.PLAIN, 15));

            JButton searchButton = createButton("Search", BUTTON_COLOR);

            add(searchField, BorderLayout.CENTER);
            add(searchButton, BorderLayout.EAST);
        }
    }
    private JButton createButton(String text, Color color) {
        main.java.com.javastock.view.Button button = new main.java.com.javastock.view.Button(text, color);
        button.setFont(new Font("Helvetica", Font.BOLD,12));
        button.setBackground(color);
        button.setForeground(Color.DARK_GRAY);
        return button;
    }

    class Button extends JButton { // Rounded Buttons
        private Color normalColor;
        private Color hoverColor;
        private Color clickColor;

        public Button(String text, Color color) {
            super(text);
            setBackground(color);
            normalColor = color;
            hoverColor = color.brighter();
            clickColor = color.darker();

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(hoverColor);
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(normalColor);
                    repaint();
                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    setBackground(clickColor);
                    repaint();
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    setBackground(hoverColor);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
