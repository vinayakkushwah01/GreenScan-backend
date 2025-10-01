package com.greenscan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class CartItemResponse {
    private Long id;
    private String itemName;
    private String materialType;
    private BigDecimal estimatedWeight;
    private BigDecimal actualWeight;
    private BigDecimal estimatedCoins;
    private BigDecimal actualCoins;
    private Boolean isRecyclable;
    private String status;
    private String imageUrl;
    private BigDecimal aiConfidenceScore;
    private Boolean userEdited;
    private String vendorNotes;
    private String rejectionReason;
    private LocalDateTime createdAt; 
}
