package main.java.com.javastock.viewmodel;

import main.java.com.javastock.model.User;
import main.java.com.javastock.repository.UserRepository;
import main.java.com.javastock.utils.DataPreloader;
import main.java.com.javastock.utils.Hasher;

import java.util.HashMap;
import java.util.Map;

public class LoginVM {
    private final UserRepository userRepository;

    public LoginVM() {
        this.userRepository = new UserRepository();
    }

    /**
     * Validates user login and preloads necessary data upon success.
     * @return true if login is successful, false otherwise.
     */
    public boolean validateLogin(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("ERROR: Username cannot be empty.");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            System.err.println("ERROR: Password cannot be empty.");
            return false;
        }

        try {
            User fetchedUser = userRepository.getUserByUsername(username);

            if (fetchedUser == null) {
                System.err.println("ERROR: User not found.");
                return false;
            }

            if (!fetchedUser.isActive()) {
                System.err.println("ERROR: User account is inactive.");
                return false;
            }

            boolean isPasswordCorrect = Hasher.verifyPassword(password, fetchedUser.getPasswordHash());

            if (isPasswordCorrect) {
                System.out.println("Login successful!");

                // Preload data before navigating to the main UI
                preloadDataAfterLogin();

                return true; // Login success
            } else {
                System.err.println("ERROR: Invalid password.");
                return false;
            }

        } catch (Exception e) {
            System.err.println("ERROR: Unexpected error during login.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Preloads necessary data after successful login.
     */
    private void preloadDataAfterLogin() {
        Map<String, String> preloadedQueries = new HashMap<>();
        preloadedQueries.put("categories", "SELECT category_name FROM categories WHERE is_active = true");
        preloadedQueries.put("suppliers", "SELECT supplier_name FROM suppliers WHERE is_active = true");
        preloadedQueries.put("warehouses", "SELECT warehouse_name FROM warehouses WHERE is_active = true");

        DataPreloader.preloadData(preloadedQueries);
    }
}
