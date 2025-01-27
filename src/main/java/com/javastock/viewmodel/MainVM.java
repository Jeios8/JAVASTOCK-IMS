package main.java.com.javastock.viewmodel;

import javax.swing.*;
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ViewModel for managing the main sections of the application.
 * This class maintains section names, corresponding colors, and the active section state.
 */
public class MainVM {
    private String activeSection; // Stores the currently active section
    private final Map<String, String> sections; // Stores section names and their corresponding content
    private final Map<String, Color> sectionColors; // Stores section names and their corresponding UI colors

    /**
     * Constructor initializes sections and their corresponding UI colors.
     */
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

    /**
     * Retrieves the map of sections and their corresponding content descriptions.
     *
     * @return A map containing section names as keys and their descriptions as values.
     */
    public Map<String, String> getSections() {
        return sections;
    }

    /**
     * Retrieves the color associated with a specific section.
     * If the section is not found, returns the default color (WHITE).
     *
     * @param section The section name.
     * @return The color associated with the given section, or WHITE if not found.
     */
    public Color getSectionColor(String section) {
        return sectionColors.getOrDefault(section, Color.WHITE);
    }

    /**
     * Retrieves the currently active section.
     *
     * @return The active section name.
     */
    public String getActiveSection() {
        return activeSection;
    }

    /**
     * Sets the active section if it exists in the predefined sections map.
     * If the section does not exist, no changes are made.
     *
     * @param section The section name to set as active.
     */
    public void setActiveSection(String section) {
        if (sections.containsKey(section)) {
            this.activeSection = section;
        }
    }
}