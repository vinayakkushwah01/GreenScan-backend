package com.greenscan.controller;

import com.greenscan.dto.request.LoginRequest;
import com.greenscan.dto.request.RegisterRequest;
import com.greenscan.dto.response.AuthResponse;

import com.greenscan.service.impl.AuthServiceImpl;

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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

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
        
        // Delegate login and token generation to the service layer.
        AuthResponse authResponse = authService.login(request);
        
        // Login is a success action, so we return HTTP 200 OK.
        return ResponseEntity.ok(authResponse);
    }
}
