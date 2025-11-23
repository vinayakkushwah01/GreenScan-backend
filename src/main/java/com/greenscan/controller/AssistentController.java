package com.greenscan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenscan.dto.response.CartResponse;
import com.greenscan.service.impl.CartServiceImpl;

@RestController
@RequestMapping("/pickup-assistant")

public class AssistentController {
    @Autowired
    private  CartServiceImpl cartService;


    /**
     * Start pickup for a cart
     */
    @PutMapping("/start")
    public ResponseEntity<CartResponse> startPickup(
            @RequestParam Long cartId,
            @RequestParam Long assistantMainUserId) {

        CartResponse response = cartService.startPickup(cartId, assistantMainUserId);
        return ResponseEntity.ok(response);
    }

    /**
     * Complete pickup for a cart
     */
    @PutMapping("/complete")
    public ResponseEntity<CartResponse> completePickup(
            @RequestParam Long cartId,
            @RequestParam Long assistantMainUserId) {

        CartResponse response = cartService.completePickup(cartId, assistantMainUserId);
        return ResponseEntity.ok(response);
    }
}
   

