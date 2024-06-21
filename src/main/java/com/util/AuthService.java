package com.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.swing.JOptionPane;

public class AuthService {

    private static final String authFile = FileHandler.getAuthFilePath();

//    public static boolean validateUser(String name, String password) {
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(authFile))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(":");
//                if (parts.length == 2 && parts[0].equals(name) && parts[1].equals(password)) {
//                    return true;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

//    public static void registerUser(String name, String password) {
//        // Check if user is already registered
//
//        try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(authFile, true))) {
//            writer.write(name + ":" + password);
//            writer.newLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public static boolean isAnyUserExist() {
//        boolean foundFirstUser = false;
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(authFile))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Split the line into parts (assuming format is "username:password")
//                String[] parts = line.split(":");
//                if (parts.length == 2) {
//                    if (!foundFirstUser) {
//                        foundFirstUser = true; // Found the first user
//                    } else {
//                        // Found another user, return false
//                        return false;
//                    }
//                } else {
//                    // Invalid format, return false
//                    return false;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return foundFirstUser; // Return true only if exactly one user was found
//    }
//
//    public static boolean isThisUserExist(String name) {
//        try (BufferedReader reader = new BufferedReader(new FileReader(authFile))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(":");
//                if (parts.length > 0 && parts[0].equals(name)) {
//                    return true;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
    public static boolean isAnyUserExist() {
        boolean foundFirstUser = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(authFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into parts (assuming format is "username:hashedPassword:salt")
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    if (!foundFirstUser) {
                        foundFirstUser = true; // Found the first user
                    } else {
                        // Found another user, return false
                        return false;
                    }
                } else {
                    // Invalid format, return false
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return foundFirstUser; // Return true only if exactly one user was found
    }

    public static boolean isThisUserExist(String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader(authFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3 && parts[0].equals(name)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean verifyUser(String name, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(authFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3 && parts[0].equals(name)) {
                    // Extract hashed password and salt
                    String storedHashedPassword = parts[1];
                    byte[] storedSalt = Base64.getDecoder().decode(parts[2]);

                    // Hash input password with stored salt
                    String hashedPassword = hashPassword(password, storedSalt);

                    // Compare hashed passwords
                    if (hashedPassword.equals(storedHashedPassword)) {
                        return true; // Passwords match, user authenticated
                    } else {
                        return false; // Passwords don't match
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User not found or error occurred
    }

    public static void registerUser(String name, String password) {
        // Generate salt
        byte[] salt = generateSalt();

        // Hash password with salt
        String hashedPassword = hashPassword(password, salt);

        // Save username, hashed password, and salt to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(authFile, true))) {
            writer.write(name + ":" + hashedPassword + ":" + Base64.getEncoder().encodeToString(salt));
            writer.newLine();
            System.out.println("User registered successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private static String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
