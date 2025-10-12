package com.greenscan.service.impl;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.greenscan.dto.response.VendorProfileAdminViewResponse;
import com.greenscan.entity.VendorProfile;
import com.greenscan.enums.ApprovalStatus;
import com.greenscan.exception.custom.ResourceNotFoundException;
import com.greenscan.repository.VendorProfileRepository;
import com.greenscan.service.interfaces.AdminProfileService;

@Service
public class AdminProfileServiceImpl implements AdminProfileService {
    @Autowired
    private VendorProfileRepository vendorProfileRepository;
    @Autowired
    private CloudinaryService cloudinaryService;


    public VendorProfileAdminViewResponse mapVendorWithKyc(VendorProfile vendor) {
        if (vendor == null) return null;

        List<byte[]> kycDocs = null;

        if (vendor.getKycFiles() != null && !vendor.getKycFiles().isEmpty()) {
            kycDocs = vendor.getKycFiles().stream()
                            .map(file -> {
                                try {
                                    return cloudinaryService.getFile(file);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
        }

        VendorProfileAdminViewResponse dto = new VendorProfileAdminViewResponse(vendor);
        dto.setKycDocs(kycDocs);

        return dto;
    }

    @Override
    public Page<VendorProfileAdminViewResponse> getPendingVendorsProfiles(int page ) {
         Pageable pageable = PageRequest.of(page, 20);
        Page<VendorProfile> vendors = vendorProfileRepository.findByApprovalStatusAndIsActiveTrue(ApprovalStatus.PENDING , pageable);
       Page<VendorProfileAdminViewResponse> dtoPage = vendors.map(VendorProfileAdminViewResponse::new);
        return dtoPage;

    }
    @Override
    public VendorProfileAdminViewResponse getVendorById(Long vendorId) {
        return mapVendorWithKyc(vendorProfileRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("VendorProfile", "id", vendorId)));
    }

    @Override
    public String approveVendorProfile(Long vendorId,Long adminId) {
        VendorProfile vendor = vendorProfileRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("VendorProfile", "id", vendorId));
        vendor.setApprovalStatus(ApprovalStatus.APPROVED);
        vendor.setIsActive(true);
        vendor.setApprovedAt(LocalDateTime.now());
        vendor.setApprovedByAdminId(adminId);
        vendor.setKycVerified(true);
        vendorProfileRepository.save(vendor);
        return "Vendor profile approved successfully";
    }

    @Override
    public String rejectVendorProfile(Long vendorId, String reason ,Long adminId) {
        VendorProfile vendor = vendorProfileRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("VendorProfile", "id", vendorId));
        vendor.setApprovalStatus(ApprovalStatus.REJECTED);
        vendor.setIsActive(false);
        vendor.setRejectionReason(reason);
        vendor.setApprovedByAdminId(adminId);
        vendor.setKycVerified(false);
        vendorProfileRepository.save(vendor);
        return "Vendor profile rejected successfully";
       
    }

    @Override
    public String blockVendorsProfile(Long vendorId, String reason) {
        VendorProfile vendor = vendorProfileRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("VendorProfile", "id", vendorId));
        vendor.setApprovalStatus(ApprovalStatus.SUSPENDED);
        vendor.setIsActive(false);
        vendor.setRejectionReason(reason);
        vendorProfileRepository.save(vendor);
        return "Vendor profile blocked successfully";
    }


    @Override
    public String unblockVendorsProfile(Long vendorId, String reason) {
        VendorProfile vendor = vendorProfileRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("VendorProfile", "id", vendorId));
        vendor.setApprovalStatus(ApprovalStatus.PENDING);
        vendor.setIsActive(true);
        vendor.setRejectionReason("Unblocked: " + reason);
        vendorProfileRepository.save(vendor);
        return "Vendor profile unblocked successfully";
    }

    @Override
    public List<VendorProfileAdminViewResponse> getAllVendorsProfiles() {

        return vendorProfileRepository.findAll().stream()
                .map(VendorProfileAdminViewResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<VendorProfileAdminViewResponse> getAllBlockedVendorsProfiles() {
        return vendorProfileRepository.findByIsActive(false)
                .orElseGet(Collections::emptyList) // safer than orElse
                .stream()
                .map(VendorProfileAdminViewResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<VendorProfileAdminViewResponse> getAllRejectedVendorsProfiles() {
        return (List<VendorProfileAdminViewResponse>) vendorProfileRepository.findByApprovalStatusAndIsActiveTrue(ApprovalStatus.REJECTED, Pageable.unpaged())
        .getContent()
        .stream()
        .map(VendorProfileAdminViewResponse::new)
        .collect(Collectors.toList());
       
    }
    
}
