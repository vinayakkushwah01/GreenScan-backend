package com.greenscan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reward_redemptions", indexes = {
    @Index(name = "idx_redemption_user", columnList = "user_id"),
    @Index(name = "idx_redemption_reward", columnList = "reward_id"),
    @Index(name = "idx_redemption_status", columnList = "redemption_status")
})
@EqualsAndHashCode(callSuper = true)
public class RewardRedemption extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private MainUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id", nullable = false)
    private Reward reward;

    @Column(name = "redemption_code", unique = true, nullable = false)
    private String redemptionCode;

    @Column(name = "coins_spent", precision = 10, scale = 2, nullable = false)
    private BigDecimal coinsSpent;

    @Column(name = "redemption_status")
    private String redemptionStatus = "PENDING"; // PENDING, USED, EXPIRED, CANCELLED

    @Column(name = "redeemed_at")
    private LocalDateTime redeemedAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "user_notes", length = 500)
    private String userNotes;

    @Column(name = "admin_notes", length = 500)
    private String adminNotes;

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean canBeUsed() {
        return "PENDING".equals(redemptionStatus) && !isExpired();
    }
}
