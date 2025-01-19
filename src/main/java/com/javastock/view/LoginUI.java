package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.LoginVM;
import main.java.com.javastock.viewmodel.MainVM;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI {
    private final JFrame loginFrame;
    private JTextField usernameField;  // Changed to non-final
    private JPasswordField passwordField;  // Changed to non-final
    private final LoginVM loginVM;

    // Shared UI constants
    private static final Font TITLE_FONT = new Font("Arial", Font.PLAIN, 40);
    private static final Font SUBTITLE_FONT = new Font("Arial", Font.PLAIN, 20);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color LOGIN_BUTTON_COLOR = new Color(30, 100, 255);
    private static final Color EXIT_BUTTON_COLOR = new Color(200, 50, 30);

    public LoginUI(LoginVM loginVM) {
        this.loginVM = loginVM;

        loginFrame = new JFrame("JAVASTOCK");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(450, 600);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);

        // Initialize main layout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Add sub-panels
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createInputPanel(), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        // Padding
        JPanel leftPadding = new JPanel();
        JPanel rightPadding = new JPanel();

        leftPadding.setBackground(mainPanel.getBackground());
        rightPadding.setBackground(mainPanel.getBackground());

        leftPadding.setPreferredSize(new Dimension(20, 0));  // 20px left padding
        rightPadding.setPreferredSize(new Dimension(20, 0)); // 20px right padding

        mainPanel.add(leftPadding, BorderLayout.WEST);
        mainPanel.add(rightPadding, BorderLayout.EAST);

        loginFrame.add(mainPanel);
        loginFrame.setVisible(true);

        usernameField.requestFocus();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("JAVASTOCK");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Inventory Management System");
        subtitleLabel.setFont(SUBTITLE_FONT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(Box.createVerticalStrut(50));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        return titlePanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(7, 1, 0, 0));
        inputPanel.setBackground(BACKGROUND_COLOR);

        JLabel usernameLabel = createLabel("Username:");
        usernameField = createTextField();
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel passwordLabel = createLabel("Password:");
        passwordField = createPasswordField();
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JButton loginButton = createButton("Login", LOGIN_BUTTON_COLOR, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        JButton exitButton = createButton("Exit", EXIT_BUTTON_COLOR, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("")); // Spacer
        inputPanel.add(loginButton);
        inputPanel.add(exitButton);

        // Set default button for Enter key
        loginFrame.getRootPane().setDefaultButton(loginButton);

        return inputPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(BACKGROUND_COLOR);

        JLabel bottomLabel = new JLabel("Powered by ERR Software Solutions");
        bottomLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        bottomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(Box.createVerticalStrut(50));

        bottomPanel.add(bottomLabel);
        return bottomPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(Color.BLACK);
        return label;
    }

    private TextField createTextField() {
        TextField textField = new TextField(15);
        textField.setFont(LABEL_FONT);
        textField.setMargin(new Insets(5, 10, 5, 10));
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return textField;
    }

    private PasswordField createPasswordField() {
        PasswordField passwordField = new PasswordField(15);
        passwordField.setFont(LABEL_FONT);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return passwordField;
    }

    private JButton createButton(String text, Color color, ActionListener actionListener) {
        Button button = new Button(text);
        button.setFont(LABEL_FONT);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.addActionListener(actionListener);
        return button;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginFrame, "Username and Password cannot be empty.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            if (username.isEmpty()) {
                usernameField.requestFocus();
            } else {
                passwordField.requestFocus();
            }
            return;
        }

        // Create and show loading dialog
        JDialog loadingDialog = createLoadingDialog();
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws InterruptedException {
                // Simulated delay, replace with actual validation logic
                Thread.sleep(1000);
                return loginVM.validateLogin(username, password);
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    if (get()) {
                        new MainUI(new MainVM()); // Transition to MainUI
                        loginFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Invalid Username or Password",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        usernameField.setText("");
                        passwordField.setText("");
                        usernameField.requestFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(loginFrame, "An error occurred during login",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
        worker.execute();
    }

    private JDialog createLoadingDialog() {
        JDialog loadingDialog = new JDialog(loginFrame, "Loading", true);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setSize(200, 150);
        loadingDialog.setLocationRelativeTo(loginFrame);

        ImageIcon loadingIcon = new ImageIcon(getClass().getResource("/loading.gif"));
        JLabel loadingLabel = new JLabel("Validating, please wait...", loadingIcon, SwingConstants.CENTER);
        loadingLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        loadingLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        loadingDialog.add(loadingLabel);
        loadingDialog.setUndecorated(true);
        return loadingDialog;
    }
}
class Button extends JButton { //Rounded Buttons
    public Button(String text) {
        super(text);
        setContentAreaFilled(false);
        this.setBorderPainted(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.setColor(getBackground());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
}
class TextField extends JTextField { //Rounded Text Field
    public TextField(int columns) {
        super(columns);
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        g2.dispose();
        super.paintComponent(g);
    }
    @Override
    public void setBorder(Border border) {
    }
}
class PasswordField extends JPasswordField { //Rounded Password Field
    public PasswordField(int columns) {
        super(columns);
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        g2.dispose();
        super.paintComponent(g);
    }
    @Override
    public void setBorder(Border border) {
    }
}