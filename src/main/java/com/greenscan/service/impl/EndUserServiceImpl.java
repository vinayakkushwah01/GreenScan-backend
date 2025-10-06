package com.greenscan.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.greenscan.dto.request.EndUserProfileRequest;
import com.greenscan.dto.response.EndUserProfileResponse;
import com.greenscan.entity.EndUserProfile;
import com.greenscan.entity.MainUser;
import com.greenscan.exception.custom.ResourceNotFoundException;
import com.greenscan.repository.EndUserProfileRepository;
import com.greenscan.repository.MainUserRepository;
import com.greenscan.service.interfaces.EndUserService;
import java.io.File;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EndUserServiceImpl implements EndUserService {


    private final EndUserProfileRepository endUserProfileRepository;
    private final MainUserRepository mainUserRepository;
    @Override
    @Transactional(readOnly = true)
    public EndUserProfileResponse getEndUserProfile(Long userId) {
        log.info("Fetching end user profile for userId: {}", userId);
        
        MainUser mainUser = mainUserRepository.findByIdAndIsActiveTrue(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
       
        return getEndUserProfile(mainUser);
    }

    @Override
    @Transactional(readOnly = true)
    public EndUserProfileResponse getEndUserProfile(MainUser mainUser) {
        log.info("Fetching end user profile for user: {}", mainUser.getEmail());
        
        EndUserProfile profile = endUserProfileRepository.findByUserIdAndIsActiveTrue(mainUser.getId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "EndUserProfile", "userId", mainUser.getId()
            ));
        
        return mapToResponse(profile);
    }
    @Override
     @Transactional
    public EndUserProfileResponse createEndUserProfile(MainUser mainUser) {
        log.info("Creating end user profile for user: {}", mainUser.getEmail());
        
        // Check if profile already exists
        if (endUserProfileRepository.findByUserIdAndIsActiveTrue(mainUser.getId()).isPresent()) {
            throw new IllegalStateException("EndUserProfile already exists for user: " + mainUser.getId());
        }
        
        EndUserProfile profile = new EndUserProfile();
        profile.setUser(mainUser);
        profile.setGreenCoinsBalance(BigDecimal.ZERO);
        profile.setTotalCoinsEarned(BigDecimal.ZERO);
        profile.setTotalCoinsDonated(BigDecimal.ZERO);
        profile.setTotalCoinsRedeemed(BigDecimal.ZERO);
        profile.setTotalWasteRecycledKg(BigDecimal.ZERO);
        profile.setTotalCartsCompleted(0);
        profile.setEcoScore(0);
       // profile.setReferralCode(generateReferralCode(mainUser.getId()));
        profile.setReferralBonusEarned(BigDecimal.ZERO);
        
        EndUserProfile savedProfile = endUserProfileRepository.save(profile);
        log.info("Successfully created end user profile for user: {}", mainUser.getEmail());
        
        return mapToResponse(savedProfile);
    }

     @Override
    @Transactional
    public EndUserProfileResponse updateEndUserProfile(Long userId, EndUserProfileRequest profileData) {
        log.info("Updating end user profile for userId: {}", userId);
        // re implement this method  


        // EndUserProfile profile = endUserProfileRepository.findByUserIdAndIsActiveTrue(userId)
        //     .orElseThrow(() -> new ResourceNotFoundException("EndUserProfile", "userId", userId));
        
        // // Update only non-null fields from profileData
        // if (profileData.getPreferredPickupTime() != null) {
        //     profile.setPreferredPickupTime(profileData.getPreferredPickupTime());
        // }
        
        // EndUserProfile updatedProfile = endUserProfileRepository.save(profile);
        // log.info("Successfully updated end user profile for userId: {}", userId);
        
        // return mapToResponse(updatedProfile);
        return null;

    }
    

    public String generateReferralCode(Long userId) {
        String baseCode = "GS" + userId + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        
        // Ensure uniqueness
        while (endUserProfileRepository.findByReferralCodeAndIsActiveTrue(baseCode).isPresent()) {
            baseCode = "GS" + userId + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        }
        
        return baseCode;
    }
    
    /**
     * Map EndUserProfile entity to EndUserProfileResponse DTO
     */
    private EndUserProfileResponse mapToResponse(EndUserProfile profile) {
        EndUserProfileResponse response = new EndUserProfileResponse();
        response.setGreenCoinsBalance(profile.getGreenCoinsBalance());
        response.setTotalCoinsEarned(profile.getTotalCoinsEarned());
        response.setTotalCoinsDonated(profile.getTotalCoinsDonated());
        response.setTotalCoinsRedeemed(profile.getTotalCoinsRedeemed());
        response.setTotalWasteRecycledKg(profile.getTotalWasteRecycledKg());
        response.setTotalCartsCompleted(profile.getTotalCartsCompleted());
        response.setEcoScore(profile.getEcoScore());
       // response.setReferralCode(profile.getReferralCode());
        return response;
    }

    @Override
    public String uploadProfileImg(Long id, File file) {
        //here you have to use supabase storage service to upload the file and store the url in the database
        // the file name must be the userID+UserName_profileImg.extension
        throw new UnsupportedOperationException("Unimplemented method 'uploadProfileImg'");
    }

    @Override
    public String updateProfileImg(Long id, File file) {
        // first remove the old image from the storage
         //here you have to use supabase storage service to upload the file and store the url in the database
        // the file name must be the userID+UserName_profileImg.extension
        throw new UnsupportedOperationException("Unimplemented method 'updateProfileImg'");
    }
    
}
