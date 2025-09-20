package com.greenscan.enums;

public enum UserRole {
    END_USER("End User"),
    VENDOR("Vendor"),
    PICKUP_ASSISTANT("Pickup Assistant"),
    ADMIN("Admin"),
    NGO("NGO"),
    ADS_COMPANY("Ads Company");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
