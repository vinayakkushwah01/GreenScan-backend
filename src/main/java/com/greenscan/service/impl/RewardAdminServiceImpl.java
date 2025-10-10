package com.greenscan.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.greenscan.dto.request.RewardAdminRequestDTO;
import com.greenscan.dto.response.RewardAdminResponseDTO;
import com.greenscan.entity.Reward;
import com.greenscan.enums.ApprovalStatus;
import com.greenscan.exception.custom.FileUploadException;
import com.greenscan.exception.custom.ResourceNotFoundException;
import com.greenscan.repository.RewardRepository;
import com.greenscan.service.interfaces.RewardAdminService;

public class RewardAdminServiceImpl implements RewardAdminService {
    @Autowired
    private SupabaseStorageService supabaseStorageService;

    @Autowired
    private RewardRepository rewardRepository;


    private Reward mapDtoToEntity(RewardAdminRequestDTO dto) {
        Reward reward = new Reward();
        // ===== Basic Info =====
        reward.setTitle(dto.getTitle());
        reward.setDescription(dto.getDescription());
        reward.setCategory(dto.getCategory());
        reward.setCoinsRequired(dto.getCoinsRequired());
        reward.setMonetaryValue(dto.getMonetaryValue());
            // ===== Additional Info =====
        reward.setTermsConditions(dto.getTermsConditions());
        reward.setRedemptionInstructions(dto.getRedemptionInstructions());
            // ===== Admin & Approval =====
        reward.setApprovalStatus(dto.getApprovalStatus() != null ? dto.getApprovalStatus() : ApprovalStatus.PENDING);
        reward.setCreatedByAdminId(dto.getCreatedByAdminId());

        // ===== Availability =====
        reward.setAvailableFrom(dto.getAvailableFrom());
        reward.setAvailableUntil(dto.getAvailableUntil());
        reward.setTotalQuantity(dto.getTotalQuantity());
        reward.setMaxPerUser(dto.getMaxPerUser());

        // ===== Partner Info =====
        reward.setPartnerName(dto.getPartnerName());
        reward.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
    
        return reward;
    }
   
   
    @Override
    public RewardAdminResponseDTO createReward(RewardAdminRequestDTO requestDTO) {
        Reward reward = mapDtoToEntity(requestDTO);
        String rewardImgUrl =null; String partnerLogoUrl = null;
        try{
             rewardImgUrl= supabaseStorageService.uploadFile(requestDTO.getRewardImageFile(), requestDTO.getTitle());
             partnerLogoUrl = supabaseStorageService.uploadFile(requestDTO.getPartnerLogoFile(), requestDTO.getTitle());
        } catch (Exception e) {
            throw new FileUploadException("Cannot upload file. Please try later.");
        }
        reward.setImageUrl(rewardImgUrl);
        reward.setPartnerLogoUrl(partnerLogoUrl);
        // Save reward to DB (repository code not shown here)
        return new RewardAdminResponseDTO(rewardRepository.save(reward));

    }
   
    @Override
    public void deleteReward(Long rewardId) {
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new ResourceNotFoundException("Reward", "id", rewardId));
        reward.setIsActive(false);
        rewardRepository.save(reward);
    }

    @Override
    public List<RewardAdminResponseDTO> getAllRewards(String category, Boolean isActive, String approvalStatus) {
        List<Reward> rewards;

        if (category != null && isActive != null && approvalStatus != null) {
            rewards = rewardRepository.findByCategoryAndIsActiveTrue(category)
                    .stream()
                    .filter(r -> r.getApprovalStatus().name().equalsIgnoreCase(approvalStatus))
                    .toList();
        } else if (category != null && isActive != null) {
            rewards = rewardRepository.findByCategoryAndIsActiveTrue(category);
        } else if (isActive != null) {
            rewards = rewardRepository.findByIsActiveTrue();
        } else if (approvalStatus != null) {
            rewards = rewardRepository.findByApprovalStatus(ApprovalStatus.valueOf(approvalStatus));
        } else {
            rewards = rewardRepository.findAll();
        }

        return rewards.stream()
                .map(RewardAdminResponseDTO::new)
                .toList();
    }

    @Override
    public RewardAdminResponseDTO getRewardById(Long rewardId) {
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new ResourceNotFoundException("Reward", "id", rewardId));
        return new RewardAdminResponseDTO(reward);
    }

    @Override
    public RewardAdminResponseDTO updateApprovalStatus(Long rewardId, String approvalStatus, String adminNotes) {
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new ResourceNotFoundException("Reward", "id", rewardId));

        reward.setApprovalStatus(ApprovalStatus.valueOf(approvalStatus));
        rewardRepository.save(reward);
        return new RewardAdminResponseDTO(reward);
    }

    @Override
    public RewardAdminResponseDTO updateQuantity(Long rewardId, Integer newQuantity) {
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new ResourceNotFoundException("Reward", "id", rewardId));

        reward.setTotalQuantity(newQuantity);
        return new RewardAdminResponseDTO(rewardRepository.save(reward));
    }


    @Override
    public RewardAdminResponseDTO updateReward(Long rewardId, RewardAdminRequestDTO requestDTO) {
        // 1️⃣ Fetch existing reward
        Reward existingReward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new ResourceNotFoundException("Reward", "id", rewardId));

        // 2️⃣ Preserve existing image URLs
        String rewardImgUrl = existingReward.getImageUrl();
        String partnerLogoUrl = existingReward.getPartnerLogoUrl();

        // 3️⃣ Upload new files if present
        try {
            if (requestDTO.getRewardImageFile() != null && requestDTO.getRewardImageFile().exists()) {
                rewardImgUrl = supabaseStorageService.uploadFile(requestDTO.getRewardImageFile(), requestDTO.getTitle());
            }

            if (requestDTO.getPartnerLogoFile() != null && requestDTO.getPartnerLogoFile().exists()) {
                partnerLogoUrl = supabaseStorageService.uploadFile(requestDTO.getPartnerLogoFile(), requestDTO.getTitle());
            }
        } catch (Exception e) {
            throw new FileUploadException("Cannot upload file. Please try later.");
        }

        // 4️⃣ Map DTO fields to existing entity
        // Assuming you have a helper that maps all fields including image URLs
        Reward updatedReward = mapDtoToEntity(requestDTO);
        updatedReward.setId(existingReward.getId()); // Preserve ID
        updatedReward.setImageUrl(rewardImgUrl);
        updatedReward.setPartnerLogoUrl(partnerLogoUrl);

        // 5️⃣ Save and return
        return new RewardAdminResponseDTO(rewardRepository.save(updatedReward));
    }


    @Override
    public List<?> getRewardRedemptions(Long rewardId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRewardRedemptions'");
    }

   
    
}
