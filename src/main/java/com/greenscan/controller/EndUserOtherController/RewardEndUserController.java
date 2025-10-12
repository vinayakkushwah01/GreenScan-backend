package com.greenscan.controller.EndUserOtherController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenscan.dto.response.RewardDetailDTO;
import com.greenscan.dto.response.RewardSummaryDTO;
import com.greenscan.service.impl.RewardUserServiceImpl;

@RestController
@RequestMapping("/end_users/rewards")
public class RewardEndUserController {
    @Autowired
    private RewardUserServiceImpl rewardService;

    //  Get all available rewards, optionally filtered by category.
    @GetMapping("/available")
    public ResponseEntity<List<RewardSummaryDTO>> getAvailableRewards(
            @RequestParam(required = false) String category) {
                if(category == null || category.isEmpty()) {
                    List<RewardSummaryDTO> rewards = rewardService.getAvailableRewards();
                    return ResponseEntity.ok(rewards);
                }

        List<RewardSummaryDTO> rewards = rewardService.getAvailableRewards(category);
        return ResponseEntity.ok(rewards);
    }

    
    //  Get basic reward summary by ID.
     
    @GetMapping("/{rewardId}")
    public ResponseEntity<RewardSummaryDTO> getReward(@PathVariable Long rewardId) {
        RewardSummaryDTO reward = rewardService.getReward(rewardId);
        if (reward == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reward);
    }

    //  Get detailed reward info by ID.
    @GetMapping("/{rewardId}/details")
    public ResponseEntity<RewardDetailDTO> getRewardDetails(@PathVariable Long rewardId) {
        RewardDetailDTO details = rewardService.getRewardDetails(rewardId);
        if (details == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(details);
    }

    //  Check if a reward is available.
    @GetMapping("/{rewardId}/available")
    public ResponseEntity<Boolean> isRewardAvailable(@PathVariable Long rewardId) {
        boolean available = rewardService.isRewardAvailable(rewardId);
        return ResponseEntity.ok(available);
    }

    //  Get rewards sorted by monetary value.
     @GetMapping("/sort/monetary")
    public ResponseEntity<List<RewardSummaryDTO>> getRewardsByMonetaryValue(
            @RequestParam(defaultValue = "true") boolean ascending) {

        List<RewardSummaryDTO> rewards = rewardService.getRewardsByMonetaryValue(ascending);
        return ResponseEntity.ok(rewards);
    }

    //get rewards sorted by points required
    @GetMapping("/sort/coins")
    public ResponseEntity<List<RewardSummaryDTO>> getRewardsByCoinsRequired(
            @RequestParam(defaultValue = "true") boolean ascending) {

        List<RewardSummaryDTO> rewards = rewardService.getRewardsByCoinsRequired(ascending);
        return ResponseEntity.ok(rewards);
    }

    //getBy most Available rewards
     @GetMapping("/most-available")
    public ResponseEntity<List<RewardSummaryDTO>> getMostAvailableRewards(
            @RequestParam(required = false) String category) {

        List<RewardSummaryDTO> rewards = rewardService.getMostAvailableRewards(category);
        return ResponseEntity.ok(rewards);
    }

    //Get most Available rewards Category wise
    @GetMapping("/most-available/category/{category}")
    public ResponseEntity<List<RewardSummaryDTO>> getMostAvailableRewardsByCategory(
            @PathVariable String category) {

        List<RewardSummaryDTO> rewards = rewardService.getMostAvailableRewardsByCategory(category);
        return ResponseEntity.ok(rewards);
    }
}
