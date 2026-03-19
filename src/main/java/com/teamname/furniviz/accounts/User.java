package com.teamname.furniviz.accounts;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private String userId;
    private String email;
    private String hashedPassword;
    private String firstName;
    private String lastName;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private boolean isActive;

    // Constructor for creating new user (before registration)
    public User(String email, String hashedPassword, String firstName, String lastName) {
        this.userId = UUID.randomUUID().toString();
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = Role.USER;
        this.createdAt = LocalDateTime.now();
        this.lastLogin = null;
        this.isActive = true;
    }

    // Constructor for loading from database
    public User(String userId, String email, String hashedPassword, String firstName,
                String lastName, Role role, LocalDateTime createdAt,
                LocalDateTime lastLogin, boolean isActive) {
        this.userId = userId;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.isActive = isActive;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getHashedPassword() { return hashedPassword; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Role getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public boolean isActive() { return isActive; }

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }
    public void setRole(Role role) { this.role = role; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public void setActive(boolean active) { isActive = active; }

    // Helper methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }
}
