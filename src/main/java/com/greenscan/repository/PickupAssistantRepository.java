// PickupAssistantRepository.java
package com.greenscan.repository;

import com.greenscan.entity.PickupAssistant;
import com.greenscan.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PickupAssistantRepository extends JpaRepository<PickupAssistant, Long> {
    
    Optional<PickupAssistant> findByUserIdAndIsActiveTrue(Long userId);
    Optional<PickupAssistant> findByAssistantCodeAndIsActiveTrue(String assistantCode);
    
    List<PickupAssistant> findByVendorIdAndIsActiveTrue(Long vendorId);
    List<PickupAssistant> findByVendorIdAndApprovalStatusAndIsActiveTrue(Long vendorId, ApprovalStatus status);
    
    @Query("SELECT pa FROM PickupAssistant pa WHERE pa.vendor.id = :vendorId AND pa.isAvailable = true AND pa.isOnDuty = true AND pa.approvalStatus = 'APPROVED' AND pa.isActive = true")
    List<PickupAssistant> findAvailableAssistantsByVendor(@Param("vendorId") Long vendorId);
    
    @Query("SELECT pa FROM PickupAssistant pa WHERE pa.currentCartId = :cartId AND pa.isActive = true")
    Optional<PickupAssistant> findByCurrentCartId(@Param("cartId") Long cartId);
    
    long countByVendorIdAndApprovalStatusAndIsActiveTrue(Long vendorId, ApprovalStatus status);
}
