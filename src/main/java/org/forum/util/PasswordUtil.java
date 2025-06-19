package org.forum.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.forum.entity.Log;
import org.forum.entity.LogLevels;
import org.forum.repository.LogRepository;

import lombok.AllArgsConstructor;

public class PasswordUtil {

  public static String hashPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = md.digest(password.getBytes());

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

 public static boolean verifyPassword(String password, String hash) {
    String passwordHash = hashPassword(password);
    return passwordHash.equals(hash);
  }
}
