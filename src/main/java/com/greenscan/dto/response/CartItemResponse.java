package com.greenscan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.greenscan.entity.CartItem;
import com.greenscan.enums.MaterialType;

import lombok.Data;
@Data
public class CartItemResponse {
    private Long id;
    private String itemName;
    private MaterialType materialType;
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
     CartItemResponse mapToResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();
        response.setId(item.getId());
        response.setItemName(item.getItemName());
        response.setMaterialType(item.getMaterialType());
        response.setEstimatedWeight(item.getEstimatedWeight());
        response.setActualWeight(item.getActualWeight());
        response.setEstimatedCoins(item.getEstimatedCoins());
        response.setActualCoins(item.getActualCoins());
        response.setIsRecyclable(item.getIsRecyclable());
        response.setStatus(item.getStatus().name());
        response.setImageUrl(item.getImageUrl());
        response.setAiConfidenceScore(item.getAiConfidenceScore());
        response.setUserEdited(item.getUserEdited());
        response.setVendorNotes(item.getVendorNotes());
        response.setRejectionReason(item.getRejectionReason());
        response.setCreatedAt(item.getCreatedAt());
        return response;
    }
}
