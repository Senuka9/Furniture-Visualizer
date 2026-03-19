package com.teamname.furniviz.common.util;

public class ValidationUtil {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._+%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }

        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");

        return hasLetter && hasDigit;
    }

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("^[a-zA-Z\\s\\-']+$") && name.trim().length() >= 2;
    }

    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim();
    }

    public static String getEmailErrorMessage(String email) {
        if (email == null || email.isEmpty()) {
            return "Email is required";
        }
        if (!isValidEmail(email)) {
            return "Invalid email format";
        }
        return null;
    }

    public static String getPasswordErrorMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters";
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            return "Must contain a letter (a-z or A-Z)";
        }
        if (!password.matches(".*\\d.*")) {
            return "Must contain a number (0-9)";
        }
        return null;
    }

    public static String getNameErrorMessage(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Name is required";
        }
        if (name.trim().length() < 2) {
            return "Name must be at least 2 characters";
        }
        if (!isValidName(name)) {
            return "Name can only contain letters, spaces, hyphens, and apostrophes";
        }
        return null;
    }
}
