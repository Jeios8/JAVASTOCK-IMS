package main.java.com.javastock.utils;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Utility class for preloading and caching database data.
 * <p>
 * This class is designed to optimize database operations by fetching and storing
 * frequently used data sets in memory, reducing repetitive queries to the database.
 * </p>
 */
public class DataPreloader {

    // Cache to store preloaded data, organized by labels
    private static final Map<String, List<String>> cachedData = new HashMap<>();
    private static boolean isPreloaded = false; // Ensures data is loaded only once

    /**
     * Preloads multiple data sets from the database into memory.
     *
     * @param queries A map where the key is a label (e.g., "categories")
     *                and the value is the SQL query to fetch the data.
     */
    public static void preloadData(Map<String, String> queries) {
        if (isPreloaded) return; // Prevent duplicate loading

        System.out.println("Preloading selected data...");

        for (Map.Entry<String, String> entry : queries.entrySet()) {
            String label = entry.getKey();
            String query = entry.getValue();
            cachedData.put(label, fetchData(query)); // Store fetched data in the cache
        }

        isPreloaded = true; // Mark as preloaded to prevent subsequent calls
        System.out.println("Selected data preloaded successfully.");
    }

    /**
     * Fetches data from the database for a specific query.
     *
     * @param query The SQL query to execute.
     * @return A list of results where each entry corresponds to the first column of a row.
     */
    private static List<String> fetchData(String query) {
        List<String> results = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Iterate through the result set and collect the first column values
            while (rs.next()) {
                results.add(rs.getString(1));
            }
        } catch (SQLException e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Retrieves preloaded data by its label.
     *
     * @param label The key under which the data was preloaded (e.g., "categories").
     * @return An array of cached data or an empty array if the label is not found.
     */
    public static String[] getData(String label) {
        // Return cached data as an array or an empty array if the label is not found
        return cachedData.getOrDefault(label, new ArrayList<>()).toArray(new String[0]);
    }
}