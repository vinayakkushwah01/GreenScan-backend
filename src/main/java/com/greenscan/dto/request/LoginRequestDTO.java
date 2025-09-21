package com.greenscan.dto.request;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
    private String deviceId;
    private String deviceType;
    private String fcmToken;
}
