package com.greenscan.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class VerifyItemRequest {
     @NotBlank(message = "Item ID is required")
    private String itemId;
    
    @NotBlank(message = "Action is required") // APPROVE or REJECT
    private String action;
    
    private String notes;
    private String rejectionReason;
}
