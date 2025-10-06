package com.greenscan.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/pending-vendors")
    public ResponseEntity<Page<VendorProfileAdminViewResponse>> getPendingVendors(
            @RequestParam(defaultValue = "0") int page) {

        Page<VendorProfileAdminViewResponse> pendingVendors = adminProfileService.getPendingVendorsProfiles(page);
        return ResponseEntity.ok(pendingVendors);
    } 
}
