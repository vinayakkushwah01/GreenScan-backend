package com.greenscan.service.interfaces;

import java.io.File;

import com.greenscan.dto.request.EndUserProfileRequest;
import com.greenscan.dto.response.EndUserProfileResponse;
import com.greenscan.entity.MainUser;

public interface EndUserService {
    public EndUserProfileResponse getEndUserProfile(Long userId);
    public EndUserProfileResponse getEndUserProfile(MainUser mainUser);
    public EndUserProfileResponse createEndUserProfile(MainUser mainUser);
    public EndUserProfileResponse updateEndUserProfile(Long userId, EndUserProfileRequest profile);
    public String uploadProfileImg(Long  id, File file);
    public String updateProfileImg(Long id , File file);
    
}
