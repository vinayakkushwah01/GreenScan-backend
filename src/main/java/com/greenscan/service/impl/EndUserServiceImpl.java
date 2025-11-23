package com.greenscan.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenscan.dto.request.EndUserProfileRequest;
import com.greenscan.dto.response.EndUserProfileResponse;
import com.greenscan.dto.response.UserResponse;
import com.greenscan.entity.EndUserProfile;
import com.greenscan.entity.MainUser;
import com.greenscan.exception.custom.FileUploadException;
import com.greenscan.exception.custom.ResourceNotFoundException;
import com.greenscan.repository.EndUserProfileRepository;
import com.greenscan.repository.MainUserRepository;
import com.greenscan.service.interfaces.EndUserService;
import java.io.File;
import java.io.IOException;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EndUserServiceImpl implements EndUserService {


    private final EndUserProfileRepository endUserProfileRepository;
    private final MainUserRepository mainUserRepository;
        @Autowired
    private SupabaseStorageService supabaseStorageService;

    
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
    @Transactional(readOnly = true)
    public EndUserProfileResponse getEndUserProfile(Long userId) {
            log.info("Fetching Main  user profile for userId: {}", userId);
            
            MainUser mainUser = mainUserRepository.findByIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
           
            return getEndUserProfile(mainUser);
        }
        @Override
        @Transactional
        public EndUserProfileResponse createEndUserProfile( EndUserProfileRequest request) {
        // Check if profile already exist
       MainUser mainUser = mainUserRepository.findByIdAndIsActiveTrue(request.getMainUserId())
                    .orElseThrow(()-> new ResourceNotFoundException("User", "id", request.getMainUserId()));
        if (endUserProfileRepository.findByUserIdAndIsActiveTrue(mainUser.getId()).isPresent()) {
            throw new IllegalStateException("End user profile already exists for userId: " + mainUser.getId());
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
        profile.setPreferredPickupTime(request.getPreferedPickupTime() != null ? request.getPreferedPickupTime() : "");
        profile.setPreferredVendorId(request.getPreferdVendorId() != null ? request.getPreferdVendorId() : null);
       // profile.setReferralCode(generateReferralCode(mainUser.getId()));
        profile.setReferralBonusEarned(BigDecimal.ZERO);
        
        EndUserProfile savedProfile = endUserProfileRepository.save(profile);
        log.info("Successfully created end user profile for user: {}", mainUser.getEmail());
        
        return mapToResponse(savedProfile);
    }

     @Override
    @Transactional
    public EndUserProfileResponse updateEndUserProfile( EndUserProfileRequest profileData) {
        log.info("Updating end user profile for userId: {}", profileData.getMainUserId());
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
    public UserResponse getEndUserFullProfile(Long id){
    MainUser mainUser = mainUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EndUserProfile", "id", id));
        return UserResponse.fromMainUser(mainUser);
        
    }


    @Override
    public String uploadProfileImg(Long id, File file) {
        MainUser mainUser = mainUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EndUserProfile", "id", id));

        String fileName = id + mainUser.getId() + System.currentTimeMillis() + "_profileImg";
        try {
           String url = supabaseStorageService.uploadFile(file, fileName);
        
         if(mainUser.getProfileImageUrl() != null) {
            supabaseStorageService.deleteFile(mainUser.getProfileImageUrl());
            }
         mainUser.setProfileImageUrl(url);
         mainUserRepository.save(mainUser);
         return "User profile image uploaded successfully for user "+mainUser.getId();


        } catch (IOException e) {
            System.err.println("At Service layer "+e.getLocalizedMessage());
            throw new FileUploadException("Failed to upload profile image for user id: " +mainUser.getId());

        }       
    }

    
    
}
