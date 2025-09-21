package com.greenscan.dto.transaction;

import com.greenscan.dto.base.BaseDTO;
import com.greenscan.enums.TransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class RewardTransactionDTO extends BaseDTO {
    private Long userId;
    private TransactionType type;
    private BigDecimal coins;
    private Long relatedEntityId;
    private String relatedEntityType;
    private String description;
    private LocalDateTime expiryDate;
    private Boolean isExpired;
    private Boolean isReversed;
}
