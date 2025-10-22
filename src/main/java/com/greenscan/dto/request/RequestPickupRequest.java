package com.greenscan.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestPickupRequest {
    
 @NotBlank(message = "Cart ID is required")
    private Long cartId;
    
    private String preferredDate; // yyyy-MM-dd
    private String preferredTimeSlot; // e.g., "09:00-12:00"
    private String instructions;
}