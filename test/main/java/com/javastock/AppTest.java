// App.java (Main Entry Point)
package main.java.com.javastock;

import main.java.com.javastock.viewmodel.LoginVM;
import main.java.com.javastock.view.LoginUI;

public class AppTest {
    public static void main(String[] args) {
        // Initialize LoginVM and launch the Login UI
        LoginVM loginVM = new LoginVM();
        new LoginUI(loginVM);
    }
}
