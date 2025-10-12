package com.greenscan.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.greenscan.dto.request.RewardAdminRequestDTO;
import com.greenscan.dto.response.RewardAdminResponseDTO;
import com.greenscan.dto.response.VendorProfileAdminViewResponse;
import com.greenscan.entity.VendorProfile;
import com.greenscan.exception.custom.FileUploadException;
import com.greenscan.service.impl.AdminProfileServiceImpl;
import com.greenscan.service.impl.RewardAdminServiceImpl;
import com.greenscan.service.interfaces.RewardAdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


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

     // Get all vendor profiles
    @GetMapping("/vendors")
    public ResponseEntity<List<VendorProfileAdminViewResponse>> getAllVendors() {
        List<VendorProfileAdminViewResponse> vendors = adminProfileService.getAllVendorsProfiles();
        return ResponseEntity.ok(vendors);
    }

    // Reject vendor profile
    @PostMapping("/v/{id}/reject")
    public ResponseEntity<String> rejectVendorProfile(
            @PathVariable("id") Long vendorId,
            @RequestParam("reason") String reason,
            @RequestParam("adminId") Long adminId) {

        String message = adminProfileService.rejectVendorProfile(vendorId, reason, adminId);
        return ResponseEntity.ok(message);
    }

    // Block vendor profile
    @PostMapping("/v/{id}/block")
    public ResponseEntity<String> blockVendorProfile(
            @PathVariable("id") Long vendorId,
            @RequestParam("reason") String reason) {

        String message = adminProfileService.blockVendorsProfile(vendorId, reason);
        return ResponseEntity.ok(message);
    }

    // Unblock vendor profile
    @PostMapping("/v/{id}/unblock")
    public ResponseEntity<String> unblockVendorProfile(
            @PathVariable("id") Long vendorId,
            @RequestParam("reason") String reason) {

        String message = adminProfileService.unblockVendorsProfile(vendorId, reason);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/v/blocked")
    public ResponseEntity<List<VendorProfileAdminViewResponse>> getAllBlockedVendors() {
        List<VendorProfileAdminViewResponse> blockedVendors = adminProfileService.getAllBlockedVendorsProfiles();
        return ResponseEntity.ok(blockedVendors);
    }

    // Get all rejected vendor profiles
    @GetMapping("/v/rejected")
    public ResponseEntity<List<VendorProfileAdminViewResponse>> getAllRejectedVendors() {
        List<VendorProfileAdminViewResponse> rejectedVendors = adminProfileService.getAllRejectedVendorsProfiles();
        return ResponseEntity.ok(rejectedVendors);
    }

//Reward Management APIs can be added here

    @Autowired
    private RewardAdminServiceImpl rewardAdminService;

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
    //  Create a new reward
 @PostMapping(value = "/rewards/create")
    public ResponseEntity<RewardAdminResponseDTO> createReward(
         @RequestBody  RewardAdminRequestDTO request
    ) {
        RewardAdminResponseDTO response = rewardAdminService.createReward(request);
        //convertMultiPartToFile(multipartFile)
        return ResponseEntity.ok(response);
    }
    
    
    // Update reward
    @PutMapping("/rewards/{id}/update")
    public ResponseEntity<RewardAdminResponseDTO> updateReward(
            @PathVariable("id") Long rewardId,
             @RequestBody RewardAdminRequestDTO request
                ) {
        RewardAdminResponseDTO updated = rewardAdminService.updateReward(rewardId, request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping(value = "/rewards/{id}/upload-img", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity <String> uploadImage
    ( @PathVariable("id") Long rewardId, @RequestParam("rewardImg") MultipartFile rewardImg , @RequestParam("partnerLogo") MultipartFile partnerLogo) {
        try {
            // Convert MultipartFile to File
            File rewardImgFile = convertMultiPartToFile(rewardImg);
            File partnerLogoFile = convertMultiPartToFile(partnerLogo);

            rewardAdminService.uploadImg(rewardImgFile, partnerLogoFile, rewardId);
            // Delete temp file
            rewardImgFile.delete();
            partnerLogoFile.delete();

            return ResponseEntity.ok("Files uploaded successfully");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }
    

    // Get all rewards with optional filters
    @GetMapping("/rewards")
    public ResponseEntity<List<RewardAdminResponseDTO>> getAllRewards(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String approvalStatus) {
        List<RewardAdminResponseDTO> rewards = rewardAdminService.getAllRewards(category, isActive, approvalStatus);
        return ResponseEntity.ok(rewards);
    }

    // Get reward by ID
    @GetMapping("/rewards/{id}")
    public ResponseEntity<RewardAdminResponseDTO> getRewardById(@PathVariable("id") Long rewardId) {
        RewardAdminResponseDTO reward = rewardAdminService.getRewardById(rewardId);
        return ResponseEntity.ok(reward);
    }

    // Update approval status
    @PatchMapping("/rewards/{id}/approval")
    public ResponseEntity<RewardAdminResponseDTO> updateApprovalStatus(
            @PathVariable("id") Long rewardId,
            @RequestParam String approvalStatus,
            @RequestParam(required = false) String adminNotes) {
        RewardAdminResponseDTO updated = rewardAdminService.updateApprovalStatus(rewardId, approvalStatus, adminNotes);
        return ResponseEntity.ok(updated);
    }

    // Update reward quantity
    @PatchMapping("/rewards/{id}/quantity")
    public ResponseEntity<RewardAdminResponseDTO> updateQuantity(
            @PathVariable("id") Long rewardId,
            @RequestParam Integer newQuantity) {
        RewardAdminResponseDTO updated = rewardAdminService.updateQuantity(rewardId, newQuantity);
        return ResponseEntity.ok(updated);
    }

    // Soft delete reward (set inactive)
    @DeleteMapping("/rewards/{id}/delete")
    public ResponseEntity<Void> deleteReward(@PathVariable("id") Long rewardId) {
        rewardAdminService.deleteReward(rewardId);
        return ResponseEntity.noContent().build();
    }

    
    //NGO Management APIs can be added here
    
}
