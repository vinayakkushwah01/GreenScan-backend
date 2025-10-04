package com.greenscan.dto.request;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.greenscan.entity.MainUser;
import com.greenscan.entity.VendorProfile;
import com.greenscan.enums.ApprovalStatus;

import lombok.Data;
@Data
public class VendorProfileRequest {
    private Long mainUserId; // ID of the MainUser associated with this vendor
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
