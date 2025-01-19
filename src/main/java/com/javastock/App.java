package main.java.com.javastock;

import main.java.com.javastock.view.LoginUI;
import main.java.com.javastock.view.MainUI;
import main.java.com.javastock.viewmodel.LoginVM;
import main.java.com.javastock.viewmodel.MainVM;

public class App {
    public static void main(String[] args) {
        new LoginUI(new LoginVM());
    }
}
