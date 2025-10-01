package com.greenscan.dto.response;

import java.time.LocalDateTime;

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
}
