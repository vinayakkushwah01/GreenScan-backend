package com.greenscan.enums;

public enum MaterialType {
    // PLASTIC("Plastic", 2.0),
    // METAL("Metal", 5.0),
    // PAPER("Paper", 1.5),
    // GLASS("Glass", 3.0),
    // ELECTRONIC("Electronic", 10.0),
    // ORGANIC("Organic", 0.5),
    // TEXTILE("Textile", 1.0),
    // MIXED("Mixed", 1.0);

    // private final String displayName;
    // private final double coinsPerKg;

    // MaterialType(String displayName, double coinsPerKg) {
    //     this.displayName = displayName;
    //     this.coinsPerKg = coinsPerKg;
    // }

    // public String getDisplayName() {
    //     return displayName;
    // }

    // public double getCoinsPerKg() {
    //     return coinsPerKg;
    // }
       // --- Fiber / Paper Products (Market Price ~15-18 INR/kg) ---
    CARDBOARD("Corrugated Cardboard", 144.0),     // (18.0 * 0.8) * 10
    MIXED_PAPER("Mixed Paper and Paperboard", 120.0), // (15.0 * 0.8) * 10

    // --- Plastics (Market Price ~10-60 INR/kg) ---
    PET_PLASTIC_1("PET Plastic (#1 Bottles)", 320.0),  // (40.0 * 0.8) * 10 - High Value
    HDPE_PLASTIC_2("HDPE Plastic (#2 Jugs/Tubs)", 480.0), // (60.0 * 0.8) * 10 - High Value
    PP_PLASTIC_5("PP Plastic (#5 Containers)", 280.0),  // (35.0 * 0.8) * 10
    LDPE_PLASTIC_4("LDPE Plastic (#4 Film/Bags)", 240.0),  // (30.0 * 0.8) * 10
    PVC_PLASTIC_3("PVC Plastic (#3 Pipes/Wraps)", 160.0),  // (20.0 * 0.8) * 10
    PS_PLASTIC_6("PS Plastic (#6 Foam/Cups)", 80.0),   // (10.0 * 0.8) * 10 - Low Value
    OTHER_PLASTIC_7("Other Plastics (Mixed #7)", 120.0),  // (15.0 * 0.8) * 10 - General Mixed Plastic

    // --- Metals (Market Price ~22-150 INR/kg) ---
    ALUMINUM("Aluminum Cans/Foil", 1200.0),    // (150.0 * 0.8) * 10 - Highest Value
    STEEL_TIN("Steel and Tin Cans", 176.0),     // (22.0 * 0.8) * 10
    SCRAP_METAL("General Ferrous Scrap Metal", 160.0), // (20.0 * 0.8) * 10 - General Metal Fallback

    // --- Glass (Market Price ~8 INR/kg) ---
    GLASS_CONTAINER("Glass Bottles and Jars", 64.0), // (8.0 * 0.8) * 10

    // --- Special/Zero-Value Items ---
    E_WASTE("Electronic Waste", 260.0), // (20.0 * 0.8) * 10 - Based on mixed e-waste value
    TEXTILE("Fabric, Clothing, or Textiles", 32.0), // (4.0 * 0.8) * 10 - Nominal value for fiber recycling
    ORGANIC("Organic Matter (Compost)", 0.0); // Zero value for collection (may incur costs)

    private final String displayName;
    private final double coinsPerKg;

    MaterialType(String displayName, double coinsPerKg) {
        this.displayName = displayName;
        this.coinsPerKg = coinsPerKg;
    }

    /**
     * @return A user-friendly name for the material type.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return The payout value a user receives in GreenCoins per kilogram.
     */
    public double getCoinsPerKg() {
        return coinsPerKg;
    }

    /**
     * Converts a GreenCoin value to its equivalent in Indian Rupees (INR).
     * @param coins The value in GreenCoins.
     * @return The value in INR.
     */
    public static double toINR(double coins) {
        return coins / 10.0;
    }
}
