package com.greenscan.service.interfaces;

import java.io.File;

import com.greenscan.dto.request.EndUserProfileRequest;
import com.greenscan.dto.response.EndUserProfileResponse;
import com.greenscan.entity.MainUser;

public interface EndUserService {
    public EndUserProfileResponse getEndUserProfile(Long userId);
    public EndUserProfileResponse getEndUserProfile(MainUser mainUser);
    public EndUserProfileResponse createEndUserProfile( EndUserProfileRequest request);
    public EndUserProfileResponse updateEndUserProfile(EndUserProfileRequest profile);
    public String uploadProfileImg(Long  id, File file);
   // public String updateProfileImg(Long id , File file); already handel in Upload profile file 
    
}
