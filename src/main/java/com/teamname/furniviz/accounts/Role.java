package com.teamname.furniviz.accounts;

public enum Role {
    USER("User", "Standard user with design capabilities"),
    ADMIN("Administrator", "Full system access");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static Role fromString(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (Exception e) {
            return Role.USER;
        }
    }
}
