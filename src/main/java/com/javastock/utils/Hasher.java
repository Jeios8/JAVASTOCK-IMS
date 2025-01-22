package main.java.com.javastock.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for securely hashing and verifying passwords using the BCrypt algorithm.
 * <p>
 * This class provides methods to:
 * - Hash plain-text passwords securely.
 * - Verify plain-text passwords against a previously hashed password.
 * </p>
 */
public class Hasher {

    // Cost factor for the BCrypt algorithm; higher values increase hashing time for better security
    private static final int BCRYPT_COST = 12;

    /**
     * Hashes a plain-text password using the BCrypt algorithm.
     *
     * @param plainPassword The plain-text password to be hashed.
     * @return The hashed password as a BCrypt string.
     */
    public static String hashPassword(String plainPassword) {
        // Generate a salt and hash the password
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_COST));
    }

    /**
     * Verifies a plain-text password against a stored BCrypt hash.
     *
     * @param plainPassword The plain-text password provided by the user.
     * @param storedHash    The previously hashed password stored in the database.
     * @return {@code true} if the plain-text password matches the stored hash, {@code false} otherwise.
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        // Compare the plain password with the stored hash
        return BCrypt.checkpw(plainPassword, storedHash);
    }
}
