package com.greenscan.enums;

public enum NotificationType {
    CART_UPDATE("Cart Update"),
    PICKUP_SCHEDULED("Pickup Scheduled"),
    COINS_EARNED("Coins Earned"),
    VENDOR_ASSIGNMENT("Vendor Assignment"),
    ITEM_VERIFICATION("Item Verification"),
    SYSTEM_ALERT("System Alert"),
    PROMOTIONAL("Promotional"),
    NGO_CAMPAIGN("NGO Campaign");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
