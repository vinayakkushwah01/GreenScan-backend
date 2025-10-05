package com.greenscan.controller;

import com.greenscan.dto.request.VendorProfileRequest;

import com.greenscan.dto.response.VendorProfileResponse;
import com.greenscan.service.impl.VendorProfileServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/vendor") // Clear, versioned, and resource-specific path
@RequiredArgsConstructor
public class VendorProfileController {

    private final VendorProfileServiceImpl vendorProfileService;

  
    @PostMapping("/create")
    public ResponseEntity<VendorProfileResponse> createVendorProfile(
            @Valid @RequestBody VendorProfileRequest request) {

        VendorProfileResponse response = vendorProfileService.createVendorProfile(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
  
    // now upload the support doc to approve by admin
     @PostMapping("/upload-kyc/{mainUserId}")
    public ResponseEntity<String> uploadKyc(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long mainUserId) {

        String response = vendorProfileService.uploadKycDocument(file, mainUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}