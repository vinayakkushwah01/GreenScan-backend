package com.greenscan.enums;

public enum TransactionType {
    EARNED("Earned"),
    REDEEMED("Redeemed"),
    DONATED("Donated"),
    REFUNDED("Refunded"),
    PENALTY("Penalty");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
