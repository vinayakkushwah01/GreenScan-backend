package com.greenscan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Full detailed view of reward when user clicks on a card.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardDetailDTO {

    private Long id;
    private String title;
    private String description;
    private String category;
    private BigDecimal coinsRequired;
    private BigDecimal monetaryValue;
    private String imageUrl;
    private String termsConditions;
    private String redemptionInstructions;
    private String partnerName;
    private String partnerLogoUrl;
    private Integer remainingQuantity;
    private Boolean available;

    // Overloaded constructor for entity mapping
    public RewardDetailDTO(com.greenscan.entity.Reward reward) {
        this.id = reward.getId();
        this.title = reward.getTitle();
        this.description = reward.getDescription();
        this.category = reward.getCategory();
        this.coinsRequired = reward.getCoinsRequired();
        this.monetaryValue = reward.getMonetaryValue();
        this.imageUrl = reward.getImageUrl();
        this.termsConditions = reward.getTermsConditions();
        this.redemptionInstructions = reward.getRedemptionInstructions();
        this.partnerName = reward.getPartnerName();
        this.partnerLogoUrl = reward.getPartnerLogoUrl();
        this.remainingQuantity = reward.getRemainingQuantity();
        this.available = reward.isAvailable();
    }
}
