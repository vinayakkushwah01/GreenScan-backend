package com.greenscan.repository;

import com.greenscan.entity.CartItem;
import com.greenscan.enums.ItemStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findByCartIdAndIsActiveTrue(Long cartId);
    List<CartItem> findByStatusAndIsActiveTrue(ItemStatus status);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.vendor.id = :vendorId AND ci.status = 'PENDING_VENDOR_VERIFICATION' AND ci.isActive = true")
    List<CartItem> findPendingVerificationItemsByVendor(@Param("vendorId") Long vendorId);
    
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.status IN ('VERIFIED', 'USER_CONFIRMED') AND ci.isActive = true")
    long countVerifiedItemsByCart(@Param("cartId") Long cartId);
    
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.status = 'PENDING_VENDOR_VERIFICATION' AND ci.isActive = true")
    long countPendingVerificationItemsByCart(@Param("cartId") Long cartId);
    
    @Query("SELECT ci.materialType, COUNT(ci), SUM(ci.actualWeight) FROM CartItem ci WHERE ci.cart.vendor.id = :vendorId AND ci.cart.status = 'COMPLETED' AND ci.isActive = true GROUP BY ci.materialType")
    List<Object[]> getMaterialStatsForVendor(@Param("vendorId") Long vendorId);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.userEdited = true AND ci.status = 'USER_EDITED' AND ci.isActive = true")
    List<CartItem> findUserEditedItems();
}
