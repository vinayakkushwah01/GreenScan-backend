package com.greenscan.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddItemRequest {
  @NotBlank(message = "Cart ID is required")
    private String cartId;
    
    private String imageUrl; // From image upload
    private String barcode;
    private String qrCode;
    
    // For manual entry fallback
    private String itemName;
    private String materialType;
    private Double estimatedWeight;  
}
