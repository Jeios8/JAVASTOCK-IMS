package main.java.com.javastock.viewmodel;

import main.java.com.javastock.model.User;
import main.java.com.javastock.utils.DatabaseConnector;
import main.java.com.javastock.utils.Hasher;

public class LoginVM {

    public boolean validateLogin(String username, String password) {
        User fetchedUser = DatabaseConnector.getUserByUsername(username);
        return fetchedUser != null && Hasher.verifyPassword(password, fetchedUser.getPasswordHash());
    }
}
