package com.greenscan.controller.EndUserOtherController;

import com.greenscan.dto.request.CreateCartRequest;
import com.greenscan.dto.request.EditAiDetectionItemRequest;
import com.greenscan.dto.response.ApiResponse;
import com.greenscan.dto.response.CartResponse;
import com.greenscan.dto.response.PagedResponse;
import com.greenscan.enums.CartStatus;
import com.greenscan.service.impl.CartServiceImpl;
import com.greenscan.service.interfaces.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.greenscan.exception.custom.FileUploadException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/end_users/carts") 
@RequiredArgsConstructor
public class CartAndItemController {

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    private final CartServiceImpl cartService;
    
    // --- 1. Cart Creation Endpoints ---
    
    /**
     * Creates an empty DRAFT cart for the specified user.
     * Maps to: POST /end_users/carts/empty?userId={userId}
     */
    @PostMapping("/empty")
    public ResponseEntity<CartResponse> createCartEmpty(@RequestParam("userId") Long userId) {
        CartResponse response = cartService.createCartEmpty(userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Creates a fully initialized cart with provided details.
     * Maps to: POST /end_users/carts?userId={userId}
     */
    @PostMapping
    public ResponseEntity<CartResponse> createCartCompletion(
            @RequestBody CreateCartRequest request,
            @RequestParam("userId") Long userId) {
        
        CartResponse response = cartService.createCartCompletion(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // --- 2. Cart Modification / Update Endpoints ---

    /**
     * Updates the pickup address and details for an existing cart.
     * Maps to: PUT /end_users/carts/{cartId}/address?userId={userId}
     */
    @PutMapping("/{cartId}/address")
    public ResponseEntity<CartResponse> changePickupAddress(
            @PathVariable Long cartId,
            @RequestBody CreateCartRequest request,
            @RequestParam("userId") Long userId) {
        
        CartResponse response = cartService.changePickupAddress(request, userId, cartId);
        return ResponseEntity.ok(response);
    }

    /**
     * Fills in missing details (vendor, address) for a DRAFT cart.
     * Maps to: PUT /end_users/carts/{cartId}/complete-info?userId={userId}
     */
    @PutMapping("/{cartId}/complete-info")
    public ResponseEntity<CartResponse> makeCartInfoComplete(
            @PathVariable Long cartId,
            @RequestBody CreateCartRequest request,
            @RequestParam("userId") Long userId) {
        
        CartResponse response = cartService.makeCartInfoComp(cartId, userId, request);
        return ResponseEntity.ok(response);
    }
    
    // --- 3. Item Management Endpoints ---

    /**
     * Adds an item (via file upload) to a specific cart.
     * Maps to: POST /end_users/carts/{cartId}/items/add?userId={userId}&file={file}
     */
    @PostMapping("/{cartId}/items/add")
    public ResponseEntity<CartResponse> addItemToCart(
            @PathVariable Long cartId,
            @RequestParam("file") MultipartFile file, // Placeholder for multipart file upload
            @RequestParam("userId") Long userId) {
          try{      File f = convertMultiPartToFile(file);
        // NOTE: itemPreProcess.getAiScannedItem requires a real file upload handling mechanism
        CartResponse response = cartService.addItemToCart(f, userId, cartId);
        return ResponseEntity.ok(response);
        }catch(Exception e){
            throw new FileUploadException("Fail to convert multipart to file ");
        }
    }
    
    // Confirms Ai detetcted Obj 
     @PatchMapping("/{itemId}/confirm")
    public ResponseEntity<ApiResponse<String>> confirmItemIdentification(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam("confirmed") boolean confirmed) {

        cartService.confirmItemIdentification(cartId, itemId, confirmed);
        String message = confirmed
                ? "Item identification confirmed successfully."
                : "Item marked for user review/edit.";
        return ResponseEntity.ok(new ApiResponse<>(true, message, null));
    }
   
    // Edit Ai detetcted Obj 
    @PatchMapping("/{itemId}/edit")
    public ResponseEntity<ApiResponse<String>> editAiDetectedItem(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @Valid @RequestBody EditAiDetectionItemRequest request) {

        cartService.editAiDetectedItem(cartId, itemId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "AI-detected item updated successfully, But require Vendor verification", null));
    }

    //Delete cart item 
    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeItemFromCart(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam ("userId") Long userId) {

        CartResponse response = cartService.removeItemFromCart(cartId, itemId, userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Item removed from cart successfully.", response)
        );
    }

    // --- 4. Cart Retrieval Endpoints ---
    
    /**
     * Gets a paged list of all carts owned by the specified user.
     * Maps to: GET /end_users/carts/me?userId={userId}&page={page}&size={size}
     */
    @GetMapping("/me")
    public PagedResponse<CartResponse> getUserCarts(
            @RequestParam("userId") Long userId, 
            Pageable pageable) {
        
        return cartService.getUserCarts(userId, pageable);
    }

    
    /**
     * Retrieves a specific cart by its unique ID.
     * Maps to: GET /end_users/carts/{cartId}
     */
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCartById(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.getCartById(cartId));
    }

    /**
     * Retrieves a specific cart using its unique cart number.
     * Maps to: GET /end_users/carts/number/{cartNumber}
     */
    @GetMapping("/number/{cartNumber}")
    public ResponseEntity<CartResponse> getCartByNumber(@PathVariable String cartNumber) {
        return ResponseEntity.ok(cartService.getCartByNumber(cartNumber));
    }
    
    /**
     * Gets carts for the user filtered by a specific status.
     * Maps to: GET /end_users/carts/me/status?userId={userId}&status={status}
     */
    @GetMapping("/me/status")
    public ResponseEntity<List<CartResponse>> getUserCartsByStatus(
            @RequestParam("userId") Long userId,
            @RequestParam CartStatus status) {
        
        List<CartResponse> carts = cartService.getUserCartsByStatus(userId, status);
        return ResponseEntity.ok(carts);
    }
    
}