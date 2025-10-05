package com.greenscan.service.impl;

import com.greenscan.dto.response.EndUserProfileResponse;
import com.greenscan.entity.MainUser;
import com.greenscan.service.interfaces.EndUserService;

public class EndUserServiceImpl implements EndUserService {

    @Override
    public EndUserProfileResponse getEndUserProfile(String userId) {
       
        // her we need to fetch MainUser by userId and then fetch EndUserProfile

        throw new UnsupportedOperationException("Unimplemented method 'getEndUserProfile'");
    }

    @Override
    public EndUserProfileResponse getEndUserProfile(MainUser mainUser) {
     

        // here we will fetch EndUserProfile by MainUser
        throw new UnsupportedOperationException("Unimplemented method 'getEndUserProfile'");
    }

    @Override
    public EndUserProfileResponse createEndUserProfile(MainUser mainUser) {
       
        //here we will create EndUserProfile for the given MainUser
        throw new UnsupportedOperationException("Unimplemented method 'createEndUserProfile'");
    }
    
}
