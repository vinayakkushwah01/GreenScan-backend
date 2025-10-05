package com.greenscan.repository;

import com.greenscan.entity.VendorProfile;
import com.greenscan.enums.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorProfileRepository extends JpaRepository<VendorProfile, Long> {
    
    Optional<VendorProfile> findByUserIdAndIsActiveTrue(Long userId);
    Optional<VendorProfile> findByBusinessRegistrationNumber(String registrationNumber);
    
    Page<VendorProfile> findByApprovalStatusAndIsActiveTrue(ApprovalStatus status, Pageable pageable);
    
    @Query("SELECT vp FROM VendorProfile vp WHERE vp.approvalStatus = 'APPROVED' AND vp.serviceCities LIKE %:city% AND vp.isActive = true")
    List<VendorProfile> findApprovedVendorsByCity(@Param("city") String city);
    
    @Query("SELECT vp FROM VendorProfile vp WHERE vp.approvalStatus = 'APPROVED' " +
           "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(vp.user.latitude)) " +
           "* cos(radians(vp.user.longitude) - radians(:longitude)) + sin(radians(:latitude)) " +
           "* sin(radians(vp.user.latitude)))) <= vp.serviceRadiusKm AND vp.isActive = true")
    List<VendorProfile> findVendorsWithinRadius(@Param("latitude") Double latitude, @Param("longitude") Double longitude);
    
    long countByApprovalStatusAndIsActiveTrue(ApprovalStatus status);
    Optional<VendorProfile> findByUserId(Long mainUserId);
}
