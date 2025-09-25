package com.greenscan.repository;

import com.greenscan.entity.EndUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface EndUserProfileRepository extends JpaRepository<EndUserProfile, Long> {
    
    Optional<EndUserProfile> findByUserIdAndIsActiveTrue(Long userId);
    Optional<EndUserProfile> findByReferralCodeAndIsActiveTrue(String referralCode);
    
    @Modifying
    @Query("UPDATE EndUserProfile ep SET ep.greenCoinsBalance = ep.greenCoinsBalance + :amount WHERE ep.user.id = :userId")
    void addCoinsToBalance(@Param("userId") Long userId, @Param("amount") BigDecimal amount);
    
    @Modifying
    @Query("UPDATE EndUserProfile ep SET ep.greenCoinsBalance = ep.greenCoinsBalance - :amount WHERE ep.user.id = :userId AND ep.greenCoinsBalance >= :amount")
    int deductCoinsFromBalance(@Param("userId") Long userId, @Param("amount") BigDecimal amount);
    
    @Query("SELECT SUM(ep.greenCoinsBalance) FROM EndUserProfile ep WHERE ep.isActive = true")
    BigDecimal getTotalCoinsInCirculation();
    
    @Query("SELECT AVG(ep.ecoScore) FROM EndUserProfile ep WHERE ep.isActive = true")
    Double getAverageEcoScore();
}