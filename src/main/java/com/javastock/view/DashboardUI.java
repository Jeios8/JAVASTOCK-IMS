package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.DashboardVM;

import javax.swing.*;
import java.awt.*;

public class DashboardUI {
    private JPanel panel;
    private DashboardVM viewModel;

    public DashboardUI(DashboardVM viewModel) {
        this.viewModel = viewModel;
        panel = new JPanel();
        panel.add(new JLabel(viewModel.getDashboardData(), JLabel.CENTER));
    }

    public JPanel getPanel() {
        return panel;
    }
}
