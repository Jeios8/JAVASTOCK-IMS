package main.java.com.javastock;

import main.java.com.javastock.view.LoginUI;
import main.java.com.javastock.viewmodel.LoginVM;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI(new LoginVM()));
    }
}
