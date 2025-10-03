package com.greenscan.dto.request;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public class VendorProfileRequest {
    private String userId; // ID of the MainUser associated with this vendor
    private String email; // for convenience, to identify user if needed
    private String businessName;

    // Optional (user may provide, else defaults in entity will be used)
    private String businessRegistrationNumber;
    private String gstNumber;
    private String panNumber;

    private String serviceCities; // comma-separated string OR
    private List<String> serviceCitiesList; // alt for cleaner API

    private Integer serviceRadiusKm;

    private LocalTime workingStartTime;
    private LocalTime workingEndTime;
    private List<String> workingDays;

    private Integer dailyPickupCapacity;
    private BigDecimal maxWeightPerPickupKg;

    // private BigDecimal platformCommissionRate;
    private BigDecimal paymentLimit;
}
