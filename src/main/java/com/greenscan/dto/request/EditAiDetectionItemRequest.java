package com.greenscan.dto.request;


import java.math.BigDecimal;

import com.greenscan.enums.MaterialType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditAiDetectionItemRequest {

    @NotBlank(message = "Item name is required.")
    private String itemName;

    @NotNull(message = "Material type is required.")
    private MaterialType materialType;

    @NotNull(message = "Estimated weight is required.")
    @DecimalMin(value = "0.1", message = "Estimated weight must be greater than zero.")
    private BigDecimal estimatedWeight;
}
 

