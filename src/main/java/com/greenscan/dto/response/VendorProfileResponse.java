package com.greenscan.dto.response;

import java.math.BigDecimal;
import java.util.List;

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
}
