package com.greenscan.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmItemRequest {
    @NotBlank(message = "Item name is required")
    private String itemName;
    
    @NotBlank(message = "Material type is required")
    private String materialType;
    
    @NotBlank(message = "Weight is required")
    private Double estimatedWeight;
    
    private Boolean isRecyclable = true;
    private String userNotes;
}
