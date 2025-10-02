package com.greenscan.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AcceptCartRequest {
     @NotBlank(message = "Cart ID is required")
    private String cartId;
    
    private String notes;
    private String estimatedPickupTime; // ISO format
}
