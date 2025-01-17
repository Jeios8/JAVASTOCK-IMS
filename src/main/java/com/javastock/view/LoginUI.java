package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.LoginVM;

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
    }

    // ActionListener for login button
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (loginVM.validateLogin(username, password)) {
                JOptionPane.showMessageDialog(loginFrame, "Login Successful!");
                loginFrame.dispose(); // Close the login frame
                // Transition to MainUI (or next screen)
                new DashboardUI(); // Assuming MainUI exists
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}