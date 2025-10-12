package com.greenscan.dto.response;

import com.greenscan.entity.NGOProfile;
import com.greenscan.enums.ApprovalStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
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

    /**
     * Full constructor — for NGO viewing their own profile.
     * Shows all details.
     */
    public NGOProfileResponse(NGOProfile ngo, String role) {
        this.id = ngo.getId();
        this.organizationName = ngo.getOrganizationName();
        this.registrationNumber = ngo.getRegistrationNumber();
        this.causeDescription = ngo.getCauseDescription();
        this.websiteUrl = ngo.getWebsiteUrl();
        this.approvalStatus = ngo.getApprovalStatus();
        this.impactBeneficiaries = ngo.getImpactBeneficiaries();
        this.impactDescription = ngo.getImpactDescription();
        this.totalDonors = ngo.getTotalDonors();
        this.totalDonations = ngo.getTotalDonations();

        // NGO itself — full visibility
        if ("NGO".equalsIgnoreCase(role)) {
            this.userId = ngo.getUser() != null ? ngo.getUser().getId() : null;
            this.approvedByAdminId = ngo.getApprovedByAdminId();
            this.approvedAt = ngo.getApprovedAt();
            this.rejectionReason = ngo.getRejectionReason();

            this.totalCoinsReceived = ngo.getTotalCoinsReceived();
            this.totalMoneyConverted = ngo.getTotalMoneyConverted();
            this.pendingConversionCoins = ngo.getPendingConversionCoins();

            this.bankAccountNumber = ngo.getBankAccountNumber();
            this.bankIfscCode = ngo.getBankIfscCode();
            this.bankAccountHolderName = ngo.getBankAccountHolderName();
            this.bankName = ngo.getBankName();

            this.registrationDocumentUrl = null; // optional future document logic
            this.otherDocumentUrls = null;
        }

        // End User — only public, non-sensitive info
        else if ("ENDUSER".equalsIgnoreCase(role)) {
            // Hide private details
            this.userId = null;
            this.approvedByAdminId = null;
            this.approvedAt = null;
            this.rejectionReason = null;

            this.totalCoinsReceived = null;
            this.totalMoneyConverted = null;
            this.pendingConversionCoins = null;

            this.bankAccountNumber = null;
            this.bankIfscCode = null;
            this.bankAccountHolderName = null;
            this.bankName = null;

            this.registrationDocumentUrl = null;
            this.otherDocumentUrls = null;
        }
    }
}