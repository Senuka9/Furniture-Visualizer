package com.teamname.furniviz.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.teamname.furniviz.accounts.User;
import com.teamname.furniviz.accounts.UserStore;
import com.teamname.furniviz.auth.exceptions.AuthenticationException;
import com.teamname.furniviz.auth.exceptions.InvalidCredentialsException;
import com.teamname.furniviz.auth.exceptions.RegistrationException;
import com.teamname.furniviz.common.util.ValidationUtil;

public class AuthService {
    private UserStore userStore;

    public AuthService() {
        this.userStore = new UserStore();
    }

    public User login(String email, String password) throws AuthenticationException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidCredentialsException("Email is required");
        }
        if (password == null || password.isEmpty()) {
            throw new InvalidCredentialsException("Password is required");
        }

        email = email.trim();

        User user = userStore.findByEmail(email);
        if (user == null) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!verifyPassword(password, user.getHashedPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new AuthenticationException("Account has been deactivated");
        }

        user.updateLastLogin();
        userStore.updateUser(user);

        System.out.println("[OK] User logged in: " + email);
        return user;
    }

    public User register(String email, String password, String firstName, String lastName)
            throws RegistrationException {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new RegistrationException("Email is required");
            }
            if (password == null || password.isEmpty()) {
                throw new RegistrationException("Password is required");
            }
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new RegistrationException("First name is required");
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new RegistrationException("Last name is required");
            }

            email = email.trim();
            firstName = firstName.trim();
            lastName = lastName.trim();

            if (!ValidationUtil.isValidEmail(email)) {
                throw new RegistrationException("Invalid email format");
            }

            if (!isPasswordStrong(password)) {
                throw new RegistrationException(
                    "Password must be at least 8 characters with uppercase, lowercase, number, and special character"
                );
            }

            if (userStore.emailExists(email)) {
                throw new RegistrationException("Email already registered");
            }

            String hashedPassword = hashPassword(password);

            User newUser = userStore.registerUser(email, hashedPassword, firstName, lastName);
            System.out.println("[OK] User registered: " + email);
            return newUser;
        } catch (Exception e) {
            if (e instanceof RegistrationException) {
                throw (RegistrationException) e;
            }
            throw new RegistrationException("Registration failed: " + e.getMessage(), e);
        }
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified;
        } catch (Exception e) {
            System.err.println("[ERROR] Error verifying password: " + e.getMessage());
            return false;
        }
    }

    public boolean isPasswordStrong(String password) {
        if (password == null) return false;
        if (password.length() < 6) return false;

        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");

        return hasLetter && hasDigit;
    }

    public int getPasswordStrength(String password) {
        if (password == null || password.isEmpty()) return 0;

        int strength = 0;

        if (password.length() >= 6) strength += 30;
        if (password.length() >= 10) strength += 20;
        if (password.length() >= 12) strength += 10;

        if (password.matches(".*[a-zA-Z].*")) strength += 20;
        if (password.matches(".*\\d.*")) strength += 20;

        return Math.min(strength, 100);
    }
}
