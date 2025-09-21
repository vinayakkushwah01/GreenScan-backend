package com.greenscan.dto.request;

import com.greenscan.enums.UserRole;
import lombok.Data;

@Data
public class RegisterUserDTO {
    private String email;
    private String password;
    private String name;
    private String mobile;
    private UserRole role;
    private String referralCode;
}
