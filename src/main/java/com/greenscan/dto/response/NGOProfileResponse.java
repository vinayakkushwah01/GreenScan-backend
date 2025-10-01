 package com.greenscan.dto.response;

import com.greenscan.enums.ApprovalStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class NGOProfileResponse {
    private Long id;
    private Long userId;
    private String organizationName;
    private String registrationNumber;
    private String causeDescription;
    private String websiteUrl;
    private ApprovalStatus approvalStatus;
    private Long approvedByAdminId;
    private LocalDateTime approvedAt;
    private String rejectionReason;

    // Financial
    private BigDecimal totalCoinsReceived;
    private BigDecimal totalMoneyConverted;
    private BigDecimal pendingConversionCoins;

    // Bank details
    private String bankAccountNumber;
    private String bankIfscCode;
    private String bankAccountHolderName;
    private String bankName;

    // Documents
    private String registrationDocumentUrl;
    private String otherDocumentUrls;

    // Statistics
    private Integer totalDonors;
    private Integer totalDonations;
    private Integer impactBeneficiaries;
    private String impactDescription;
}