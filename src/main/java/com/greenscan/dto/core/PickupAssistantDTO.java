package com.greenscan.dto.core;

import com.greenscan.dto.base.BaseDTO;
import com.greenscan.enums.ApprovalStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class PickupAssistantDTO extends BaseDTO {
    private UserDTO user;
    private VendorProfileDTO vendor;
    private String assistantCode;
    private ApprovalStatus approvalStatus;
    private String employeeId;
    private Double currentLatitude;
    private Double currentLongitude;
    private LocalDateTime locationUpdatedAt;
    private Boolean isAvailable;
    private Boolean isOnDuty;
    private Long currentCartId;
    private Integer totalPickupsCompleted;
    private BigDecimal totalDistanceCoveredKm;
    private BigDecimal averageRating;
    private Integer totalRatings;
    private BigDecimal onTimePickupPercentage;
    private BigDecimal successfulPickupPercentage;
    private String vehicleType;
    private String vehicleNumber;
    private BigDecimal vehicleCapacityKg;
    private String emergencyContactName;
    private String emergencyContactMobile;
}
