package snippets;

import main.java.com.javastock.viewmodel.MainVM;
import main.java.com.javastock.view.DashboardUI;
import main.java.com.javastock.view.InventoryUI;

import javax.swing.*;
import java.awt.*;

public class MainUI {
    private MainVM viewModel;
    private JPanel contentPanel;

    public MainUI(MainVM viewModel) {
        this.viewModel = viewModel;
        initializeUI();
    }

    private void initializeUI() {
        JFrame mainFrame = new JFrame("JAVASTOCK");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1280, 720);
        mainFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel menuPanel = createMenuPanel();

        // Content panel for dynamic content switching
        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(new JLabel("Welcome to JAVASTOCK!", JLabel.CENTER), "default");

        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(10, 1, 0, 5));
        menuPanel.setPreferredSize(new Dimension(250, 720));
        menuPanel.setBackground(Color.WHITE);

        JButton dashboardButton = createButton("Dashboard", "dashboard");
        JButton inventoryButton = createButton("Inventory", "inventory");

        menuPanel.add(dashboardButton);
        menuPanel.add(inventoryButton);

        return menuPanel;
    }

    private JButton createButton(String label, String action) {
        JButton button = new JButton(label);
        button.addActionListener(e -> switchContent(action));
        return button;
    }

    private void switchContent(String action) {
        contentPanel.removeAll();

        switch (action) {
            case "dashboard":
                contentPanel.add(new DashboardUI(viewModel.getDashboardVM()).getPanel(), "dashboard");
                break;
            case "inventory":
                contentPanel.add(new InventoryUI(viewModel.getInventoryVM()).getPanel(), "inventory");
                break;
            default:
                contentPanel.add(new JLabel("Unknown Section", JLabel.CENTER), "unknown");
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
