package com.greenscan.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AcceptCartRequest {
     @NotBlank(message = "Cart ID is required")
    private Long cartId;
    private String notes;
    private LocalDateTime estimatedPickupTime; // ISO format
}
