package main.java.com.javastock;

import main.java.com.javastock.view.LoginUI;
import main.java.com.javastock.viewmodel.LoginVM;
import main.java.com.javastock.view.MainUI;
import main.java.com.javastock.viewmodel.MainVM;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new LoginUI(new LoginVM()));
//        SwingUtilities.invokeLater(() -> new MainUI(new MainVM()));
    }
}
