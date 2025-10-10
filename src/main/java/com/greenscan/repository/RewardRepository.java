package com.greenscan.repository;
import com.greenscan.entity.Reward;
import com.greenscan.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
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

    // ✅ Custom: find rewards available for redemption
    default List<Reward> findAvailableRewards(List<Reward> rewards) {
        return rewards.stream()
                .filter(Reward::isAvailable)
                .toList();
    }
}