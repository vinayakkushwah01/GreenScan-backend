package com.greenscan.repository;

import com.greenscan.entity.NGOProfile;
import com.greenscan.enums.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface NGOProfileRepository extends JpaRepository<NGOProfile, Long> {
    
    Optional<NGOProfile> findByUserIdAndIsActiveTrue(Long userId);
    Optional<NGOProfile> findByRegistrationNumber(String registrationNumber);
    
    Page<NGOProfile> findByApprovalStatusAndIsActiveTrue(ApprovalStatus status, Pageable pageable);
    List<NGOProfile> findByApprovalStatusAndIsActiveTrue(ApprovalStatus status);
    
    @Query("SELECT SUM(np.totalCoinsReceived) FROM NGOProfile np WHERE np.approvalStatus = 'APPROVED' AND np.isActive = true")
    BigDecimal getTotalCoinsReceivedByAllNGOs();
    
    long countByApprovalStatusAndIsActiveTrue(ApprovalStatus status);
}
