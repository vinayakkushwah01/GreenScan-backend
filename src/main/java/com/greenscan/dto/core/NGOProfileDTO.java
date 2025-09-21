package com.greenscan.dto.core;

import com.greenscan.dto.base.BaseDTO;
import com.greenscan.enums.ApprovalStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class NGOProfileDTO extends BaseDTO {
    private UserDTO user;
    private String organizationName;
    private String registrationNumber;
    private String causeDescription;
    private String websiteUrl;
    private ApprovalStatus approvalStatus;
    private Long approvedByAdminId;
    private LocalDateTime approvedAt;
    private String rejectionReason;
    private BigDecimal totalCoinsReceived;
    private BigDecimal totalMoneyConverted;
    private BigDecimal pendingConversionCoins;
    private String bankAccountNumber;
    private String bankIfscCode;
    private String bankAccountHolderName;
    private String bankName;
    private String registrationDocumentUrl;
    private List<String> otherDocumentUrls;
    private Integer totalDonors;
    private Integer totalDonations;
    private Integer impactBeneficiaries;
    private String impactDescription;
}
