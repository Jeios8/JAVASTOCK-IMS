// Updated LoginVM.java
package main.java.com.javastock.viewmodel;

import main.java.com.javastock.model.User;
import main.java.com.javastock.utils.DatabaseConnector;

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
            return fetchedUser.getPasswordHash().equals(password);
        }
        return false;
    }
}
