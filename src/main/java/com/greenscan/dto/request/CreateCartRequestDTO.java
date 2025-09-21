package com.greenscan.dto.request;

import lombok.Data;
import java.util.List;

import com.greenscan.dto.cart.CartItemDTO;

@Data
public class CreateCartRequestDTO {
    private Long vendorId;
    private String pickupAddressLine1;
    private String pickupAddressLine2;
    private String pickupCity;
    private String pickupState;
    private String pickupPincode;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private String landmark;
    private String scheduledPickupTime;
    private List<CartItemDTO> items;
    private String specialInstructions;
}
