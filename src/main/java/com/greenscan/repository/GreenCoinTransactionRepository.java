package com.greenscan.repository;

import com.greenscan.entity.GreenCoinTransaction;
import com.greenscan.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GreenCoinTransactionRepository extends JpaRepository<GreenCoinTransaction, Long> {
    
    Page<GreenCoinTransaction> findByUserIdAndIsActiveTrue(Long userId, Pageable pageable);
    List<GreenCoinTransaction> findByUserIdAndTransactionTypeAndIsActiveTrue(Long userId, TransactionType type);
    
    @Query("SELECT gct FROM GreenCoinTransaction gct WHERE gct.user.id = :userId AND gct.createdAt BETWEEN :startDate AND :endDate AND gct.isActive = true ORDER BY gct.createdAt DESC")
    List<GreenCoinTransaction> findUserTransactionsByDateRange(
        @Param("userId") Long userId, 
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT SUM(gct.amount) FROM GreenCoinTransaction gct WHERE gct.transactionType = :type AND gct.isActive = true")
    BigDecimal getTotalAmountByTransactionType(@Param("type") TransactionType type);
    
    @Query("SELECT gct.transactionType, COUNT(gct), SUM(gct.amount) FROM GreenCoinTransaction gct WHERE gct.isActive = true GROUP BY gct.transactionType")
    List<Object[]> getTransactionStats();
}
