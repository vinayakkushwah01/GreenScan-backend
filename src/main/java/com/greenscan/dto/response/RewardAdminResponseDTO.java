package com.greenscan.dto.response;


import com.greenscan.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardAdminResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String category;
    private BigDecimal coinsRequired;
    private BigDecimal monetaryValue;
    private String imageUrl;
    private String termsConditions;
    private String redemptionInstructions;
    private ApprovalStatus approvalStatus;
    private Long createdByAdminId;
    private LocalDateTime availableFrom;
    private LocalDateTime availableUntil;
    private Integer totalQuantity;
    private Integer redeemedQuantity;
    private Integer maxPerUser;
    private String partnerName;
    private String partnerLogoUrl;
    private Boolean isActive;
    private Boolean available;
    private Integer remainingQuantity;

    // Overloaded constructor for entity mapping
    public RewardAdminResponseDTO(com.greenscan.entity.Reward reward) {
        this.id = reward.getId();
        this.title = reward.getTitle();
        this.description = reward.getDescription();
        this.category = reward.getCategory();
        this.coinsRequired = reward.getCoinsRequired();
        this.monetaryValue = reward.getMonetaryValue();
        this.imageUrl = reward.getImageUrl();
        this.termsConditions = reward.getTermsConditions();
        this.redemptionInstructions = reward.getRedemptionInstructions();
        this.approvalStatus = reward.getApprovalStatus();
        this.createdByAdminId = reward.getCreatedByAdminId();
        this.availableFrom = reward.getAvailableFrom();
        this.availableUntil = reward.getAvailableUntil();
        this.totalQuantity = reward.getTotalQuantity();
        this.redeemedQuantity = reward.getRedeemedQuantity();
        this.maxPerUser = reward.getMaxPerUser();
        this.partnerName = reward.getPartnerName();
        this.partnerLogoUrl = reward.getPartnerLogoUrl();
        this.isActive = reward.getIsActive();
        this.available = reward.isAvailable();
        this.remainingQuantity = reward.getRemainingQuantity();
    }
}
