package com.greenscan.dto.transaction;

import com.greenscan.dto.base.BaseDTO;
import com.greenscan.enums.TransactionStatus;
import com.greenscan.enums.TransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionDTO extends BaseDTO {
    private Long userId;
    private TransactionType type;
    private TransactionStatus status;
    private BigDecimal amount;
    private String currency;
    private Long relatedEntityId;
    private String relatedEntityType;
    private String description;
    private String referenceNumber;
    private String paymentMethod;
    private String paymentGateway;
    private String transactionId;
    private LocalDateTime processedAt;
    private String failureReason;
}
