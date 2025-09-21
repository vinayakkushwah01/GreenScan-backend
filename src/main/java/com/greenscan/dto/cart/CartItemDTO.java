package com.greenscan.dto.cart;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

import com.greenscan.dto.base.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class CartItemDTO extends BaseDTO {
    private Long cartId;
    private String category;
    private String subCategory;
    private String materialType;
    private BigDecimal weightKg;
    private BigDecimal ratePerKg;
    private BigDecimal estimatedValue;
    private BigDecimal coinsEarned;
    private String itemImageUrl;
}
