package com.greenscan.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class VendorRegistrationRequest {
    @NotBlank(message = "Business name is required")
    private String businessName;
    
    private String businessRegistrationNumber;
    private String gstNumber;
    private String panNumber;
    
    @NotBlank(message = "Service cities are required")
    private String serviceCities; // Comma-separated
    
    private Integer serviceRadiusKm = 10;
    private Integer dailyPickupCapacity = 20;
    private Double maxWeightPerPickupKg = 50.0;
    
    private String workingStartTime = "08:00";
    private String workingEndTime = "18:00";
    private String workingDays = "Monday,Tuesday,Wednesday,Thursday,Friday,Saturday";
 
}
