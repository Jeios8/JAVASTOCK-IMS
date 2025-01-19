package main.java.com.javastock.utils;

import org.mindrot.jbcrypt.BCrypt;

public class Hasher {
    private static final int BCRYPT_COST = 12;

    // Hash a password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_COST));
    }

    // Verify password
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        // Check if the stored hash is a valid BCrypt hash
        return BCrypt.checkpw(plainPassword, storedHash);// Invalid hash
    }
}
