package com.greenscan.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.greenscan.dto.request.EndUserProfileRequest;
import com.greenscan.dto.response.ApiResponse;
import com.greenscan.dto.response.EndUserProfileResponse;
import com.greenscan.entity.MainUser;
import com.greenscan.exception.custom.FileUploadException;
import com.greenscan.service.interfaces.EndUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EndUserProfileController {

    private final EndUserService endUserService;

    // Get my profile
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<EndUserProfileResponse>> getMyProfile(Authentication auth) {
        MainUser user = (MainUser) auth.getPrincipal();
        EndUserProfileResponse profile = endUserService.getEndUserProfile(user);
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profile));
    }

    // Get profile by user ID (Admin only)
    // @GetMapping("/profile/{userId}")
    // public ResponseEntity<ApiResponse<EndUserProfileResponse>> getProfileById(@PathVariable String userId) {
    //     EndUserProfileResponse profile = endUserService.getEndUserProfile(userId);
    //     return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profile));
    // }

    // Create new profile
    @PostMapping("/profile/create")
    public ResponseEntity<ApiResponse<EndUserProfileResponse>> createProfile(EndUserProfileRequest request) {
       // MainUser user = (MainUser) auth.getPrincipal();
        EndUserProfileResponse profile = endUserService.createEndUserProfile(request);
        return ResponseEntity.ok(ApiResponse.success("Profile created successfully", profile));
    }

    // Get my coins balance
    @GetMapping("/coins/balance")
    public ResponseEntity<ApiResponse<String>> getCoinsBalance(Authentication auth) {
        MainUser user = (MainUser) auth.getPrincipal();
        EndUserProfileResponse profile = endUserService.getEndUserProfile(user);
        return ResponseEntity.ok(ApiResponse.success("Balance: " + profile.getGreenCoinsBalance(), null));
    }

    // Get my eco score
    @GetMapping("/eco-score")
    public ResponseEntity<ApiResponse<Integer>> getEcoScore(Authentication auth) {
        MainUser user = (MainUser) auth.getPrincipal();
        EndUserProfileResponse profile = endUserService.getEndUserProfile(user);
        return ResponseEntity.ok(ApiResponse.success("Eco Score", profile.getEcoScore()));
    }

    // Get my statistics
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<EndUserProfileResponse>> getMyStats(Authentication auth) {
        MainUser user = (MainUser) auth.getPrincipal();
        EndUserProfileResponse profile = endUserService.getEndUserProfile(user);
        return ResponseEntity.ok(ApiResponse.success("Statistics retrieved successfully", profile));
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadProfileImage(
        @RequestPart("image") MultipartFile image , Long id ){
            try{
           String ans = endUserService.uploadProfileImg(id, convertMultiPartToFile(image));
             return ResponseEntity.ok(ApiResponse.success(ans));
            }
            catch(Exception e ){
                 throw new FileUploadException("Failed to upload profile image: " + e.getMessage());    
            }
    }
    
    @PostMapping(value = "/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateProfileImage(
        @RequestPart("image") MultipartFile image , Long id ){
            try{
           String ans = endUserService.uploadProfileImg(id, convertMultiPartToFile(image));
             return ResponseEntity.ok(ApiResponse.success(ans));
            }
            catch(Exception e ){
                 throw new FileUploadException("Failed to upload profile image: " + e.getMessage());    
            }
    }
    


    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}