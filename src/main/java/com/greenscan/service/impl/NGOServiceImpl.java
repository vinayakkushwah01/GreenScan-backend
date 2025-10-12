package com.greenscan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenscan.dto.request.NGOProfileRequest;
import com.greenscan.dto.response.NGOProfileResponse;
import com.greenscan.entity.MainUser;
import com.greenscan.entity.NGOProfile;
import com.greenscan.enums.ApprovalStatus;
import com.greenscan.enums.UserRole;
import com.greenscan.exception.custom.ResourceNotFoundException;
import com.greenscan.repository.MainUserRepository;
import com.greenscan.repository.NGOProfileRepository;
import com.greenscan.service.interfaces.NGOService;


@Service
public class NGOServiceImpl implements NGOService {
   @Autowired
    private  MainUserRepository mainUserRepository;
   @Autowired
   private  NGOProfileRepository ngoProfileRepository;

    private NGOProfile mapNgoFromRequest(NGOProfileRequest ngoProfileRequest) {
          NGOProfile ngo = new NGOProfile();

        // Set basic details
        ngo.setOrganizationName(ngoProfileRequest.getOrganizationName());
        ngo.setRegistrationNumber(ngoProfileRequest.getRegistrationNumber());
        ngo.setCauseDescription(ngoProfileRequest.getCauseDescription());
        ngo.setWebsiteUrl(ngoProfileRequest.getWebsiteUrl());

        // Bank details
        ngo.setBankAccountNumber(ngoProfileRequest.getBankAccountNumber());
        ngo.setBankIfscCode(ngoProfileRequest.getBankIfscCode());
        ngo.setBankAccountHolderName(ngoProfileRequest.getBankAccountHolderName());
        ngo.setBankName(ngoProfileRequest.getBankName());

        // Impact details
        ngo.setImpactBeneficiaries(ngoProfileRequest.getImpactBeneficiaries());
        ngo.setImpactDescription(ngoProfileRequest.getImpactDescription());

        // Default values (optional, but good practice)
        ngo.setApprovalStatus(com.greenscan.enums.ApprovalStatus.PENDING);
        ngo.setTotalCoinsReceived(java.math.BigDecimal.ZERO);
        ngo.setTotalMoneyConverted(java.math.BigDecimal.ZERO);
        ngo.setPendingConversionCoins(java.math.BigDecimal.ZERO);
        ngo.setTotalDonors(0);
        ngo.setTotalDonations(0);
        return ngo;
    }
    @Override
    public NGOProfileResponse createNgo( NGOProfileRequest ngoProfileRequest) {
        MainUser mainUser = mainUserRepository.findById(ngoProfileRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        NGOProfile ngoProfile = mapNgoFromRequest(ngoProfileRequest);
        ngoProfile.setUser(mainUser);
        ngoProfile = ngoProfileRepository.save(ngoProfile);
 
        return new NGOProfileResponse(ngoProfile ,"NGO");
    }


    @Override
    public List<NGOProfileResponse> getAllNgos() {
        List<NGOProfile> ngos = ngoProfileRepository.findByApprovalStatusAndIsActiveTrue(ApprovalStatus.APPROVED);
        return ngos.stream()
                .map(ngo -> new NGOProfileResponse(ngo, "ENDUSER"))
                .toList();
    }
    @Override
    public List<NGOProfileResponse> getAllNgoForNgo() {
         List<NGOProfile> ngos = ngoProfileRepository.findByApprovalStatusAndIsActiveTrue(ApprovalStatus.APPROVED);
        return ngos.stream()
                .map(ngo -> new NGOProfileResponse(ngo, "NGO"))
                .toList();
    }


    
}
