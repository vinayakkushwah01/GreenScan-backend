package com.greenscan.dto.response;

import com.greenscan.dto.core.UserDTO;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
    private String tokenType = "Bearer";
    private Long expiresIn;
}
