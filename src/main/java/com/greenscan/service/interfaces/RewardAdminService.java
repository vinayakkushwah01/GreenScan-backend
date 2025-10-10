package com.greenscan.service.interfaces;

import com.greenscan.dto.request.RewardAdminRequestDTO;
import com.greenscan.dto.response.RewardAdminResponseDTO;
import java.util.List;

public interface RewardAdminService {

    RewardAdminResponseDTO createReward(RewardAdminRequestDTO requestDTO);

    RewardAdminResponseDTO updateReward(Long rewardId, RewardAdminRequestDTO requestDTO);

    void deleteReward(Long rewardId);

    // Get all rewards (optionally filter by category, availability, or approval)
    List<RewardAdminResponseDTO> getAllRewards(String category, Boolean isActive, String approvalStatus);

    //  Get a single reward details (admin view)
    RewardAdminResponseDTO getRewardById(Long rewardId);

    //Approve or reject a reward
    RewardAdminResponseDTO updateApprovalStatus(Long rewardId, String approvalStatus, String adminNotes);

    // Update reward quantity (inventory)
    RewardAdminResponseDTO updateQuantity(Long rewardId, Integer newQuantity);

    
    // View all redemptions of a specific reward
    List<?> getRewardRedemptions(Long rewardId);
}
