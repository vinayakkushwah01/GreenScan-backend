package com.greenscan.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.greenscan.entity.MainUser;
import com.greenscan.entity.VendorProfile;

import lombok.Data;
@Data
public class VendorProfileResponse {
    private String businessName;
    private String approvalStatus;
    private List<String> serviceCities;
    private String workingHours;
    private List<String> workingDays;
    private Integer dailyPickupCapacity;
    private BigDecimal averageRating;
    private Integer totalPickupsCompleted;
    private BigDecimal totalWeightCollectedKg;
    private UserResponse user;
    private String vendorId;
    private String address;
    private Double latitude;
    private Double longitude;
    private String contactNumber;
    private String email;


     public static VendorProfileResponse fromEntity(MainUser user, VendorProfile vendor) {
        if (vendor == null || user == null) {
            return null;
        }
        
        VendorProfileResponse response = new VendorProfileResponse();

        response.setBusinessName(vendor.getBusinessName());
        response.setApprovalStatus(vendor.getApprovalStatus() != null
                ? vendor.getApprovalStatus().name()
                : null);
        response.setServiceCities(vendor.getServiceCitiesList());

        // Working hours combined
        if (vendor.getWorkingStartTime() != null && vendor.getWorkingEndTime() != null) {
            response.setWorkingHours(vendor.getWorkingStartTime() + " - " + vendor.getWorkingEndTime());
        }

        response.setWorkingDays(vendor.getWorkingDaysList());
        response.setDailyPickupCapacity(vendor.getDailyPickupCapacity());
        response.setAverageRating(vendor.getAverageRating());
        response.setTotalPickupsCompleted(vendor.getTotalPickupsCompleted());
        response.setTotalWeightCollectedKg(vendor.getTotalWeightCollectedKg());

        // User Info
        response.setUser(UserResponse.fromMainUser(user));
        response.setVendorId(vendor.getId() != null ? vendor.getId().toString() : null);

        // Additional fields from MainUser (assuming MainUser has these fields)
        response.setAddress(user.getAddressLine1()+", "+user.getAddressLine2()+", "+user.getCity());
        response.setLatitude(user.getLatitude());
        response.setLongitude(user.getLongitude());
        response.setContactNumber(user.getMobile());
        response.setEmail(user.getEmail());

        return response;
    }
}
