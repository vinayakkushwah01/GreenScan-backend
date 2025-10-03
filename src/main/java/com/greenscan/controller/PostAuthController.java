package com.greenscan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenscan.dto.request.ChangePasswordRequest;
import com.greenscan.dto.request.CompleteProfileRequest;
import com.greenscan.dto.response.AuthResponse;
import com.greenscan.dto.response.StringResponse;
import com.greenscan.dto.response.UserResponse;
import com.greenscan.service.impl.AuthServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/mainuser")
@RequiredArgsConstructor
public class PostAuthController {
    @Autowired 
    private AuthServiceImpl authService;

     @PostMapping("/register/complete/{id}")
    public ResponseEntity<UserResponse> completeProfile(@PathVariable  Long id, @RequestBody CompleteProfileRequest request){
        log.info("Received Complete profile  request for id: {}", id);
       UserResponse res =  authService.completeProfile(id, request);
       return ResponseEntity.ok(res);

    }
      @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/changePassword/{id}")
    public ResponseEntity<StringResponse> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request){
        log.info("Received change password request for id: {}", id);
       String response = authService.changePassword(id, request.getOldPassword(), request.getNewPassword());
       return ResponseEntity.ok(new StringResponse(response));
    }
}
