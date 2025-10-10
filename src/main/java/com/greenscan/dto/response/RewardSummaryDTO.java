package com.greenscan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Lightweight summary of reward shown in cards/list view.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardSummaryDTO {

    private Long id;
    private String title;
    private String category;
    private BigDecimal coinsRequired;
    private String imageUrl;
    private String partnerName;
    private String partnerLogoUrl;
    private Boolean available;

    // For direct conversion from entity
    public RewardSummaryDTO(com.greenscan.entity.Reward reward) {
        this.id = reward.getId();
        this.title = reward.getTitle();
        this.category = reward.getCategory();
        this.coinsRequired = reward.getCoinsRequired();
        this.imageUrl = reward.getImageUrl();
        this.partnerName = reward.getPartnerName();
        this.partnerLogoUrl = reward.getPartnerLogoUrl();
        this.available = reward.isAvailable();
    }
}
