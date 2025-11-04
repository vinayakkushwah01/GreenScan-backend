package com.greenscan.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.greenscan.enums.CartStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "carts", indexes = {
    @Index(name = "idx_cart_user", columnList = "user_id"),
    @Index(name = "idx_cart_vendor", columnList = "vendor_id"),
    @Index(name = "idx_cart_status", columnList = "status"),
    @Index(name = "idx_cart_created", columnList = "created_at")
})
@EqualsAndHashCode(callSuper = true)
public class Cart extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private MainUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private MainUser vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_assistant_id")
    private MainUser pickupAssistant;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CartStatus status = CartStatus.DRAFT;

    @Column(name = "cart_number", unique = true, nullable = false)
    private String cartNumber;

    @Column(name = "total_estimated_weight", precision = 8, scale = 2)
    private BigDecimal totalEstimatedWeight = BigDecimal.ZERO;

    @Column(name = "total_actual_weight", precision = 8, scale = 2)
    private BigDecimal totalActualWeight = BigDecimal.ZERO;

    @Column(name = "total_estimated_coins", precision = 10, scale = 2)
    private BigDecimal totalEstimatedCoins = BigDecimal.ZERO;

    @Column(name = "total_actual_coins", precision = 10, scale = 2)
    private BigDecimal totalActualCoins = BigDecimal.ZERO;

    @Column(name = "pickup_requested_at")
    private LocalDateTime pickupRequestedAt;

    @Column(name = "pickup_scheduled_at")
    private LocalDateTime pickupScheduledAt;

    @Column(name = "pickup_started_at")
    private LocalDateTime pickupStartedAt;

    @Column(name = "pickup_completed_at")
    private LocalDateTime pickupCompletedAt;

    // Pickup Address (can be different from user's default address)
    @Column(name = "pickup_address_line1")
    private String pickupAddressLine1;

    @Column(name = "pickup_address_line2")
    private String pickupAddressLine2;

    @Column(name = "pickup_city")
    private String pickupCity;

    @Column(name = "pickup_pincode")
    private String pickupPincode;

    @Column(name = "pickup_latitude")
    private Double pickupLatitude;

    @Column(name = "pickup_longitude")
    private Double pickupLongitude;

    @Column(name = "pickup_instructions", length = 500)
    private String pickupInstructions;

    @Column(name = "vendor_notes", length = 500)
    private String vendorNotes;

    @Column(name = "assistant_notes", length = 500)
    private String assistantNotes;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Column(name = "cancelled_by_user_id")
    private Long cancelledByUserId;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CartItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartStatusHistory> statusHistory = new ArrayList<>();

    // Helper methods
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
        recalculateTotals();
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
        recalculateTotals();
    }

    public void recalculateTotals() {
        totalEstimatedWeight = items.stream()
            .filter(item -> item.getStatus().equals(com.greenscan.enums.ItemStatus.VERIFIED) ||
                           item.getStatus().equals(com.greenscan.enums.ItemStatus.USER_CONFIRMED))
            .map(CartItem::getEstimatedWeight)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalEstimatedCoins = items.stream()
            .filter(item -> item.getStatus().equals(com.greenscan.enums.ItemStatus.VERIFIED) ||
                           item.getStatus().equals(com.greenscan.enums.ItemStatus.USER_CONFIRMED))
            .map(CartItem::getEstimatedCoins)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getVerifiedItemCount() {
        return (int) items.stream()
            .filter(item -> item.getStatus().equals(com.greenscan.enums.ItemStatus.VERIFIED) ||
                           item.getStatus().equals(com.greenscan.enums.ItemStatus.USER_CONFIRMED))
            .count();
    }

    public int getPendingVerificationCount() {
        return (int) items.stream()
            .filter(item -> item.getStatus().equals(com.greenscan.enums.ItemStatus.PENDING_VENDOR_VERIFICATION))
            .count();
    }
}
