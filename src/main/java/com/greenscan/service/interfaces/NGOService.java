package com.greenscan.service.interfaces;

import java.util.List;

import com.greenscan.dto.request.NGOProfileRequest;
import com.greenscan.dto.response.NGOProfileResponse;
import com.greenscan.dto.response.NGOProfileResponseAdminView;
import com.greenscan.entity.MainUser;

public interface NGOService {
    NGOProfileResponse createNgo( NGOProfileRequest ngoProfileRequest);
    // this will be part of admin service
    //NGOProfileResponseAdminView getNgoProfileById(Long ngoId);
    List<NGOProfileResponse> getAllNgos();
    List<NGOProfileResponse> getAllNgoForNgo();
    //NGOProfileResponse addDonation();
} 