package org.forum.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtil {
    
    /**
     * Hash a password using SHA256
     * @param password the plain text password
     * @return the SHA256 hash of the password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            
            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Verify a password against its hash
     * @param password the plain text password to verify
     * @param hash the stored hash to compare against
     * @return true if the password matches the hash, false otherwise
     */
    public static boolean verifyPassword(String password, String hash) {
        String passwordHash = hashPassword(password);
        return passwordHash.equals(hash);
    }
} 