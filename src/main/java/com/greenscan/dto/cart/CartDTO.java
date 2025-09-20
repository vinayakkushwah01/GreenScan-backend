package com.greenscan.dto.cart;

import com.greenscan.dto.base.BaseDTO;
import com.greenscan.enums.CartStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CartDTO extends BaseDTO {
    private Long endUserId;
    private Long vendorId;
    private Long assistantId;
    private CartStatus status;
    private LocalDateTime scheduledPickupTime;
    private LocalDateTime actualPickupTime;
    private LocalDateTime completedAt;
    private String pickupAddressLine1;
    private String pickupAddressLine2;
    private String pickupCity;
    private String pickupState;
    private String pickupPincode;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private String landmark;
    private List<CartItemDTO> items;
    private BigDecimal totalWeightKg;
    private BigDecimal totalCoinsEarned;
    private BigDecimal totalValue;
    private String specialInstructions;
    private String cancellationReason;
    private Long cancelledByUserId;
    private LocalDateTime cancelledAt;
    private Boolean isRescheduled;
    private Integer rescheduleCount;
}
