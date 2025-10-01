package com.greenscan.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
     private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn; // in seconds
    private UserResponse user;

}
