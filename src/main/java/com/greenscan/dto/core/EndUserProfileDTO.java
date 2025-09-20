package com.greenscan.dto.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import com.greenscan.dto.base.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class EndUserProfileDTO extends BaseDTO {
    private UserDTO user;
    private BigDecimal greenCoinsBalance;
    private BigDecimal totalCoinsEarned;
    private BigDecimal totalCoinsDonated;
    private BigDecimal totalCoinsRedeemed;
    private BigDecimal totalWasteRecycledKg;
    private Integer totalCartsCompleted;
    private Long preferredVendorId;
    private String preferredPickupTime;
    private Integer ecoScore;
    private String referralCode;
    private String referredByCode;
    private BigDecimal referralBonusEarned;
}
