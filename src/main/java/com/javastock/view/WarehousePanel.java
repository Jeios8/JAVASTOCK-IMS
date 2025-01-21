package main.java.com.javastock.view;

import main.java.com.javastock.viewmodel.WarehouseVM;

import javax.swing.*;
import java.awt.*;

public class WarehousePanel extends JPanel {
    private static final Font LABEL_FONT = new Font("Helvetica", Font.PLAIN, 14);
    private WarehouseVM warehouseVM;
    private static final Font TITLE_FONT = new Font("Helvetica", Font.BOLD, 16);
    String[] warehouseName = {
            "Store 1",
            "Store 2",
            "Store 3",
    };

    public WarehousePanel(WarehouseVM warehouseVM) {
        this.warehouseVM = warehouseVM;
        setLayout(new BorderLayout());

        //Add components to main Panel
        add(createPadding(0,20), BorderLayout.NORTH);
        add(createPadding(20,0), BorderLayout.EAST);
        add(createPadding(20,0), BorderLayout.WEST);
        add(createPadding(0,100), BorderLayout.SOUTH);
        add(contentPanel(), BorderLayout.CENTER);
    }
    private JPanel createPadding(int width, int height) {
        JPanel padding = new JPanel();
        padding.setBackground(Color.LIGHT_GRAY);
        padding.setPreferredSize(new Dimension(width, height));
        return padding;
    }
    private JPanel contentPanel(){
        //Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Manage Store");
        titleLabel.setFont(TITLE_FONT);

        contentPanel.add(titleLabel);
        contentPanel.add(storeInfoContainer());
        return contentPanel;
    }
    private JPanel storeInfoContainer(){
        JPanel storeContainer = new JPanel(); //Container for store data panels
        storeContainer.setLayout(new BoxLayout(storeContainer, BoxLayout.Y_AXIS));
        storeContainer.setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 80));
        JScrollPane scrollPane = new JScrollPane(storeContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        storeContainer.add(storeInfo(0));
        storeContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        storeContainer.add(storeInfo(1));
        storeContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        storeContainer.add(storeInfo(2));

        //revalidate and repaint storeContainer to reflect added stores
        storeContainer.revalidate();
        storeContainer.repaint();

        return storeContainer;
    }
    private JPanel storeInfo(int i){
        JPanel storeInfo = new JPanel();
        storeInfo.setLayout(new BorderLayout(10,0));
        storeInfo.setMaximumSize(new Dimension(800, 120));
        storeInfo.setBackground(Color.white);
        storeInfo.setForeground(Color.BLACK);
        storeInfo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        storeInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel storeNamePanel = new JPanel();
        storeNamePanel.setBackground(Color.LIGHT_GRAY);
        storeNamePanel.setForeground(Color.BLACK);
        storeNamePanel.setPreferredSize(new Dimension(200, 120));
        storeNamePanel.add(storeName(warehouseName[i]));

        JPanel storeDetailsPanel = new JPanel();
        storeDetailsPanel.setBackground(Color.WHITE);
        storeDetailsPanel.setForeground(Color.BLACK);
        storeDetailsPanel.add(storeDetails());


        storeInfo.add(storeNamePanel,BorderLayout.WEST);
        storeInfo.add(storeDetailsPanel,BorderLayout.CENTER);
        return storeInfo;
    }
    private JLabel storeName(String name){
        JLabel storeName = new JLabel(name);
        storeName.setFont(TITLE_FONT);
        return storeName;
    }
    private JTextArea storeDetails(){
        JTextArea storeDetails = new JTextArea(4,20);
        storeDetails.setText("STORE NAME \nAddress \nContact \nPhone Number");
        storeDetails.setFont(LABEL_FONT);
        storeDetails.setWrapStyleWord(true);
        storeDetails.setLineWrap(true);
        storeDetails.setEditable(false);
        return storeDetails;
    }
    private JButton addStore(){
        JButton addButton = new JButton("Add Store");
        addButton.setFont(LABEL_FONT);
        addButton.setBackground(Color.BLUE);
        addButton.setForeground(Color.WHITE);
        return addButton;
    }
}
