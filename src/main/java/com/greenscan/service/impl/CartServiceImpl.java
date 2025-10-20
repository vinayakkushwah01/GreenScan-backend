package com.greenscan.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import com.greenscan.dto.request.AcceptCartRequest;
import com.greenscan.dto.request.AddItemRequest;
import com.greenscan.dto.request.CreateCartRequest;
import com.greenscan.dto.request.RequestPickupRequest;
import com.greenscan.dto.response.CartItemResponse;
import com.greenscan.dto.response.CartResponse;
import com.greenscan.dto.response.CartStatusHistoryResponse;
import com.greenscan.dto.response.PagedResponse;
import com.greenscan.dto.response.UserResponse;
import com.greenscan.entity.Cart;
import com.greenscan.entity.CartItem;
import com.greenscan.entity.CartStatusHistory;
import com.greenscan.entity.MainUser;
import com.greenscan.entity.VendorProfile;
import com.greenscan.enums.CartStatus;
import com.greenscan.exception.custom.CartNotFound;
import com.greenscan.exception.custom.ResourceNotFoundException;
import com.greenscan.exception.custom.UnAuthorizedException;
import com.greenscan.repository.CartRepository;
import com.greenscan.repository.EndUserProfileRepository;
import com.greenscan.repository.MainUserRepository;
import com.greenscan.repository.VendorProfileRepository;
import com.greenscan.service.interfaces.CartService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class CartServiceImpl  implements CartService{
    private final CartRepository cartRepository;
    private final EndUserProfileRepository endUserProfileRepository;
    private final MainUserRepository mainUserRepository;
    private final VendorProfileRepository vendorProfileRepository;


    public Cart convertToEntity(CreateCartRequest request, MainUser user, VendorProfile vendor) {
        if (request == null || user == null) {
            throw new IllegalArgumentException("Request and user cannot be null");
        }

        Cart cart = new Cart();

        // Basic info
        cart.setUser(user);
        cart.setVendor(vendor.getUser()); // can be null if vendor not chosen yet
        cart.setStatus(com.greenscan.enums.CartStatus.DRAFT);

        // Generate unique cart number (you can replace this logic as needed)
        cart.setCartNumber("CART-" + System.currentTimeMillis());

        // Pickup details
        cart.setPickupInstructions(request.getPickupInstructions());
        cart.setPickupAddressLine1(request.getPickupAddressLine1());
        cart.setPickupAddressLine2(request.getPickupAddressLine2());
        cart.setPickupCity(request.getPickupCity());
        cart.setPickupPincode(request.getPickupPincode());
        cart.setPickupLatitude(request.getPickupLatitude());
        cart.setPickupLongitude(request.getPickupLongitude());

        // Defaults
        cart.setTotalEstimatedWeight(BigDecimal.ZERO);
        cart.setTotalActualWeight(BigDecimal.ZERO);
        cart.setTotalEstimatedCoins(BigDecimal.ZERO);
        cart.setTotalActualCoins(BigDecimal.ZERO);
        cart.setPickupRequestedAt(LocalDateTime.now());

        return cart;
    }

    @Override
    public CartResponse createCartCompletion(CreateCartRequest request, Long userId) {
        MainUser user = mainUserRepository.findById(userId)
        .orElseThrow(() ->new ResourceNotFoundException("User not found"));
       
        VendorProfile vendor = null;
        if (request.getVendorId() != null) {
            vendor = vendorProfileRepository.findById(request.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        }

        Cart cart = convertToEntity(request, user, vendor);
        return convertToResponse(cartRepository.save(cart));
       
    }
    @Override
    public CartResponse createCartEmpty(Long userId) {
        MainUser user = mainUserRepository.findById(userId)
            .orElseThrow(()->new ResourceNotFoundException("Main User not found for given id"));
        return convertToResponse( cartRepository.save(convertToEntity(null, user, null)));
    }

    @Override
    public CartResponse changeVendorAssign(Long cartId, Long userId, Long vendorId) {
        MainUser user = mainUserRepository.findById(userId)
            .orElseThrow(()-> new ResourceNotFoundException("User With given Id not found "));
        VendorProfile vendor = vendorProfileRepository.findById(vendorId)
            .orElseThrow(()-> new ResourceNotFoundException("vendor with given iD not found "));
        
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFound("Cart with given id not found "));
        cart.setVendor(vendor.getUser());
        return convertToResponse( cartRepository.save(cart));
    }
    
    @Override
    public boolean checkCartInfoCompl(Long userId, Long cartId) {
       
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFound("Cart not found for id "+cartId));
      if( cart.getUser()!= mainUserRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found for id "+userId))){
        throw new AuthorizationServiceException("user not authorized");
      }
      if(cart.getVendor() == null) return false;
      if(cart.getPickupAddressLine1() == null) return false;
      if(cart.getPickupAddressLine2() == null)  return false;
      if(cart.getPickupCity() == null) return false;
      if(cart.getPickupLatitude() == null) return false;
      if(cart.getPickupLongitude() == null) return false;
      if(cart.getPickupPincode() == null) return false;
      return true;

    }
   
    @Override
    public CartResponse makeCartInfoComp(Long cartId, Long userId, CreateCartRequest request) {
          Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFound("Cart not found for id "+cartId));
      if( cart.getUser()!= mainUserRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found for id "+userId))){
        throw new AuthorizationServiceException("user not authorized");
      }
      if(cart.getVendor() == null) {
        if(request.getVendorId()!= null){
            VendorProfile v =  vendorProfileRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Vendor not found for id "+request.getVendorId()));
            cart.setVendor(v.getUser());
        }
      }
      if(cart.getPickupAddressLine1() == null) {
        cart.setPickupAddressLine1(request.getPickupAddressLine1());
      }
      if(cart.getPickupAddressLine2() == null)  {
        cart.setPickupAddressLine2(request.getPickupAddressLine2());
      }
      if(cart.getPickupCity() == null) {
        cart.setPickupCity(request.getPickupCity());
      }
      if(cart.getPickupLatitude() == null) {
        cart.setPickupLatitude(request.getPickupLatitude());
      }
      if(cart.getPickupLongitude() == null) {
        cart.setPickupLongitude(request.getPickupLongitude());
      }
      if(cart.getPickupPincode() == null) {
        cart.setPickupPincode(request.getPickupPincode());
      }
      if(cart.getPickupInstructions() == null){
        cart.setPickupInstructions(request.getPickupInstructions());
      }
     return convertToResponse(cartRepository.save(cart));
     
    }

    @Override
    public CartResponse changePickupAddress(CreateCartRequest request, Long userId , Long cartId) {
             Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFound("Cart not found for id "+cartId));
        if( cart.getUser()!= mainUserRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found for id "+userId))){
            throw new AuthorizationServiceException("user not authorized");
        }
        if(request.getPickupAddressLine1() == null) {
            cart.setPickupAddressLine1(request.getPickupAddressLine1());
        }
        if(request.getPickupAddressLine2() == null)  {
            cart.setPickupAddressLine2(request.getPickupAddressLine2());
        }
        if(request.getPickupCity() == null) {
            cart.setPickupCity(request.getPickupCity());
        }
        if(request.getPickupLatitude() == null) {
            cart.setPickupLatitude(request.getPickupLatitude());
        }
        if(request.getPickupLongitude() == null) {
            cart.setPickupLongitude(request.getPickupLongitude());
        }
        if(request.getPickupPincode() == null) {
            cart.setPickupPincode(request.getPickupPincode());
        }
        if(request.getPickupInstructions() == null){
            cart.setPickupInstructions(request.getPickupInstructions());
        }
        return convertToResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse getCartById(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFound("Cart not found for id "+cartId));
        return convertToResponse(cart);
    }

    @Override
    public CartResponse getCartByNumber(String cartNumber) {
       return convertToResponse(cartRepository.findByCartNumberAndIsActiveTrue(cartNumber)
        .orElseThrow(()->new  CartNotFound("Cart not found for number  "+cartNumber)));
    }

    @Override
    public PagedResponse<CartResponse> getUserCarts(Long userId, Pageable pageable) {
        var page = cartRepository.findByUserIdAndIsActiveTrue(userId, pageable);
        var content = page.getContent().stream()
                .map(this::convertToResponse)
                .toList();
            
        PagedResponse<CartResponse> response = new PagedResponse<>();
        response.setContent(content);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());

        return response;
    }
   
    @Override
    public PagedResponse<CartResponse> getVendorCarts(Long vendorId, Pageable pageable) {
        var page = cartRepository.findByVendorIdAndIsActiveTrue(vendorId, pageable);

        List<CartResponse> content = page.getContent().stream()
                .map(this::convertToResponse)
                .toList();

        PagedResponse<CartResponse> response = new PagedResponse<>();
        response.setContent(content);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());

        return response;
    }

    @Override
    public PagedResponse<CartResponse> getPickupAssistantCarts(Long assistantId, Pageable pageable) {
        var page = cartRepository.findByPickupAssistantIdAndIsActiveTrue(assistantId, pageable);

        List<CartResponse> content = page.getContent().stream()
                .map(this::convertToResponse)
                .toList();

        PagedResponse<CartResponse> response = new PagedResponse<>();
        response.setContent(content);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());

        return response;
    }

   @Override
    public List<CartResponse> getUserCartsByStatus(Long userId, CartStatus status) {
        List<Cart> carts = cartRepository.findByUserIdAndStatusAndIsActiveTrue(userId, status);
        return carts.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<CartResponse> getVendorCartsByStatus(Long vendorId, CartStatus status) {
        List<Cart> carts = cartRepository.findByVendorIdAndStatusAndIsActiveTrue(vendorId, status);
        return carts.stream()
        .map(this::convertToResponse)
        .toList();
    }
    
    @Override
    public CartResponse assignVendorToCart(Long cartId, Long vendorId, Long assignedBy) {
       
        VendorProfile vendor = vendorProfileRepository.findById(vendorId)
            .orElseThrow(()-> new ResourceNotFoundException("vendor with given iD not found "));
        
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFound("Cart with given id not found "));
        cart.setVendor(vendor.getUser());
        return convertToResponse( cartRepository.save(cart));
    }

   
    @Override
    public CartResponse addItemToCart(AddItemRequest request, Long userId) {
        Cart cart = cartRepository.findById(request.getCartId()).orElseThrow(()-> new CartNotFound("Cart not found for id "+request.getCartId()));
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addItemToCart'");
    }
    @Override
    public CartResponse removeItemFromCart(Long cartId, Long itemId, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeItemFromCart'");
    }
    @Override
    public CartResponse requestPickup(RequestPickupRequest request, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requestPickup'");
    }

    // Vendor Services related to cart 


    @Override
    public CartResponse acceptCart(AcceptCartRequest request, Long vendorId) {
        
        Cart cart = cartRepository.findById(request.getCartId()).orElseThrow(()-> new CartNotFound("Cart not found for id "+request.getCartId()));
        if(cart.getVendor().getId() != vendorId ) {
            throw new UnAuthorizedException("you are not authorize to make change in others cart ");     
        }   
        cart.setStatus(CartStatus.PICKUP_SCHEDULED);
            cart.setUpdatedAt(LocalDateTime.now());
            cart.setPickupScheduledAt(request.getEstimatedPickupTime());
          return convertToResponse(cartRepository.save(cart));   
    }

    @Override
    public CartResponse rejectCart(Long cartId, String reason, Long vendorId) {
       
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFound("Cart not found for id "+cartId));
        if(cart.getVendor().getId() != vendorId ) {
            throw new UnAuthorizedException("you are not authorize to make change in others cart ");     
        }   

        cart.setStatus(CartStatus.REJECTED);
            cart.setUpdatedAt(LocalDateTime.now());
            cart.setCancelledByUserId(vendorId);
            cart.setCancellationReason(reason);
            cart.setCancelledAt(LocalDateTime.now());
        return convertToResponse(cartRepository.save(cart));
    
    }
    
   //we need to remove this method  
    @Override
    public CartResponse assignPickupAssistant(Long cartId, Long assistantId, Long vendorId) {
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignPickupAssistant'");
    }

    //Assistant Service related to cart 

    @Override
    public CartResponse startPickup(Long cartId, Long assistantId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startPickup'");
    }
    @Override
    public CartResponse completePickup(Long cartId, Long assistantId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'completePickup'");
    }
    @Override
    public CartResponse markAsCollected(Long cartId, Long vendorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markAsCollected'");
    }


    //Vendor methods 
    @Override
    public CartResponse cancelCart(Long cartId, String reason, Long cancelledBy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelCart'");
    }

    @Override
    public CartResponse completeCart(Long cartId, Long vendorId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFound("Cart With id not found "+ cartId));
            cart.setStatus(CartStatus.COMPLETED);
            cart.setUpdatedAt(LocalDateTime.now());
            return convertToResponse(cartRepository.save(cart));
    }


    @Override
    public CartResponse updateCartStatus(Long cartId, CartStatus newStatus, String notes, Long changedBy) {
       Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFound("Cart not found for id "+cartId));
        cart.setStatus(newStatus);
        cart.setUpdatedAt(LocalDateTime.now());
        return convertToResponse(cartRepository.save(cart));
    }


    @Override
    public List<CartResponse> getUnassignedCartsByCity(String city) {
       return  cartRepository.findUnassignedCartsByCity(city)
        .stream()
        .map(this::convertToResponse)
        .toList();
    }
   
    @Override
    public List<CartResponse> getPendingPickupsForVendor(Long vendorId) {
       return cartRepository.findPendingPickupsByVendor(vendorId)
                .stream().map(this::convertToResponse)
                .toList();
    }


    @Override
    public CartResponse autoAssignVendor(Long cartId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'autoAssignVendor'");
    }
    @Override
    public void recalculateCartTotals(Long cartId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recalculateCartTotals'");
    }

    @Override
    public Cart getCartEntity(Long cartId) {
       return cartRepository.findById(cartId).orElseThrow(()-> new CartNotFound("Cart with id not found "+cartId));
    }


    @Override
    public CartResponse convertToResponse(Cart cart) {
         if (cart == null) return null;

        CartResponse response = new CartResponse();

        // Basic info
        response.setId(cart.getId());
        response.setCartNumber(cart.getCartNumber());
        response.setStatus(cart.getStatus().name());
        response.setCreatedAt(cart.getCreatedAt());
        response.setPickupScheduledAt(cart.getPickupScheduledAt());
        response.setPickupCompletedAt(cart.getPickupCompletedAt());

        // Totals
        response.setTotalEstimatedWeight(cart.getTotalEstimatedWeight());
        response.setTotalActualWeight(cart.getTotalActualWeight());
        response.setTotalEstimatedCoins(cart.getTotalEstimatedCoins());
        response.setTotalActualCoins(cart.getTotalActualCoins());

        // Pickup info
        response.setPickupAddress(
            String.join(", ",
                cart.getPickupAddressLine1() != null ? cart.getPickupAddressLine1() : "",
                cart.getPickupAddressLine2() != null ? cart.getPickupAddressLine2() : "",
                cart.getPickupCity() != null ? cart.getPickupCity() : "",
                cart.getPickupPincode() != null ? cart.getPickupPincode() : ""
            ).replaceAll(", $", "")
        );
        response.setPickupInstructions(cart.getPickupInstructions());
        response.setVendorNotes(cart.getVendorNotes());

        // Latitude & Longitude (convert Double → Long safely)
        if (cart.getPickupLatitude() != null)
            response.setLattiude(cart.getPickupLatitude().longValue());
        if (cart.getPickupLongitude() != null)
            response.setLogitude(cart.getPickupLongitude().longValue());

        // Vendor & pickup assistant
        if (cart.getVendor() != null)
            response.setVendor(UserResponse.fromMainUser(cart.getVendor()));

        if (cart.getPickupAssistant() != null)
            response.setPickupAssistant(UserResponse.fromMainUser(cart.getPickupAssistant()));

        // Items
        if (cart.getItems() != null) {
            response.setItems(
                cart.getItems().stream()
                    .map(this::convertCartItemToResponse)
                    .toList()
            );
        }

        // Status history
        if (cart.getStatusHistory() != null) {
            response.setStatusHistory(
                cart.getStatusHistory().stream()
                    .map(this::convertStatusHistoryToResponse)
                    .toList()
            );
        }

        // Computed fields
        response.setVerifiedItemCount(cart.getVerifiedItemCount());
        response.setPendingVerificationCount(cart.getPendingVerificationCount());

        return response;
    }


    private CartItemResponse convertCartItemToResponse(CartItem item) {
        if (item == null) return null;

        CartItemResponse res = new CartItemResponse();

        res.setId(item.getId());
        res.setItemName(item.getItemName());
        res.setMaterialType(item.getMaterialType());
        res.setEstimatedWeight(item.getEstimatedWeight());
        res.setActualWeight(item.getActualWeight());
        res.setEstimatedCoins(item.getEstimatedCoins());
        res.setActualCoins(item.getActualCoins());
        res.setIsRecyclable(item.getIsRecyclable());
        res.setStatus(item.getStatus() != null ? item.getStatus().name() : null);
        res.setImageUrl(item.getImageUrl());
        res.setAiConfidenceScore(item.getAiConfidenceScore());
        res.setUserEdited(item.getUserEdited());
        res.setVendorNotes(item.getVendorNotes());
        res.setRejectionReason(item.getRejectionReason());
        res.setCreatedAt(item.getCreatedAt());

        return res;
    }


    private CartStatusHistoryResponse convertStatusHistoryToResponse(CartStatusHistory history) {
        if (history == null) return null;

        CartStatusHistoryResponse res = new CartStatusHistoryResponse();

        res.setId(history.getId());
        res.setCartId(history.getCart() != null ? history.getCart().getId() : null);
        res.setStatus(history.getToStatus()); // to_status = current/new status
        res.setChangedAt(history.getCreatedAt()); // inherited from BaseEntity
        res.setChangedByUserId(history.getChangedByUserId());
        res.setRemarks(history.getNotes()); // map 'notes' → 'remarks'

        return res;
    }



    
}
