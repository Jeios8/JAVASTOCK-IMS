package main.java.com.javastock.viewmodel;

import main.java.com.javastock.model.User;
import main.java.com.javastock.utils.DatabaseConnector;
import main.java.com.javastock.utils.Hasher;

public class LoginVM {

    /**
     * Validates the login credentials.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return true if the credentials are valid, false otherwise.
     */
    public boolean validateLogin(String username, String password) {
        User fetchedUser = DatabaseConnector.getUserByUsername(username);
        if (fetchedUser != null) {
            // Use Hasher to verify the password against the stored hash
            return Hasher.verifyPassword(password, fetchedUser.getPasswordHash());
        }
        return false;
    }
}
