package com.greenscan.service.interfaces;

import com.greenscan.dto.request.CompleteProfileRequest;
import com.greenscan.dto.request.LoginRequest;
import com.greenscan.dto.request.RegisterRequest;
import com.greenscan.dto.response.AuthResponse;
import com.greenscan.dto.response.UserResponse;

public interface AuthService {
     AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    UserResponse completeProfile(Long userId, CompleteProfileRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(Long userId);
    void changePassword(Long userId, String oldPassword, String newPassword);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}