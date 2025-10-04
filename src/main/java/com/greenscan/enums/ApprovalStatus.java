package com.greenscan.enums;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import com.greenscan.dto.request.VendorProfileRequest;
import com.greenscan.entity.MainUser;
import com.greenscan.entity.VendorProfile;

public enum ApprovalStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    SUSPENDED("Suspended");

    private final String displayName;

    ApprovalStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }


     public static VendorProfile toEntity(VendorProfileRequest request, MainUser user) {
        VendorProfile entity = new VendorProfile();

        entity.setUser(user);


        entity.setBusinessName(request.getBusinessName());
        entity.setBusinessRegistrationNumber(request.getBusinessRegistrationNumber());
        entity.setGstNumber(request.getGstNumber());
        entity.setPanNumber(request.getPanNumber());
        
        entity.setServiceRadiusKm(request.getServiceRadiusKm());
        entity.setWorkingStartTime(request.getWorkingStartTime());
        entity.setWorkingEndTime(request.getWorkingEndTime());

        entity.setDailyPickupCapacity(request.getDailyPickupCapacity());
        entity.setMaxWeightPerPickupKg(request.getMaxWeightPerPickupKg());
        entity.setPaymentLimit(request.getPaymentLimit());


        if (request.getServiceCitiesList() != null && !request.getServiceCitiesList().isEmpty()) {
            String cities = request.getServiceCitiesList().stream()
                                    .collect(Collectors.joining(","));
            entity.setServiceCities(cities);
        } else if (request.getServiceCities() != null) {
            entity.setServiceCities(request.getServiceCities());
        }

        // Working Days: Convert List<String> to comma-separated String
        if (request.getWorkingDays() != null && !request.getWorkingDays().isEmpty()) {
            String days = request.getWorkingDays().stream()
                                    .collect(Collectors.joining(","));
            entity.setWorkingDays(days);
        }

        
        entity.setApprovalStatus(ApprovalStatus.PENDING); 
   
        entity.setPendingPaymentAmount(BigDecimal.ZERO);
        entity.setTotalRevenue(BigDecimal.ZERO);
        entity.setTotalPlatformFeePaid(BigDecimal.ZERO);
        entity.setTotalPickupsCompleted(0);
        entity.setAverageRating(BigDecimal.ZERO);
        entity.setTotalRatings(0);

       
        
        return entity;
    }
}
