package main.java.com.javastock;

import main.java.com.javastock.model.User;
import main.java.com.javastock.viewmodel.LoginVM;
import main.java.com.javastock.view.LoginUI;

public class App {
    public static void main(String[] args) {
        // Create a User object with test credentials
        User testUser = new User("admin", "admin");

        // Pass User to LoginVM
        LoginVM loginVM = new LoginVM(testUser);

        // Pass LoginVM to LoginUI
        new LoginUI(loginVM);
    }
}