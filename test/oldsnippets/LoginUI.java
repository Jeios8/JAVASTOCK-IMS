package oldsnippets;

import main.java.com.javastock.viewmodel.LoginVM;
import main.java.com.javastock.viewmodel.MainVM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI {
    private JFrame loginFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private LoginVM loginVM; // ViewModel instance for handling business logic

    public LoginUI(LoginVM loginVM) {
        this.loginVM = loginVM;

        loginFrame = new JFrame("JAVASTOCK");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(450, 600);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(mainPanel.getBackground());
        titlePanel.add(Box.createVerticalStrut(50));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("JAVASTOCK");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Inventory Management System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(subtitleLabel);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(mainPanel.getBackground());
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(mainPanel.getBackground());

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(mainPanel.getBackground());
        inputPanel.setLayout(new GridLayout(7, 1, 0, 0));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.BLACK);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.BLACK);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loginButton.setBackground(new Color(173, 216, 230));
        loginButton.setForeground(Color.BLACK);
        loginButton.addActionListener(new LoginButtonListener()); // Attach the listener

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.setBackground(new Color(255, 105, 97));
        exitButton.setForeground(Color.BLACK);
        exitButton.addActionListener(e -> System.exit(0)); // Close the application

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(loginButton);
        inputPanel.add(exitButton);

        // Set the login button as the default button
        loginFrame.getRootPane().setDefaultButton(loginButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        JLabel bottomLabel = new JLabel("Powered by ERR Software Solutions");
        bottomLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        bottomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(Box.createVerticalStrut(50));
        bottomPanel.add(bottomLabel);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        loginFrame.add(mainPanel);
        loginFrame.setVisible(true);

        usernameField.requestFocus();
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Check if username or password is empty
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        loginFrame,
                        "Username and Password cannot be empty.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                if (username.trim().isEmpty()) {
                    usernameField.requestFocus(); // Focus on username field if empty
                } else {
                    passwordField.requestFocus(); // Focus on password field if username is not empty
                }
                return; // Exit the method
            }

            // Create a loading dialog with animation
            JDialog loadingDialog = new JDialog(loginFrame, "Loading", true);
            loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            loadingDialog.setSize(200, 150);
            loadingDialog.setLocationRelativeTo(loginFrame);

            // Load the animated GIF
            ImageIcon loadingIcon = new ImageIcon(getClass().getResource("/old_loading.gif"));
            JLabel loadingLabel = new JLabel("Validating, please wait...", loadingIcon, SwingConstants.CENTER);
            loadingLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            loadingLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

            loadingDialog.add(loadingLabel);
            loadingDialog.setUndecorated(true); // Optional for a cleaner look

            // Run the login process in a background thread
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() {
                    // Simulate database validation delay
                    try {
                        Thread.sleep(1000); // Simulated delay, replace with real login logic
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    return loginVM.validateLogin(username, password);
                }

                @Override
                protected void done() {
                    loadingDialog.dispose(); // Close the loading dialog
                    try {
                        boolean isSuccess = get(); // Get the result from doInBackground
                        if (isSuccess) {
                            // JOptionPane.showMessageDialog(loginFrame, "Login Successful!");
                            // loginFrame.dispose(); // Close the login frame
                            // Transition to MainUI (or next screen)
                            MainVM viewModel = new MainVM();
                            new MainUI(viewModel);
                        } else {
                            JOptionPane.showMessageDialog(loginFrame, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
                            usernameField.setText("");
                            passwordField.setText("");
                            usernameField.requestFocus();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(loginFrame, "An error occurred during login", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            // Show the loading dialog on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                loadingDialog.setVisible(true);
            });

            // Start the worker
            worker.execute();
        }
    }


}