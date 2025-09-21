package com.greenscan.dto.core;

import com.greenscan.dto.base.BaseDTO;
import com.greenscan.enums.ApprovalStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class VendorProfileDTO extends BaseDTO {
    private UserDTO user;
    private String businessName;
    private String businessRegistrationNumber;
    private String gstNumber;
    private String panNumber;
    private ApprovalStatus approvalStatus;
    private Long approvedByAdminId;
    private LocalDateTime approvedAt;
    private String rejectionReason;
    private List<String> serviceCities;
    private Integer serviceRadiusKm;
    private LocalTime workingStartTime;
    private LocalTime workingEndTime;
    private List<String> workingDays;
    private Integer dailyPickupCapacity;
    private BigDecimal maxWeightPerPickupKg;
    private BigDecimal platformCommissionRate;
    private BigDecimal pendingPaymentAmount;
    private BigDecimal paymentLimit;
    private BigDecimal totalRevenue;
    private BigDecimal totalPlatformFeePaid;
    private Integer totalPickupsCompleted;
    private BigDecimal totalWeightCollectedKg;
    private BigDecimal averageRating;
    private Integer totalRatings;
    private Boolean kycVerified;
    private LocalDateTime kycVerifiedAt;
    private List<PickupAssistantDTO> assistants;
}