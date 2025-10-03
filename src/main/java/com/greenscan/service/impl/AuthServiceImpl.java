package com.greenscan.service.impl;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.greenscan.security.UserDetailsServiceImpl;
import com.greenscan.security.UserPrincipal;
import com.greenscan.service.interfaces.AuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
    private final UserDetailsServiceImpl userDetailsServiceImpl;


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
          
        MainUser user = mainUserRepository.findByEmailAndIsActiveTrue(request.getEmail()) .orElseThrow(() -> new UsernameNotFoundException("User not  befofre nkjnasfound"));
        user.setFailedLoginAttempts(0);
        user.setIsActive(true);
        user.setLastLogin(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        mainUserRepository.save(user);
        return new AuthResponse(
                    accessToken,
                    refreshToken,
                    "Bearer",
                    accessTokenExpirationInMs,
                    u  
            );
        } catch (BadCredentialsException ex) {
            MainUser user = mainUserRepository.findByEmailAndIsActiveTrue(request.getEmail()) .orElseThrow(() -> new BadCredentialsException("User not found"));
            
                if(user!= null){
                    user.setFailedLoginAttempts(user.getFailedLoginAttempts()+1);
                    if(user.getFailedLoginAttempts() >5){
                        user.setIsActive(false);
                    }
                    mainUserRepository.save(user);
                }
                
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
        user.setIsEmailVerified(false);
        user.setIsMobileVerified(false);
        user.setIsActive(true);
        user.setCountry("India"); 

        MainUser savedUser = mainUserRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        Authentication fullyAuthenticated = authenticationManager.authenticate(authentication);

        SecurityContextHolder.getContext().setAuthentication(fullyAuthenticated);
        String accessToken = tokenProvider.generateToken(fullyAuthenticated);
        String refreshToken = tokenProvider.generateRefreshToken(fullyAuthenticated);

        Long accessTokenExpirationInMs = tokenProvider.getExpirationDateFromJWT(accessToken).getTime() - System.currentTimeMillis();
        
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
        MainUser u = mainUserRepository.findById(userId).get();
      return UserResponse.fromMainUser( 
                                    mainUserRepository.save(
                                        CompleteProfileRequest.updateUserFromRequest(u,request)
                                        )
                                        );


        //throw new UnsupportedOperationException("Unimplemented method 'completeProfile'");
    }

    @Override
 public AuthResponse refreshToken(String refreshToken) {

    if (!tokenProvider.validateToken(refreshToken)) {
        throw new RuntimeException("Invalid refresh token");
    }

    Claims claims = Jwts.parserBuilder()
            .setSigningKey(tokenProvider.getSigningKey())
            .build()
            .parseClaimsJws(refreshToken)
            .getBody();

    String tokenType = claims.get("type", String.class);
    if (tokenType == null || !tokenType.equals("refresh")) {
        throw new RuntimeException("Provided token is not a refresh token");
    }

    Long userId = Long.parseLong(claims.getSubject());
    String email = claims.get("email", String.class);
   

    MainUser u = mainUserRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

    UserDetails userDetails = userDetailsServiceImpl.loadUserById(userId);
    UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    String newAccessToken = tokenProvider.generateToken(authentication);

    String newRefreshToken = tokenProvider.generateRefreshToken(authentication);

    Date exp = tokenProvider.getExpirationDateFromJWT(newAccessToken);
    long expiresIn = exp.getTime() - System.currentTimeMillis();

    return new AuthResponse(
            newAccessToken,   
            newRefreshToken,  
            "Bearer",       
            expiresIn,       
            UserResponse.fromMainUser(u)                 
    );
}


    @Override
    public void logout(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

    @Override
    public String  changePassword(Long userId, String oldPassword, String newPassword) {
       MainUser mainUser = mainUserRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
         if(!passwordEncoder.matches(oldPassword, mainUser.getPassword())){
            throw new BadCredentialsException("Old password is incorrect");
         }
         mainUser.setPassword(passwordEncoder.encode(newPassword));
        mainUserRepository.save(mainUser);
        return"Password change succesfully";
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
