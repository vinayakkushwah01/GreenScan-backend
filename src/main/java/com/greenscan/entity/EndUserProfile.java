package com.greenscan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "end_user_profiles")
@EqualsAndHashCode(callSuper = true)
public class EndUserProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private MainUser user;

    @Column(name = "green_coins_balance", precision = 10, scale = 2)
    private BigDecimal greenCoinsBalance = BigDecimal.ZERO;

    @Column(name = "total_coins_earned", precision = 10, scale = 2)
    private BigDecimal totalCoinsEarned = BigDecimal.ZERO;

    @Column(name = "total_coins_donated", precision = 10, scale = 2)
    private BigDecimal totalCoinsDonated = BigDecimal.ZERO;

    @Column(name = "total_coins_redeemed", precision = 10, scale = 2)
    private BigDecimal totalCoinsRedeemed = BigDecimal.ZERO;

    @Column(name = "total_waste_recycled_kg", precision = 8, scale = 2)
    private BigDecimal totalWasteRecycledKg = BigDecimal.ZERO;

    @Column(name = "total_carts_completed")
    private Integer totalCartsCompleted = 0;

    @Column(name = "preferred_vendor_id")
    private Long preferredVendorId;

    @Column(name = "preferred_pickup_time")
    private String preferredPickupTime;

    @Column(name = "eco_score")
    private Integer ecoScore = 0;

    @Column(name = "referral_code", unique = true)
    private String referralCode;

    @Column(name = "referred_by_code")
    private String referredByCode;

    @Column(name = "referral_bonus_earned", precision = 10, scale = 2)
    private BigDecimal referralBonusEarned = BigDecimal.ZERO;
}
