package com.greenscan.dto.request;

import com.greenscan.entity.MainUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompleteProfileRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Mobile is required")
    @Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
    private String mobile;
    
    @NotBlank(message = "Address is required")
    private String addressLine1;
    
    private String addressLine2;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "State is required")
    private String state;
    
    @NotBlank(message = "Pincode is required")
    @Size(min = 6, max = 6, message = "Pincode must be 6 digits")
    private String pincode;
    
    private Double latitude;
    private Double longitude;

    public static MainUser updateUserFromRequest(MainUser user, CompleteProfileRequest request) {
    user.setName(request.getName());
    user.setMobile(request.getMobile());
    user.setAddressLine1(request.getAddressLine1());
    user.setAddressLine2(request.getAddressLine2());
    user.setCity(request.getCity());
    user.setState(request.getState());
    user.setPincode(request.getPincode());
    user.setLatitude(request.getLatitude());
    user.setLongitude(request.getLongitude());
    user.setLocationUpdatedAt(java.time.LocalDateTime.now());
    return user;
}
}
