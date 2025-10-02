package com.greenscan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class GreenCoinTransactionResponse {
     private Long id;
    private String transactionType;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String description;
    private LocalDateTime createdAt;
    private String referenceId;   
}
