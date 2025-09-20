package com.greenscan.dto.core;

import com.greenscan.enums.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO {
    private String email;
    private String name;
    private String mobile;
    private UserRole role;
    private String profileImageUrl;
    private Boolean isEmailVerified;
    private Boolean isMobileVerified;
    private LocalDateTime lastLogin;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private Double latitude;
    private Double longitude;
    private Boolean locationVerified;
    private LocalDateTime locationUpdatedAt;
    private Boolean emailNotificationsEnabled;
    private Boolean pushNotificationsEnabled;
    private Boolean smsNotificationsEnabled;
}