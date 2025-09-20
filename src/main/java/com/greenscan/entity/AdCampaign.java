package com.greenscan.entity;

import com.greenscan.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ad_campaigns", indexes = {
    @Index(name = "idx_ad_campaign_company", columnList = "ads_company_id"),
    @Index(name = "idx_ad_campaign_status", columnList = "approval_status"),
    @Index(name = "idx_ad_campaign_active", columnList = "is_active")
})
@EqualsAndHashCode(callSuper = true)
public class AdCampaign extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ads_company_id", nullable = false)
    private AdsCompanyProfile adsCompany;

    @Column(name = "campaign_name", nullable = false)
    private String campaignName;

    @Column(name = "campaign_description", columnDefinition = "TEXT")
    private String campaignDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Column(name = "approved_by_admin_id")
    private Long approvedByAdminId;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    // Campaign media
    @Column(name = "media_type")
    private String mediaType; // IMAGE, VIDEO, BANNER

    @Column(name = "media_url", nullable = false)
    private String mediaUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "click_url")
    private String clickUrl;

    // Targeting
    @Column(name = "target_cities", length = 1000)
    private String targetCities; // Comma-separated

    @Column(name = "target_age_min")
    private Integer targetAgeMin;

    @Column(name = "target_age_max")
    private Integer targetAgeMax;

    @Column(name = "target_gender")
    private String targetGender; // MALE, FEMALE, ALL

    // Campaign duration
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    // Budget and pricing
    @Column(name = "total_budget", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalBudget;

    @Column(name = "spent_amount", precision = 12, scale = 2)
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @Column(name = "cost_per_impression", precision = 8, scale = 4)
    private BigDecimal costPerImpression = BigDecimal.valueOf(0.10);

    @Column(name = "cost_per_click", precision = 8, scale = 2)
    private BigDecimal costPerClick = BigDecimal.valueOf(1.00);

    // Performance metrics
    @Column(name = "total_impressions")
    private Long totalImpressions = 0L;

    @Column(name = "total_clicks")
    private Long totalClicks = 0L;

    @Column(name = "click_through_rate", precision = 5, scale = 2)
    private BigDecimal clickThroughRate = BigDecimal.ZERO;

    @Column(name = "priority_level")
    private Integer priorityLevel = 1; // 1-5, higher number = higher priority

    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        return getIsActive() &&
               approvalStatus == ApprovalStatus.APPROVED &&
               now.isAfter(startDate) &&
               now.isBefore(endDate) &&
               spentAmount.compareTo(totalBudget) < 0;
    }

    public BigDecimal getRemainingBudget() {
        return totalBudget.subtract(spentAmount);
    }
}
