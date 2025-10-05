package com.greenscan.dto.response;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class EndUserProfileResponse {
    private BigDecimal greenCoinsBalance;
    private BigDecimal totalCoinsEarned;
    private BigDecimal totalCoinsDonated;
    private BigDecimal totalCoinsRedeemed;
    private BigDecimal totalWasteRecycledKg;
    private Integer totalCartsCompleted;
    private Integer ecoScore;
    private UserResponse user;
  

}
