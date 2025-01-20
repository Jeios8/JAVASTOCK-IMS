package main.java.com.javastock.utils;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class DataPreloader {
    private static final Map<String, List<String>> cachedData = new HashMap<>();
    private static boolean isPreloaded = false; // Prevents duplicate loading

    /**
     * General method to preload multiple data sets at once.
     * @param queries A map where the key is a label (e.g., "categories") and the value is an SQL query.
     */
    public static void preloadData(Map<String, String> queries) {
        if (isPreloaded) return; // Prevents duplicate loading

        System.out.println("Preloading selected data...");

        for (Map.Entry<String, String> entry : queries.entrySet()) {
            String label = entry.getKey();
            String query = entry.getValue();
            cachedData.put(label, fetchData(query)); // Store fetched data in cache
        }

        isPreloaded = true;
        System.out.println("Selected data preloaded successfully.");
    }

    /**
     * Fetches data for a given SQL query.
     */
    private static List<String> fetchData(String query) {
        List<String> results = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Retrieves preloaded data based on a label.
     * @param label The key under which the data was preloaded (e.g., "categories").
     * @return The cached data as an array, or an empty array if not found.
     */
    public static String[] getData(String label) {
        return cachedData.getOrDefault(label, new ArrayList<>()).toArray(new String[0]);
    }
}
