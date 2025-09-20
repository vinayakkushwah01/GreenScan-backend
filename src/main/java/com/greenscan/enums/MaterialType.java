package com.greenscan.enums;

public enum MaterialType {
    PLASTIC("Plastic", 2.0),
    METAL("Metal", 5.0),
    PAPER("Paper", 1.5),
    GLASS("Glass", 3.0),
    ELECTRONIC("Electronic", 10.0),
    ORGANIC("Organic", 0.5),
    TEXTILE("Textile", 1.0),
    MIXED("Mixed", 1.0);

    private final String displayName;
    private final double coinsPerKg;

    MaterialType(String displayName, double coinsPerKg) {
        this.displayName = displayName;
        this.coinsPerKg = coinsPerKg;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getCoinsPerKg() {
        return coinsPerKg;
    }
}
