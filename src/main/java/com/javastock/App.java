package main.java.com.javastock;

import com.formdev.flatlaf.FlatLightLaf;
import main.java.com.javastock.view.LoginUI;
import main.java.com.javastock.view.ProductInfoPanel;
import main.java.com.javastock.viewmodel.LoginVM;
import main.java.com.javastock.view.MainUI;
import main.java.com.javastock.viewmodel.MainVM;
import main.java.com.javastock.viewmodel.ProductVM;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

         SwingUtilities.invokeLater(() -> new LoginUI(new LoginVM()));
//        SwingUtilities.invokeLater(() -> new MainUI(new MainVM()));
    }
}
