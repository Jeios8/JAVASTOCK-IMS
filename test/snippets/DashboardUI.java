package snippets;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardUI extends JFrame {

    public DashboardUI() {
        // Set up the main frame
        setTitle("JavaStock - Inventory Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setBounds(0, 0, 280, 900);
        sidebar.setBackground(Color.WHITE);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(240, 241, 243)));
        add(sidebar);

        JLabel logo = new JLabel("JAVASTOCK", SwingConstants.CENTER);
        logo.setFont(new Font("Inter", Font.BOLD, 24));
        logo.setForeground(new Color(56, 62, 73));
        logo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(logo);

        sidebar.add(createSidebarItem("Dashboard", new Color(21, 112, 239), "You clicked Dashboard"));
        sidebar.add(createSidebarItem("Inventory", new Color(93, 102, 121), "You clicked Inventory"));
        sidebar.add(createSidebarItem("Reports", new Color(93, 102, 121), "You clicked Reports"));
        sidebar.add(createSidebarItem("Suppliers", new Color(93, 102, 121), "You clicked Suppliers"));
        sidebar.add(createSidebarItem("Orders", new Color(93, 102, 121), "You clicked Orders"));
        sidebar.add(createSidebarItem("Manage Store", new Color(93, 102, 121), "You clicked Manage Store"));

        // Top Bar
        JPanel topBar = new JPanel();
        topBar.setBounds(280, 0, 1160, 100);
        topBar.setBackground(Color.WHITE);
        topBar.setLayout(new BorderLayout());
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 241, 243)));
        add(topBar);

        JTextField searchBar = new JTextField("Search product, supplier, order");
        searchBar.setPreferredSize(new Dimension(400, 40));
        searchBar.setBorder(BorderFactory.createLineBorder(new Color(240, 241, 243)));
        searchBar.setFont(new Font("Inter", Font.PLAIN, 16));
        topBar.add(searchBar, BorderLayout.WEST);

        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        profilePanel.setBackground(Color.WHITE);
        JLabel notificationIcon = new JLabel("🔔");
        JLabel avatarIcon = new JLabel("👤");
        profilePanel.add(notificationIcon);
        profilePanel.add(avatarIcon);
        topBar.add(profilePanel, BorderLayout.EAST);

        // Main Content Area
        JPanel mainContent = new JPanel();
        mainContent.setBounds(280, 100, 1160, 860);
        mainContent.setLayout(new GridLayout(3, 2, 20, 20));
        mainContent.setBackground(new Color(248, 249, 251));
        add(mainContent);

        // Add panels for dashboard sections
        mainContent.add(createContentPanel("Sales Overview"));
        mainContent.add(createContentPanel("Inventory Summary"));
        mainContent.add(createContentPanel("Purchase Overview"));
        mainContent.add(createContentPanel("Product Summary"));
        mainContent.add(createContentPanel("Sales & Purchase Chart"));
        mainContent.add(createContentPanel("Low Quantity Stock"));
    }

    private JPanel createSidebarItem(String text, Color color, String message) {
        JPanel menuItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        menuItem.setPreferredSize(new Dimension(280, 50));
        menuItem.setBackground(Color.WHITE);

        JLabel label = new JLabel(text);
        label.setFont(new Font("Inter", Font.PLAIN, 16));
        label.setForeground(color);
        menuItem.add(label);

        // Add click listener
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, message);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                menuItem.setBackground(new Color(240, 241, 243));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuItem.setBackground(Color.WHITE);
            }
        });

        return menuItem;
    }

    private JPanel createContentPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(240, 241, 243)));
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titleLabel.setForeground(new Color(56, 62, 73));
        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardUI dashboard = new DashboardUI();
            dashboard.setVisible(true);
        });
    }
}
