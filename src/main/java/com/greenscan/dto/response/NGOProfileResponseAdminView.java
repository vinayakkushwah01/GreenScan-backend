package com.greenscan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.greenscan.entity.CloudinaryFile;
import com.greenscan.entity.NGOProfile;
import com.greenscan.enums.ApprovalStatus;

public class NGOProfileResponseAdminView {
   
    private Long id;

    private Long userId;
    private String organizationName;
    private String registrationNumber;
    private String causeDescription;
    private String websiteUrl;

    //documents 
    private List<String> docsName;
    private List<byte[]> docsFile;

    // Approval details
    private ApprovalStatus approvalStatus;
    private Long approvedByAdminId;
    private LocalDateTime approvedAt;
    private String rejectionReason;

    // Financial stats
    private BigDecimal totalCoinsReceived;
    private BigDecimal totalMoneyConverted;
    private BigDecimal pendingConversionCoins;

    // Bank details (for verification, not for public display)
    private String bankAccountNumber;
    private String bankIfscCode;
    private String bankAccountHolderName;
    private String bankName;

    // Impact statistics
    private Integer totalDonors;
    private Integer totalDonations;
    private Integer impactBeneficiaries;
    private String impactDescription;

    /**
     * Constructor to map directly from NGOProfile entity.
     */
    public NGOProfileResponseAdminView(NGOProfile ngo) {
        this.id = ngo.getId();
        this.userId = ngo.getUser() != null ? ngo.getUser().getId() : null;
        this.organizationName = ngo.getOrganizationName();
        this.registrationNumber = ngo.getRegistrationNumber();
        this.causeDescription = ngo.getCauseDescription();
        this.websiteUrl = ngo.getWebsiteUrl();

        this.approvalStatus = ngo.getApprovalStatus();
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

        this.totalDonors = ngo.getTotalDonors();
        this.totalDonations = ngo.getTotalDonations();
        this.impactBeneficiaries = ngo.getImpactBeneficiaries();
        this.impactDescription = ngo.getImpactDescription();

         if (ngo.getRegistrationDocument() != null && !ngo.getRegistrationDocument().isEmpty()) {
        this.docsName = ngo.getRegistrationDocument().stream()
                              .map(CloudinaryFile::getFileName)
                              .toList();
        } else {
            this.docsName = null;
        }
    } 
}
