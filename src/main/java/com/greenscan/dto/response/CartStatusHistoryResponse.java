package com.greenscan.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

import com.greenscan.enums.CartStatus;

@Data
public class CartStatusHistoryResponse {
    private Long id;
    private Long cartId;
    private CartStatus status;      
    private LocalDateTime changedAt;
    private Long changedByUserId;
    private String remarks;
}
