package com.greenscan.dto.request;

import com.greenscan.enums.MaterialType;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddItemRequest {
  @NotBlank(message = "Cart ID is required")
    private Long cartId;
    
    private String imageUrl; // From image upload
    
    // For manual entry fallback
    private String itemName;
    private MaterialType materialType;
    private Double estimatedGreenCoin;
    private Double estimatedWeight;  
}
