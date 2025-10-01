package com.greenscan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class RewardResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private BigDecimal coinsRequired;
    private BigDecimal monetaryValue;
    private String imageUrl;
    private String partnerName;
    private Integer remainingQuantity;
    private Boolean isAvailable;
    private LocalDateTime availableUntil;
}
