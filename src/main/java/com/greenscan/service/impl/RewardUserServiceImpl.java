package com.greenscan.service.impl;

//import static org.junit.jupiter.api.DynamicTest.stream;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenscan.dto.response.RewardDetailDTO;
import com.greenscan.dto.response.RewardSummaryDTO;
import com.greenscan.dto.response.VendorProfileAdminViewResponse;
import com.greenscan.entity.Reward;
import com.greenscan.entity.RewardRedemption;
import com.greenscan.enums.ApprovalStatus;
import com.greenscan.repository.RewardRepository;
import com.greenscan.service.interfaces.RewardUserService;

@Service
public class RewardUserServiceImpl implements RewardUserService {

    @Autowired
    private RewardRepository rewardRepository;

        @Override
        public List<RewardSummaryDTO> getAvailableRewards(String category) {
        return  rewardRepository.findByApprovalStatusAndCategoryAndIsActiveTrue(ApprovalStatus.APPROVED,category)
                    .stream()
                    .map(RewardSummaryDTO::new)
                    .collect(Collectors.toList());
        }

        public List<RewardSummaryDTO> getAvailableRewards() {
        return  rewardRepository.findByApprovalStatusAndIsActiveTrue(ApprovalStatus.APPROVED)
                    .stream()
                    .map(RewardSummaryDTO::new)
                    .collect(Collectors.toList());
        }
        
        @Override
        public RewardSummaryDTO getReward(Long rewardId){
            return rewardRepository.findById(rewardId)
                    .map(RewardSummaryDTO::new)
                    .orElse(null);
            
        }

        @Override
        public RewardDetailDTO getRewardDetails(Long rewardId) {
            return rewardRepository.findById(rewardId)
                    .map(RewardDetailDTO::new)
                    .orElse(null);
        }

        @Override
        public boolean isRewardAvailable(Long rewardId) {
            return rewardRepository.findById(rewardId)
                    .map(reward -> reward.getApprovalStatus() == ApprovalStatus.APPROVED && reward.isAvailable())
                    .orElse(false);
        }

        public List<RewardSummaryDTO> getRewardsByMonetaryValue(boolean ascending) {
            List<Reward> rewards;
            if (ascending) {
                rewards = rewardRepository.findByApprovalStatusAndIsActiveTrueOrderByMonetaryValueAsc(ApprovalStatus.APPROVED);
            } else {
                rewards = rewardRepository.findByApprovalStatusAndIsActiveTrueOrderByMonetaryValueDesc(ApprovalStatus.APPROVED);
            }
            return rewards.stream()
                    .map(RewardSummaryDTO::new)
                    .collect(Collectors.toList());
        }

        public List<RewardSummaryDTO> getRewardsByCoinsRequired(boolean ascending) {
            List<Reward> rewards;
            if (ascending) {
                rewards = rewardRepository.findByApprovalStatusAndIsActiveTrueOrderByCoinsRequiredAsc(ApprovalStatus.APPROVED);
            } else {
                rewards = rewardRepository.findByApprovalStatusAndIsActiveTrueOrderByCoinsRequiredDesc(ApprovalStatus.APPROVED);
            }
            return rewards.stream()
                    .map(RewardSummaryDTO::new)
                    .collect(Collectors.toList());
        }
        public List<RewardSummaryDTO> getMostAvailableRewards(String category) {
            List<Reward> rewards;
            if (category != null && !category.isEmpty()) {
                rewards = rewardRepository.findMostAvailableRewardsByCategory(ApprovalStatus.APPROVED, category);
            } else {
                rewards = rewardRepository.findMostAvailableRewards(ApprovalStatus.APPROVED);
            }
            return rewards.stream()
                    .map(RewardSummaryDTO::new)
                    .collect(Collectors.toList());
        }

        public List<RewardSummaryDTO> getMostAvailableRewardsByCategory(String category) {
            List<Reward> rewards = rewardRepository.findMostAvailableRewardsByCategory(ApprovalStatus.APPROVED, category);
            return rewards.stream()
                    .map(RewardSummaryDTO::new)
                    .collect(Collectors.toList());
        }


    // reward redemption and history methods to be implemented

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

    
}
