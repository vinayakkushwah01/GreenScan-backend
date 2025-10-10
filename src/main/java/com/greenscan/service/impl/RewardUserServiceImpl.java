package com.greenscan.service.impl;

import java.util.List;

import com.greenscan.dto.response.RewardDetailDTO;
import com.greenscan.dto.response.RewardSummaryDTO;
import com.greenscan.entity.RewardRedemption;
import com.greenscan.service.interfaces.RewardUserService;

public class RewardUserServiceImpl implements RewardUserService {

    @Override
    public List<RewardSummaryDTO> getAvailableRewards(String category) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAvailableRewards'");
    }

    @Override
    public RewardDetailDTO getRewardDetails(Long rewardId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRewardDetails'");
    }

    @Override
    public RewardRedemption redeemReward(Long userId, Long rewardId, String userNotes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'redeemReward'");
    }

    @Override
    public List<RewardRedemption> getUserRewardHistory(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserRewardHistory'");
    }

    @Override
    public RewardRedemption getRedemptionStatus(Long userId, Long rewardId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRedemptionStatus'");
    }

    @Override
    public boolean cancelRedemption(Long userId, Long redemptionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelRedemption'");
    }

    @Override
    public boolean markRewardAsUsed(Long redemptionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markRewardAsUsed'");
    }

    @Override
    public void checkAndUpdateExpiredRedemptions() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkAndUpdateExpiredRedemptions'");
    }

    @Override
    public boolean isRewardAvailable(Long rewardId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isRewardAvailable'");
    }
    
}
