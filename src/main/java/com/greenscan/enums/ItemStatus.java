package com.greenscan.enums;

public enum ItemStatus {
    PENDING_USER_CONFIRMATION("Pending User Confirmation"),
    USER_CONFIRMED("User Confirmed"),
    USER_EDITED("User Edited"),
    PENDING_VENDOR_VERIFICATION("Pending Vendor Verification"),
    VERIFIED("Verified"),
    REJECTED("Rejected");

    private final String displayName;

    ItemStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
