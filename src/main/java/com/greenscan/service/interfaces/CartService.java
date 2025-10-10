package com.greenscan.service.interfaces;

import com.greenscan.dto.request.*;
import com.greenscan.dto.response.CartResponse;
import com.greenscan.dto.response.PagedResponse;
import com.greenscan.entity.Cart;
import com.greenscan.enums.CartStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {
    
    /**
     * Create a new cart for the authenticated user
     */
    CartResponse createCart(CreateCartRequest request, Long userId);
    
    /**
     * Get cart by ID
     */
    CartResponse getCartById(Long cartId);
    
    /**
     * Get cart by cart number
     */
    CartResponse getCartByNumber(String cartNumber);
    
    /**
     * Get all carts for a user with pagination
     */
    PagedResponse<CartResponse> getUserCarts(Long userId, Pageable pageable);
    
    /**
     * Get all carts for a vendor with pagination
     */
    PagedResponse<CartResponse> getVendorCarts(Long vendorId, Pageable pageable);
    
    /**
     * Get all carts for a pickup assistant with pagination
     */
    PagedResponse<CartResponse> getPickupAssistantCarts(Long assistantId, Pageable pageable);
    
    /**
     * Get carts by status for a user
     */
    List<CartResponse> getUserCartsByStatus(Long userId, CartStatus status);
    
    /**
     * Get carts by status for a vendor
     */
    List<CartResponse> getVendorCartsByStatus(Long vendorId, CartStatus status);
    
    /**
     * Add item to cart
     */
    CartResponse addItemToCart(AddItemRequest request, Long userId);
    
    /**
     * Remove item from cart
     */
    CartResponse removeItemFromCart(Long cartId, Long itemId, Long userId);
    
    /**
     * Request pickup for a cart
     */
    CartResponse requestPickup(RequestPickupRequest request, Long userId);
    
    /**
     * Assign vendor to cart (Admin or Auto-assignment)
     */
    CartResponse assignVendorToCart(Long cartId, Long vendorId, Long assignedBy);
    
    /**
     * Vendor accepts cart
     */
    CartResponse acceptCart(AcceptCartRequest request, Long vendorId);
    
    /**
     * Vendor rejects cart
     */
    CartResponse rejectCart(Long cartId, String reason, Long vendorId);
    
    /**
     * Assign pickup assistant to cart
     */
    CartResponse assignPickupAssistant(Long cartId, Long assistantId, Long vendorId);
    
    /**
     * Start pickup process
     */
    CartResponse startPickup(Long cartId, Long assistantId);
    
    /**
     * Complete pickup process
     */
    CartResponse completePickup(Long cartId, Long assistantId);
    
    /**
     * Mark cart as collected
     */
    CartResponse markAsCollected(Long cartId, Long vendorId);
    
    /**
     * Complete cart and award coins
     */
    CartResponse completeCart(Long cartId, Long vendorId);
    
    /**
     * Cancel cart
     */
    CartResponse cancelCart(Long cartId, String reason, Long cancelledBy);
    
    /**
     * Update cart status
     */
    CartResponse updateCartStatus(Long cartId, CartStatus newStatus, String notes, Long changedBy);
    
    /**
     * Get unassigned carts by city
     */
    List<CartResponse> getUnassignedCartsByCity(String city);
    
    /**
     * Get pending pickups for vendor
     */
    List<CartResponse> getPendingPickupsForVendor(Long vendorId);
    
    /**
     * Auto-assign vendor based on location and availability
     */
    CartResponse autoAssignVendor(Long cartId);
    
    /**
     * Recalculate cart totals
     */
    void recalculateCartTotals(Long cartId);
    
    /**
     * Get cart entity (for internal use)
     */
    Cart getCartEntity(Long cartId);
    
    /**
     * Convert Cart entity to CartResponse
     */
    CartResponse convertToResponse(Cart cart);
}
