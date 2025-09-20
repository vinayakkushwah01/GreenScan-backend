package com.greenscan.entity;

import com.greenscan.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ngo_profiles")
@EqualsAndHashCode(callSuper = true)
public class NGOProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private MainUser user;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "registration_number", unique = true)
    private String registrationNumber;

    @Column(name = "cause_description", columnDefinition = "TEXT")
    private String causeDescription;

    @Column(name = "website_url")
    private String websiteUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Column(name = "approved_by_admin_id")
    private Long approvedByAdminId;

    @Column(name = "approved_at")
    private java.time.LocalDateTime approvedAt;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    // Financial
    @Column(name = "total_coins_received", precision = 15, scale = 2)
    private BigDecimal totalCoinsReceived = BigDecimal.ZERO;

    @Column(name = "total_money_converted", precision = 15, scale = 2)
    private BigDecimal totalMoneyConverted = BigDecimal.ZERO;

    @Column(name = "pending_conversion_coins", precision = 15, scale = 2)
    private BigDecimal pendingConversionCoins = BigDecimal.ZERO;

    // Bank details for money conversion
    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "bank_ifsc_code")
    private String bankIfscCode;

    @Column(name = "bank_account_holder_name")
    private String bankAccountHolderName;

    @Column(name = "bank_name")
    private String bankName;

    // Documents
    @Column(name = "registration_document_url")
    private String registrationDocumentUrl;

    @Column(name = "other_document_urls", columnDefinition = "TEXT")
    private String otherDocumentUrls; // JSON array

    // Statistics
    @Column(name = "total_donors")
    private Integer totalDonors = 0;

    @Column(name = "total_donations")
    private Integer totalDonations = 0;

    @Column(name = "impact_beneficiaries")
    private Integer impactBeneficiaries = 0;

    @Column(name = "impact_description", columnDefinition = "TEXT")
    private String impactDescription;
}
