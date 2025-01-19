package main.java.com.javastock;

import main.java.com.javastock.view.LoginUI;
import main.java.com.javastock.viewmodel.LoginVM;
import main.java.com.javastock.viewmodel.MainVM;
import main.java.com.javastock.view.MainUI;

public class App {
    public static void main(String[] args) {
        LoginVM viewModel = new LoginVM();
        new LoginUI(viewModel);
    }
}
