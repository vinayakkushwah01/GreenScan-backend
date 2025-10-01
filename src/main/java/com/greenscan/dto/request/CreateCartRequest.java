package com.greenscan.dto.request;

import lombok.Data;

@Data
public class CreateCartRequest {
    private Long vendorId; // Optional - for manual vendor selection
    private String pickupInstructions;
    
    // Pickup address (if different from user's default)
    private String pickupAddressLine1;
    private String pickupAddressLine2;
    private String pickupCity;
    private String pickupPincode;
    private Double pickupLatitude;
    private Double pickupLongitude;
}
