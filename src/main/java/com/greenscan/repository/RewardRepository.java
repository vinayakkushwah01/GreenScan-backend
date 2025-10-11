package com.greenscan.repository;
import com.greenscan.entity.Reward;
import com.greenscan.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {


    List<Reward> findByIsActiveTrue();

    // ✅ Find by approval status
    List<Reward> findByApprovalStatus(ApprovalStatus status);

    // ✅ Find by category (optional filter)
    List<Reward> findByCategoryAndIsActiveTrue(String category);

    // ✅ Find by approval status and active
    List<Reward> findByApprovalStatusAndIsActiveTrue(ApprovalStatus status);
    // ✅ Find by approval status, category, and active
    List<Reward> findByApprovalStatusAndCategoryAndIsActiveTrue(ApprovalStatus status, String category);

    // ✅ Custom: find rewards available for redemption
    default List<Reward> findAvailableRewards(List<Reward> rewards) {
        return rewards.stream()
                .filter(Reward::isAvailable)
                .toList();
    }


     // ✅ By monetary value (ascending)
    List<Reward> findByApprovalStatusAndIsActiveTrueOrderByMonetaryValueAsc(ApprovalStatus status);

    // ✅ By monetary value (descending)
    List<Reward> findByApprovalStatusAndIsActiveTrueOrderByMonetaryValueDesc(ApprovalStatus status);

    // ✅ By coins required (ascending)
    List<Reward> findByApprovalStatusAndIsActiveTrueOrderByCoinsRequiredAsc(ApprovalStatus status);

    // ✅ By coins required (descending)
    List<Reward> findByApprovalStatusAndIsActiveTrueOrderByCoinsRequiredDesc(ApprovalStatus status);


    /* =======================
       🔹 MOST AVAILABLE
       ======================= */
    // ✅ Using JPQL for computed field: (totalQuantity - redeemedQuantity)
    @Query("""
           SELECT r FROM Reward r
           WHERE r.approvalStatus = :status AND r.isActive = true
           ORDER BY (r.totalQuantity - r.redeemedQuantity) DESC
           """)
    List<Reward> findMostAvailableRewards(ApprovalStatus status);

    // ✅ (Optional) Filter by category for most available
    @Query("""
           SELECT r FROM Reward r
           WHERE r.approvalStatus = :status AND r.isActive = true AND r.category = :category
           ORDER BY (r.totalQuantity - r.redeemedQuantity) DESC
           """)
    List<Reward> findMostAvailableRewardsByCategory(ApprovalStatus status, String category);
}