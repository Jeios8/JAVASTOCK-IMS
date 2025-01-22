package main.java.com.javastock.viewmodel;

import javax.swing.*;
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainVM {
    private String activeSection;
    private final Map<String, String> sections;
    private final Map<String, Color> sectionColors;

    public MainVM() {
        sections = new LinkedHashMap<>();
        sections.put("Dashboard", "Dashboard Content Loaded");
        sections.put("Inventory", "Inventory Content Loaded");
        sections.put("Reports", "Reports Content Loaded");
        sections.put("Suppliers", "Suppliers Content Loaded");
        sections.put("Orders", "Orders Content Loaded");
        sections.put("Manage Store", "Manage Store Content Loaded");

        sectionColors = new LinkedHashMap<>();
        sectionColors.put("Dashboard", Color.CYAN);
        sectionColors.put("Inventory", Color.YELLOW);
        sectionColors.put("Reports", Color.GREEN);
        sectionColors.put("Suppliers", Color.ORANGE);
        sectionColors.put("Orders", Color.PINK);
        sectionColors.put("Manage Store", Color.MAGENTA);
    }

    public Map<String, String> getSections() {
        return sections;
    }

    public Color getSectionColor(String section) {
        return sectionColors.getOrDefault(section, Color.WHITE);
    }

    public String getActiveSection() {
        return activeSection;
    }

    public void setActiveSection(String section) {
        if (sections.containsKey(section)) {
            this.activeSection = section;
        }
    }
}
