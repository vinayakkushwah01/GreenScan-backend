package com.greenscan.dto.response;

import java.time.LocalDateTime;

import com.greenscan.entity.MainUser;
import com.greenscan.enums.UserRole;

import lombok.Data;
@Data
public class UserResponse {
 
    private Long id;
    private String email;
    private String name;
    private String mobile;
    private String role;
    private String profileImageUrl;
    private String city;
    private String state;
    private Boolean isEmailVerified;
    private Boolean isMobileVerified;
    private LocalDateTime lastLogin;
    
    // Role-specific data
    private EndUserProfileResponse endUserProfile;
    private VendorProfileResponse vendorProfile;
    private NGOProfileResponse ngoProfile;  
    private AdsCompanyProfileResponse adsCompanyProfile;



    public static UserResponse fromMainUser(MainUser user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();

        // 1. Map common fields
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setMobile(user.getMobile());
        response.setRole(user.getRole() != null ? user.getRole().name() : null);
        response.setProfileImageUrl(user.getProfileImageUrl());
        response.setCity(user.getCity());
        response.setState(user.getState());
        response.setIsEmailVerified(user.getIsEmailVerified());
        response.setIsMobileVerified(user.getIsMobileVerified());
        response.setLastLogin(user.getLastLogin());

        // 2. Map role-specific profile data conditionally
        UserRole userRole = user.getRole();
        return response;
    }
}
