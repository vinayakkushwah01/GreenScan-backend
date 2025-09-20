package com.greenscan.dto.core;

import com.greenscan.dto.base.BaseDTO;
import com.greenscan.enums.ApprovalStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdsCompanyProfileDTO extends BaseDTO {
    private UserDTO user;
    private String companyName;
    private String businessRegistrationNumber;
    private String gstNumber;
    private String industryType;
    private ApprovalStatus approvalStatus;
    private Long approvedByAdminId;
    private LocalDateTime approvedAt;
    private String rejectionReason;
    private BigDecimal totalSpent;
    private BigDecimal walletBalance;
    private BigDecimal creditLimit;
    private Integer totalCampaigns;
    private Integer activeCampaigns;
    private Long totalImpressions;
    private Long totalClicks;
    private BigDecimal averageCtr;
}
