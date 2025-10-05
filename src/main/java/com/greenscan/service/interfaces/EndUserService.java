package com.greenscan.service.interfaces;

import com.greenscan.dto.response.EndUserProfileResponse;
import com.greenscan.entity.MainUser;

public interface EndUserService {
    public EndUserProfileResponse getEndUserProfile(String userId);
    public EndUserProfileResponse getEndUserProfile(MainUser mainUser);
    public EndUserProfileResponse createEndUserProfile(MainUser mainUser);
    //public EndUserProfileResponse updateEndUserProfile(String userId, EndUserProfileRequest profile);

}
