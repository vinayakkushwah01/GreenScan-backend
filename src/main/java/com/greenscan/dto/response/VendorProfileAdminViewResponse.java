package com.greenscan.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import com.greenscan.entity.CloudinaryFile;
import com.greenscan.entity.VendorProfile;
import com.greenscan.enums.ApprovalStatus;
import com.greenscan.service.impl.CloudinaryService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class VendorProfileAdminViewResponse {
    
 
    private Long id;
    private Long userId;
    private String businessName;
    private String businessRegistrationNumber;
    private String gstNumber;
    private String panNumber;

    
    private List<String> kycFiles;
    private List<byte[]> kycDocs; // List of file contents
    
    private String serviceCities;
    private Integer serviceRadiusKm;
    private List<String> workingDays;
    private LocalTime workingStartTime;
    private LocalTime workingEndTime;


    private Integer dailyPickupCapacity;
    private BigDecimal maxWeightPerPickupKg;


    private ApprovalStatus approvalStatus;
    private String rejectionReason;
    private Boolean kycVerified;
    private LocalDateTime kycVerifiedAt;

    // 🔹 Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public VendorProfileAdminViewResponse(VendorProfile vendor) {
    if (vendor == null) return;

    this.id = vendor.getId();
    this.userId = vendor.getUser() != null ? vendor.getUser().getId() : null;
    this.businessName = vendor.getBusinessName();
    this.businessRegistrationNumber = vendor.getBusinessRegistrationNumber();
    this.gstNumber = vendor.getGstNumber();
    this.panNumber = vendor.getPanNumber();
    this.kycDocs = null; // populate later

        if (vendor.getKycFiles() != null && !vendor.getKycFiles().isEmpty()) {
        this.kycFiles = vendor.getKycFiles().stream()
                              .map(CloudinaryFile::getFileName)
                              .toList();
    } else {
        this.kycFiles = null;
    }
    this.kycVerified = vendor.getKycVerified();
    this.kycVerifiedAt = vendor.getKycVerifiedAt();
    this.serviceCities = String.join(",", vendor.getServiceCitiesList());
    this.serviceRadiusKm = vendor.getServiceRadiusKm();
    this.workingDays = vendor.getWorkingDaysList();
    this.workingStartTime = vendor.getWorkingStartTime();
    this.workingEndTime = vendor.getWorkingEndTime();
    this.dailyPickupCapacity = vendor.getDailyPickupCapacity();
    this.maxWeightPerPickupKg = vendor.getMaxWeightPerPickupKg();
    this.approvalStatus = vendor.getApprovalStatus();
    this.rejectionReason = vendor.getRejectionReason();
    this.createdAt = vendor.getCreatedAt();
    this.updatedAt = vendor.getUpdatedAt();
    }

   


}
