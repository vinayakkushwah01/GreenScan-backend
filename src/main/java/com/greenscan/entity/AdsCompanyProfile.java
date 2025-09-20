package com.greenscan.entity;

import com.greenscan.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ads_company_profiles")
@EqualsAndHashCode(callSuper = true)
public class AdsCompanyProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private MainUser user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "business_registration_number")
    private String businessRegistrationNumber;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "industry_type")
    private String industryType;

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
    @Column(name = "total_spent", precision = 15, scale = 2)
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "wallet_balance", precision = 12, scale = 2)
    private BigDecimal walletBalance = BigDecimal.ZERO;

    @Column(name = "credit_limit", precision = 12, scale = 2)
    private BigDecimal creditLimit = BigDecimal.valueOf(50000.00);

    // Statistics
    @Column(name = "total_campaigns")
    private Integer totalCampaigns = 0;

    @Column(name = "active_campaigns")
    private Integer activeCampaigns = 0;

    @Column(name = "total_impressions")
    private Long totalImpressions = 0L;

    @Column(name = "total_clicks")
    private Long totalClicks = 0L;

    @Column(name = "average_ctr", precision = 5, scale = 2)
    private BigDecimal averageCtr = BigDecimal.ZERO; // Click-through rate
}
