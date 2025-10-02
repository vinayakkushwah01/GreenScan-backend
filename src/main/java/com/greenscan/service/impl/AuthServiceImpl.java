package com.greenscan.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.greenscan.dto.request.CompleteProfileRequest;
import com.greenscan.dto.request.LoginRequest;
import com.greenscan.dto.request.RegisterRequest;
import com.greenscan.dto.response.AuthResponse;
import com.greenscan.dto.response.UserResponse;
import com.greenscan.entity.MainUser;
import com.greenscan.enums.UserRole;
import com.greenscan.exception.custom.EmailAlreadyExistsException;
import com.greenscan.exception.custom.MobileAlreadyExistsException;
import com.greenscan.repository.MainUserRepository;
import com.greenscan.security.JwtTokenProvider;
import com.greenscan.security.UserPrincipal;
import com.greenscan.service.interfaces.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MainUserRepository mainUserRepository;
     private final AuthenticationManager authenticationManager;
     private final JwtTokenProvider tokenProvider;
      private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 3. Generate the JWT tokens
            String accessToken = tokenProvider.generateToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(authentication);
            
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Long accessTokenExpirationInMs = tokenProvider.getExpirationDateFromJWT(accessToken).getTime() - System.currentTimeMillis();

        UserResponse u = UserResponse.fromMainUser(mainUserRepository.findByEmailAndIsActiveTrue(userPrincipal.getEmail()).get());
            return new AuthResponse(
                    accessToken,
                    refreshToken,
                    "Bearer",
                    accessTokenExpirationInMs,
                    u  
            );
        } catch (BadCredentialsException ex) {
                
                throw new BadCredentialsException("Invalid email or password");
            }
    
        // return null;

    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
       // 1. Check if email already exists
        if (mainUserRepository.findByEmailAndIsActiveTrue(request.getEmail()).isPresent()) {
            log.warn("Registration failed: Email already exists - {}", request.getEmail());
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        //check if mobile alredy exuist 
         if (mainUserRepository.findByMobileAndIsActiveTrue(request.getMobile()).isPresent()) {
        log.warn("Registration failed: Mobile already exists - {}", request.getMobile());
        throw new MobileAlreadyExistsException(request.getMobile());
        }

        // 2. Map DTO to Entity and Encrypt Password
        MainUser user = new MainUser();
        user.setEmail(request.getEmail());
        // CRITICAL STEP: Use PasswordEncoder to securely hash the password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setMobile(request.getMobile());

        // Convert role String to Enum
        try {
            user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid role provided: {}. Defaulting to END_USER.", request.getRole());
            user.setRole(UserRole.END_USER);
        }

        // Set default values
        user.setIsEmailVerified(false);
        user.setIsMobileVerified(false);
        user.setIsActive(true);
        user.setCountry("India"); // Default country

        // 3. Save the new user to the database
        MainUser savedUser = mainUserRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        // 4. Perform automatic login (Authenticate the newly created user)
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        Authentication fullyAuthenticated = authenticationManager.authenticate(authentication);

        // Set the Authentication object in the Security Context
        SecurityContextHolder.getContext().setAuthentication(fullyAuthenticated);

        // 5. Generate the JWT tokens
        String accessToken = tokenProvider.generateToken(fullyAuthenticated);
        String refreshToken = tokenProvider.generateRefreshToken(fullyAuthenticated);

        // Calculate expiration time for the response
        Long accessTokenExpirationInMs = tokenProvider.getExpirationDateFromJWT(accessToken).getTime() - System.currentTimeMillis();
       // System.out.println("user found with emial :-"+ request.getEmail());

       UserResponse u = UserResponse.fromMainUser(mainUserRepository.findByEmailAndIsActiveTrue(request.getEmail()).get());
         return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                accessTokenExpirationInMs,
                u  
        );
    }

    @Override
    public UserResponse completeProfile(Long userId, CompleteProfileRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'completeProfile'");
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshToken'");
    }

    @Override
    public void logout(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public void forgotPassword(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'forgotPassword'");
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetPassword'");
    }
    
}
