package main.java.com.javastock.viewmodel;

import main.java.com.javastock.model.User;
import main.java.com.javastock.repository.UserRepository;
import main.java.com.javastock.utils.DataPreloader;
import main.java.com.javastock.utils.Hasher;

/**
 * ViewModel responsible for user authentication and login validation.
 */
public class LoginVM {
    private final UserRepository userRepository;

    /**
     * Constructor initializes the user repository.
     */
    public LoginVM() {
        this.userRepository = new UserRepository();
    }

    /**
     * Validates user login and preloads necessary data upon success.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
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

        // Retrieve preloaded users
        String[] preloadedUsers = DataPreloader.getData("users");

        // Find user in preloaded cache
        String userHash = null;
        for (String userEntry : preloadedUsers) {
            String[] parts = userEntry.split("::");
            if (parts.length == 2 && parts[0].equals(username)) {
                userHash = parts[1];
                break;
            }
        }

        if (userHash != null) {
            System.out.println("User found in preloaded cache: " + username);
            boolean isPasswordCorrect = Hasher.verifyPassword(password, userHash);
            if (isPasswordCorrect) {
                System.out.println("Login successful!");
                return true;
            } else {
                System.err.println("ERROR: Invalid password.");
                return false;
            }
        }

        System.out.println("User not found in cache. Fetching from database...");
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
            return true;
        } else {
            System.err.println("ERROR: Invalid password.");
            return false;
        }
    }
}
