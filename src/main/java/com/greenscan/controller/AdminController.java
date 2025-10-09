package com.greenscan.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenscan.dto.response.VendorProfileAdminViewResponse;
import com.greenscan.entity.VendorProfile;
import com.greenscan.service.impl.AdminProfileServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminProfileServiceImpl adminProfileService;

    //  Get all pending vendor profiles with pagination
    @GetMapping("/pending-v")
    public ResponseEntity<Page<VendorProfileAdminViewResponse>> getPendingVendors(
            @RequestParam(defaultValue = "0") int page) {

        Page<VendorProfileAdminViewResponse> pendingVendors = adminProfileService.getPendingVendorsProfiles(page);
        return ResponseEntity.ok(pendingVendors);
    } 

    //  Get vendor by ID
    @GetMapping("/pending-v/{id}")
    public ResponseEntity<VendorProfileAdminViewResponse> getVendorById(
            @PathVariable("id") Long vendorId) {

        VendorProfileAdminViewResponse vendor = adminProfileService.getVendorById(vendorId);
        return ResponseEntity.ok(vendor);
    }

    //  Approve vendor profile
    @PostMapping("/pending-v/{id}/approve")
    public ResponseEntity<String> approveVendorProfile(
            @PathVariable("id") Long vendorId,
            @RequestParam("adminId") Long adminId) {

        String message = adminProfileService.approveVendorProfile(vendorId, adminId);
        return ResponseEntity.ok(message);
    }

    
}
