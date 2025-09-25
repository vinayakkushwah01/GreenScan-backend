package com.greenscan.repository;

import com.greenscan.entity.Cart;
import com.greenscan.enums.CartStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    Optional<Cart> findByCartNumberAndIsActiveTrue(String cartNumber);
    
    Page<Cart> findByUserIdAndIsActiveTrue(Long userId, Pageable pageable);
    Page<Cart> findByVendorIdAndIsActiveTrue(Long vendorId, Pageable pageable);
    Page<Cart> findByPickupAssistantIdAndIsActiveTrue(Long assistantId, Pageable pageable);
    
    List<Cart> findByUserIdAndStatusAndIsActiveTrue(Long userId, CartStatus status);
    List<Cart> findByVendorIdAndStatusAndIsActiveTrue(Long vendorId, CartStatus status);
    List<Cart> findByPickupAssistantIdAndStatusAndIsActiveTrue(Long assistantId, CartStatus status);
    
    @Query("SELECT c FROM Cart c WHERE c.status = :status AND c.pickupScheduledAt BETWEEN :startDate AND :endDate AND c.isActive = true")
    List<Cart> findCartsByStatusAndPickupDateRange(
        @Param("status") CartStatus status, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT c FROM Cart c WHERE c.user.city = :city AND c.vendor IS NULL AND c.status = 'PENDING_VENDOR_ASSIGNMENT' AND c.isActive = true")
    List<Cart> findUnassignedCartsByCity(@Param("city") String city);
    
    @Query("SELECT COUNT(c) FROM Cart c WHERE c.vendor.id = :vendorId AND c.status IN :statuses AND DATE(c.createdAt) = CURRENT_DATE")
    long countTodayCartsByVendorAndStatuses(@Param("vendorId") Long vendorId, @Param("statuses") List<CartStatus> statuses);
    
    @Query("SELECT c FROM Cart c WHERE c.vendor.id = :vendorId AND c.status = 'PICKUP_REQUESTED' AND c.isActive = true ORDER BY c.pickupRequestedAt ASC")
    List<Cart> findPendingPickupsByVendor(@Param("vendorId") Long vendorId);
    
    long countByStatusAndIsActiveTrue(CartStatus status);
    
    @Query("SELECT COUNT(c) FROM Cart c WHERE c.user.id = :userId AND c.status = :status AND c.isActive = true")
    long countByUserIdAndStatusAndIsActiveTrue(@Param("userId") Long userId, @Param("status") CartStatus status);
}