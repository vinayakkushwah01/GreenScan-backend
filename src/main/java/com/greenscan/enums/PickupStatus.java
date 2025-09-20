package com.greenscan.enums;

public enum PickupStatus {
    ASSIGNED("Assigned"),
    EN_ROUTE("En Route"),
    ARRIVED("Arrived"),
    IN_PROGRESS("In Progress"),
    COLLECTED("Collected"),
    COMPLETED("Completed"),
    FAILED("Failed");

    private final String displayName;

    PickupStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
