package com.greenscan.entity;

import com.greenscan.enums.ItemStatus;
import com.greenscan.enums.MaterialType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cart_items", indexes = {
    @Index(name = "idx_cart_item_cart", columnList = "cart_id"),
    @Index(name = "idx_cart_item_status", columnList = "status"),
    @Index(name = "idx_cart_item_material", columnList = "material_type")
})
@EqualsAndHashCode(callSuper = true)
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", nullable = false)
    private MaterialType materialType;

    @Column(name = "estimated_weight", precision = 8, scale = 2, nullable = false)
    private BigDecimal estimatedWeight;

    @Column(name = "actual_weight", precision = 8, scale = 2)
    private BigDecimal actualWeight;

    @Column(name = "estimated_coins", precision = 10, scale = 2, nullable = false)
    private BigDecimal estimatedCoins;

    @Column(name = "actual_coins", precision = 10, scale = 2)
    private BigDecimal actualCoins;

    @Column(name = "is_recyclable", nullable = false)
    private Boolean isRecyclable = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ItemStatus status = ItemStatus.PENDING_USER_CONFIRMATION;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "ai_confidence_score", precision = 5, scale = 2)
    private BigDecimal aiConfidenceScore;

    @Column(name = "ai_detection_data", columnDefinition = "TEXT")
    private String aiDetectionData; // JSON string with AI response

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "qr_code")
    private String qrCode;

    // User edit tracking
    @Column(name = "user_edited")
    private Boolean userEdited = false;

    @Column(name = "original_item_name")
    private String originalItemName;

    @Column(name = "original_material_type")
    @Enumerated(EnumType.STRING)
    private MaterialType originalMaterialType;

    @Column(name = "original_estimated_weight", precision = 8, scale = 2)
    private BigDecimal originalEstimatedWeight;

    // Vendor verification
    @Column(name = "verified_by_vendor_id")
    private Long verifiedByVendorId;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "vendor_notes", length = 500)
    private String vendorNotes;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    // Assistant verification during pickup
    @Column(name = "pickup_verified_by_assistant_id")
    private Long pickupVerifiedByAssistantId;

    @Column(name = "pickup_verified_at")
    private LocalDateTime pickupVerifiedAt;

    @Column(name = "assistant_notes", length = 500)
    private String assistantNotes;

    // Helper methods
    public void calculateEstimatedCoins() {
        if (materialType != null && estimatedWeight != null) {
            BigDecimal ratePerKg = BigDecimal.valueOf(materialType.getCoinsPerKg());
            estimatedCoins = estimatedWeight.multiply(ratePerKg);
        }
    }

    public void calculateActualCoins() {
        if (materialType != null && actualWeight != null) {
            BigDecimal ratePerKg = BigDecimal.valueOf(materialType.getCoinsPerKg());
            actualCoins = actualWeight.multiply(ratePerKg);
        }
    }

    public boolean requiresVendorVerification() {
        return userEdited && (status == ItemStatus.USER_EDITED || status == ItemStatus.PENDING_VENDOR_VERIFICATION);
    }
}
