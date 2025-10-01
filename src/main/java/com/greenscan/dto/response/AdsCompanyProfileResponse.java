package com.greenscan.dto.response;

import com.greenscan.enums.ApprovalStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdsCompanyProfileResponse {
    private Long id;
    private Long userId;
    private String companyName;
    private String businessRegistrationNumber;
    private String gstNumber;
    private String industryType;
    private ApprovalStatus approvalStatus;
    private Long approvedByAdminId;
    private LocalDateTime approvedAt;
    private String rejectionReason;

    // Financial
    private BigDecimal totalSpent;
    private BigDecimal walletBalance;
    private BigDecimal creditLimit;

    // Statistics
    private Integer totalCampaigns;
    private Integer activeCampaigns;
    private Long totalImpressions;
    private Long totalClicks;
    private BigDecimal averageCtr;
}
