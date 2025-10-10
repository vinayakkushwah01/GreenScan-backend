package com.greenscan.service.interfaces;

import com.greenscan.dto.response.RewardSummaryDTO;
import com.greenscan.dto.response.RewardDetailDTO;
import com.greenscan.entity.RewardRedemption;
import java.util.List;

public interface RewardUserService {

    // Get summarized rewards (for card/list view)
    List<RewardSummaryDTO> getAvailableRewards(String category);

    // Get detailed info of a specific reward
    RewardDetailDTO getRewardDetails(Long rewardId);

    // Redeem a reward
    RewardRedemption redeemReward(Long userId, Long rewardId, String userNotes);

    //  Get user’s redemption history
    List<RewardRedemption> getUserRewardHistory(Long userId);

    //  Get current redemption status for a specific reward
    RewardRedemption getRedemptionStatus(Long userId, Long rewardId);

    // Cancel a pending redemption (if allowed)
    boolean cancelRedemption(Long userId, Long redemptionId);

    //  Mark a redemption as used (user confirms usage or admin validates)
    boolean markRewardAsUsed(Long redemptionId);

    // ⏳Check for expired redemptions and update statuses
    void checkAndUpdateExpiredRedemptions();

    //  Revalidate redemption availability (for front-end before redeem)
    boolean isRewardAvailable(Long rewardId);
}

