package com.greenscan.entity;

import com.greenscan.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "green_coin_transactions", indexes = {
    @Index(name = "idx_transaction_user", columnList = "user_id"),
    @Index(name = "idx_transaction_type", columnList = "transaction_type"),
    @Index(name = "idx_transaction_cart", columnList = "cart_id"),
    @Index(name = "idx_transaction_created", columnList = "created_at")
})
@EqualsAndHashCode(callSuper = true)
public class GreenCoinTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private MainUser user;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "balance_before", precision = 15, scale = 2)
    private BigDecimal balanceBefore;

    @Column(name = "balance_after", precision = 15, scale = 2)
    private BigDecimal balanceAfter;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "reference_id")
    private String referenceId;

    // Related entities
    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "ngo_id")
    private Long ngoId;

    @Column(name = "reward_id")
    private Long rewardId;

    @Column(name = "processed_by_admin_id")
    private Long processedByAdminId;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON for additional data
}
