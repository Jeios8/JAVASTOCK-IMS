package main.java.com.javastock.view;

import com.mysql.cj.log.Log;
import main.java.com.javastock.model.User;
import main.java.com.javastock.utils.DataPreloader;
import main.java.com.javastock.viewmodel.LoginVM;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SplashUI {
    private JFrame frame;

    public SplashUI() {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(350, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // Top Logo
        JLabel topLogo = new JLabel(new ImageIcon("src/main/resources/loading.gif"));
        topLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel title = new JLabel("JAVASTOCK");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(new Color(36, 23, 70));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Inventory Management System");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.DARK_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Loading Message
        JLabel loadingLabel = new JLabel("Loading Please Wait...");
        loadingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Bottom Logo (Powered By)
        JLabel poweredBy = new JLabel("Powered by:");
        poweredBy.setFont(new Font("Arial", Font.PLAIN, 12));
        poweredBy.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bottomLogo = new JLabel(new ImageIcon("src/main/resources/team_logo.png"));
        bottomLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add Components
        panel.add(Box.createVerticalStrut(20)); // Spacing
        panel.add(topLogo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(title);
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loadingLabel);
        panel.add(Box.createVerticalGlue());
        panel.add(poweredBy);
        panel.add(bottomLogo);
        panel.add(Box.createVerticalStrut(20));

        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
        preloadUserAccounts();
    }

    private void preloadUserAccounts() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                System.out.println("Preloading user accounts...");

                // Define query to fetch usernames or relevant data
                Map<String, String> queries = new HashMap<>();
                queries.put("users", "SELECT username FROM users WHERE is_active = TRUE");
                queries.put("categories", "SELECT category_name FROM categories WHERE is_active = true");
                queries.put("suppliers", "SELECT supplier_name FROM suppliers WHERE is_active = true");
                queries.put("warehouses", "SELECT warehouse_name FROM warehouses WHERE is_active = true");
                queries.put("products", "SELECT product_name FROM products WHERE is_active = true");

                // Preload data using DataPreloader
                DataPreloader.preloadData(queries);

                System.out.println("User accounts preloaded successfully.");
                return null;
            }

            @Override
            protected void done() {
                frame.dispose(); // Close splash screen
                new LoginUI(new LoginVM()); // Open main application
            }
        };
        worker.execute();
    }
}
