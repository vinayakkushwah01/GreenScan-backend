package com.greenscan.controller;

import com.greenscan.dto.request.CompleteProfileRequest;
import com.greenscan.dto.request.EndUserProfileRequest;
import com.greenscan.dto.request.LoginRequest;
import com.greenscan.dto.request.RegisterRequest;
import com.greenscan.dto.response.AuthResponse;
import com.greenscan.dto.response.EndUserProfileResponse;
import com.greenscan.dto.response.StringResponse;
import com.greenscan.dto.response.UserResponse;
import com.greenscan.service.impl.AuthServiceImpl;
import com.greenscan.service.impl.EndUserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller handling user authentication (login and registration).
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final  EndUserServiceImpl endUserService;


    /**
     * Endpoint for user registration.
     * Maps to POST /api/auth/register
     * * @param request The registration details (email, password, name, role).
     * @return ResponseEntity containing the AuthResponse (tokens) and HTTP 201 Created status.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        log.info("Received registration request for email: {}", request.getEmail());
        
        // Delegate registration logic to the service layer.
        // The service handles password encryption, saving the user, and auto-logging in.
        AuthResponse authResponse = authService.register(request);

           EndUserProfileResponse profile = endUserService.createEndUserProfile(new EndUserProfileRequest(authResponse.getUser().getId()));
          
        // Registration is a creation action, so we return HTTP 201 Created.
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint for user login.
     * Maps to POST /api/auth/login
     * * @param request The login credentials (email and password).
     * @return ResponseEntity containing the AuthResponse (tokens) and HTTP 200 OK status.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        log.info("Received login request for email: {}", request.getEmail());
        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.ok(authResponse);
    }
      
    
    @PostMapping("/forgot-password")
    public ResponseEntity<StringResponse> forgotPassword(@RequestParam String email) {
        log.info("Received forgot password request for email: {}", email);
        String message = authService.forgotPassword(email);
        return ResponseEntity.ok(new StringResponse(message));
    }

    // 2️⃣ Reset Password using OTP
    @PostMapping("/reset-password")
    public ResponseEntity<StringResponse> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {

        log.info("Received reset password request for email: {}", email);
        String message = authService.resetPassword(email, otp, newPassword);
        return ResponseEntity.ok(new StringResponse(message));
    }
}
