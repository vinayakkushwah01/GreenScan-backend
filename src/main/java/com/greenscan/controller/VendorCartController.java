package com.greenscan.controller;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.greenscan.dto.request.AcceptCartRequest;
import com.greenscan.dto.response.CartResponse;
import com.greenscan.dto.response.PagedResponse;
import com.greenscan.enums.CartStatus;
import com.greenscan.service.impl.CartServiceImpl;
import com.greenscan.service.interfaces.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vendor/cart")
@RequiredArgsConstructor
public class VendorCartController {

    private final CartServiceImpl cartService;

    // ✅ 1. Get all carts for a vendor
    @GetMapping("/{vendorId}/all")
    public ResponseEntity<PagedResponse<CartResponse>> getVendorCarts(
            @PathVariable Long vendorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size ,Sort.by("createdAt").descending());
        return ResponseEntity.ok(cartService.getVendorCarts(vendorId, pageable));
    }

    // ✅ 2. Get carts by status
    @GetMapping("/{vendorId}/status/{status}")
    public ResponseEntity<?> getVendorCartsByStatus(
            @PathVariable Long vendorId,
            @PathVariable CartStatus status) {
        return ResponseEntity.ok(cartService.getVendorCartsByStatus(vendorId, status));
    }

    // ✅ 3. Accept a user’s pickup request
    @PostMapping("/{vendorId}/accept")
    public ResponseEntity<CartResponse> acceptCart(
            @PathVariable Long vendorId,
            @RequestBody AcceptCartRequest request) {
        return ResponseEntity.ok(cartService.acceptCart(request, vendorId));
    }

    // ✅ 4. Reject a cart
    @PostMapping("/{vendorId}/reject/{cartId}")
    public ResponseEntity<CartResponse> rejectCart(
            @PathVariable Long vendorId,
            @PathVariable Long cartId,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(cartService.rejectCart(cartId, reason, vendorId));
    }

    // ✅ 5. Mark cart as collected
    // @PostMapping("/{vendorId}/collected/{cartId}")
    // public ResponseEntity<CartResponse> markAsCollected(
    //         @PathVariable Long vendorId,
    //         @PathVariable Long cartId) {
    //     return ResponseEntity.ok(cartService.markAsCollected(cartId, vendorId));
    // }

    // ✅ 6. Complete a cart
    @PostMapping("/{vendorId}/complete/{cartId}")
    public ResponseEntity<CartResponse> completeCart(
            @PathVariable Long vendorId,
            @PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.completeCart(cartId, vendorId));
    }

    // ✅ 7. Get pending pickups
    @GetMapping("/{vendorId}/pending-pickups")
    public ResponseEntity<?> getPendingPickupsForVendor(@PathVariable Long vendorId) {
        return ResponseEntity.ok(cartService.getPendingPickupsForVendor(vendorId));
    }

    //Assig Assiatent 
    @PutMapping("/{vendorId}/assign-pickup-assistant")
    public ResponseEntity<CartResponse> assignPickupAssistant(
        @RequestParam Long cartId,
        @RequestParam Long pickupAssistantUserId,
        @PathVariable Long vendorId) {

        CartResponse response = cartService.assignPickupAssistant(cartId, pickupAssistantUserId, vendorId);
        return ResponseEntity.ok(response);
    }

}
