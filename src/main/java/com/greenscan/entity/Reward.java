package com.greenscan.entity;

import com.greenscan.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rewards", indexes = {
    @Index(name = "idx_reward_status", columnList = "approval_status"),
    @Index(name = "idx_reward_active", columnList = "is_active"),
    @Index(name = "idx_reward_category", columnList = "category")
})
@EqualsAndHashCode(callSuper = true)
public class Reward extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category")
    private String category; // VOUCHER, DISCOUNT, PRODUCT, CASHBACK

    @Column(name = "coins_required", precision = 10, scale = 2, nullable = false)
    private BigDecimal coinsRequired;

    @Column(name = "monetary_value", precision = 10, scale = 2)
    private BigDecimal monetaryValue;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "terms_conditions", columnDefinition = "TEXT")
    private String termsConditions;

    @Column(name = "redemption_instructions", columnDefinition = "TEXT")
    private String redemptionInstructions;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus = ApprovalStatus.APPROVED;

    @Column(name = "created_by_admin_id")
    private Long createdByAdminId;

    // Availability
    @Column(name = "available_from")
    private LocalDateTime availableFrom;

    @Column(name = "available_until")
    private LocalDateTime availableUntil;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @Column(name = "redeemed_quantity")
    private Integer redeemedQuantity = 0;

    @Column(name = "max_per_user")
    private Integer maxPerUser = 1;

    @Column(name = "partner_name")
    private String partnerName;

    @Column(name = "partner_logo_url")
    private String partnerLogoUrl;

    public boolean isAvailable() {
        LocalDateTime now = LocalDateTime.now();
        return getIsActive() &&
               approvalStatus == ApprovalStatus.APPROVED &&
               (availableFrom == null || now.isAfter(availableFrom)) &&
               (availableUntil == null || now.isBefore(availableUntil)) &&
               (totalQuantity == null || redeemedQuantity < totalQuantity);
    }

    public Integer getRemainingQuantity() {
        if (totalQuantity == null) return null;
        return totalQuantity - redeemedQuantity;
    }
}
