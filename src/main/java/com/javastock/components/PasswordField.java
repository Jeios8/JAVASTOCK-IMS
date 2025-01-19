package main.java.com.javastock.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class PasswordField extends JPasswordField {
    public PasswordField(int columns) {
        super(columns);
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        g2.dispose();
        super.paintComponent(g);
    }
    @Override
    public void setBorder(Border border) {
    }
}
