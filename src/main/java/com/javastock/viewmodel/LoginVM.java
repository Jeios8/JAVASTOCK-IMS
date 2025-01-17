package main.java.com.javastock.viewmodel;

import main.java.com.javastock.model.User;

public class LoginVM {
    private User userModel;

    public LoginVM(User userModel) {
        this.userModel = userModel;
    }

    /**
     * Validates the login credentials.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return true if the credentials are valid, false otherwise.
     */
    public boolean validateLogin(String username, String password) {
        // Fetch user credentials from the model
        String storedUsername = userModel.getUsername();
        String storedPassword = userModel.getPassword();

        // Perform validation (basic example; replace with hashed password check in production)
        return username.equals(storedUsername) && password.equals(storedPassword);
    }
}
