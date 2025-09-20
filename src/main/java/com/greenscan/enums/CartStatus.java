package com.greenscan.enums;

public enum CartStatus {
    DRAFT("Draft"),
    PENDING_VENDOR_ASSIGNMENT("Pending Vendor Assignment"),
    VENDOR_ASSIGNED("Vendor Assigned"),
    PICKUP_REQUESTED("Pickup Requested"),
    PICKUP_SCHEDULED("Pickup Scheduled"),
    IN_PROGRESS("In Progress"),
    COLLECTED("Collected"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    REJECTED("Rejected");

    private final String displayName;

    CartStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
