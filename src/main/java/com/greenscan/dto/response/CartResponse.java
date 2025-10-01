package com.greenscan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CartResponse {
    private Long id;
    private String cartNumber;
    private String status;
    private BigDecimal totalEstimatedWeight;
    private BigDecimal totalActualWeight;
    private BigDecimal totalEstimatedCoins;
    private BigDecimal totalActualCoins;
    private LocalDateTime createdAt;
    private LocalDateTime pickupScheduledAt;
    private LocalDateTime pickupCompletedAt;
    
    private UserResponse vendor;
    private UserResponse pickupAssistant;
    
    private String pickupAddress;
    private String pickupInstructions;
    private String vendorNotes;
    
    private List<CartItemResponse> items;
    private List<CartStatusHistoryResponse> statusHistory;
    
    private Integer verifiedItemCount;
    private Integer pendingVerificationCount;

}
