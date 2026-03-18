package com.teamname.furniviz.auth;

import com.teamname.furniviz.accounts.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class Session {
    private static Session instance;
    private static final int SESSION_TIMEOUT_MINUTES = 30;

    private User currentUser;
    private String sessionToken;
    private LocalDateTime createdAt;
    private LocalDateTime lastActivityAt;

    private Session() {
        this.currentUser = null;
        this.sessionToken = null;
        this.createdAt = null;
        this.lastActivityAt = null;
    }

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setUser(User user) {
        this.currentUser = user;
        this.sessionToken = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
        System.out.println("[OK] Session created for user: " + (user != null ? user.getEmail() : "null"));
    }

    public User getCurrentUser() {
        if (isSessionValid()) {
            updateActivity();
            return currentUser;
        }
        return null;
    }

    public boolean isLoggedIn() {
        return currentUser != null && isSessionValid();
    }

    public boolean isSessionValid() {
        if (currentUser == null) {
            return false;
        }

        if (lastActivityAt == null) {
            return false;
        }

        long minutesSinceActivity = java.time.temporal.ChronoUnit.MINUTES
                .between(lastActivityAt, LocalDateTime.now());

        if (minutesSinceActivity > SESSION_TIMEOUT_MINUTES) {
            System.out.println("[INFO] Session expired due to inactivity");
            logout();
            return false;
        }

        return true;
    }

    public void updateActivity() {
        this.lastActivityAt = LocalDateTime.now();
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("[OK] User logged out: " + currentUser.getEmail());
        }
        this.currentUser = null;
        this.sessionToken = null;
        this.createdAt = null;
        this.lastActivityAt = null;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
