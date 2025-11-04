package com.greenscan.service.impl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.greenscan.dto.request.VendorProfileRequest;
import com.greenscan.dto.response.VendorProfileResponse;
import com.greenscan.entity.CloudinaryFile;
import com.greenscan.entity.MainUser;
import com.greenscan.entity.VendorProfile;
import com.greenscan.enums.ApprovalStatus;
import com.greenscan.exception.custom.DuplicateResourceException;
import com.greenscan.exception.custom.ResourceNotFoundException;
import com.greenscan.repository.MainUserRepository;
import com.greenscan.repository.VendorProfileRepository;
import com.greenscan.service.interfaces.VendorService;

@Service
public class VendorProfileServiceImpl implements VendorService {

    @Autowired
    private  MainUserRepository mainUserRepository;
    @Autowired
    private VendorProfileRepository vendorProfileRepository;
    @Autowired
    private  CloudinaryService cloudinaryService;
    
private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    final int EARTH_RADIUS_KM = 6371;
    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS_KM * c;
}

    @Override
    public VendorProfileResponse createVendorProfile(VendorProfileRequest request) {
        MainUser mainUser = mainUserRepository.findById(request.getMainUserId())
        .orElseThrow(() -> new RuntimeException("MainUser not found with id: " + request.getMainUserId()));

        VendorProfile entity = VendorProfileRequest.toEntity(request, mainUser);
        try {
            vendorProfileRepository.save(entity);
            return VendorProfileResponse.fromEntity(mainUser, entity);

        }
         catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Vendor", "businessName", request.getBusinessName());
        }
    }

    @Override
    public String uploadKycDocument(MultipartFile file, Long mainUserId) {
        // Find the vendor profile
        VendorProfile vendorProfile = vendorProfileRepository.findByUserId(mainUserId)
                .orElseThrow(() -> new RuntimeException(
                        "Vendor profile not found for user ID: " + mainUserId));
    
        String name = vendorProfile.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename() + "_"
                + (vendorProfile.getKycFiles() != null ? vendorProfile.getKycFiles().size() : 0);
    
        try {
            CloudinaryFile c = cloudinaryService.uploadFile(file, name);
    
            if (vendorProfile.getKycFiles() == null) {
                vendorProfile.setKycFiles(new ArrayList<>());
            }
    
            // Add the uploaded file to vendor's KYC files
            vendorProfile.getKycFiles().add(c);
            vendorProfileRepository.save(vendorProfile);
            return "File uploaded successfully for : "+vendorProfile.getId();
            
        } catch (IOException e) {
            
            throw new RuntimeException("Error in uploading file: " + file.getOriginalFilename(), e);
        }
        catch (Exception e) {
            throw new RuntimeException("Error in uploading or encrypting file: " + file.getOriginalFilename(), e);
        }
    }
    

    // Un implemented methods from VendorService interface
    
    @Override
    public VendorProfileResponse getVendorProfile(Long vendorId) {
           // Fetch vendor by ID
            VendorProfile vendor = vendorProfileRepository.findById(vendorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with ID: " + vendorId));

            // Map entity to response DTO
            return VendorProfileResponse.toVendorProfileRespose(vendor);
    }
   @Override
    public Page<VendorProfileResponse> listAllVendors(int pageNo) {
        int pageSize = 10; // You can make this dynamic if needed
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // Fetch all approved and active vendors with pagination
        Page<VendorProfile> vendorPage =
                vendorProfileRepository.findByApprovalStatusAndIsActiveTrue(ApprovalStatus.APPROVED, pageable);

        // Convert each VendorProfile to VendorProfileResponse using your mapper
        Page<VendorProfileResponse> responsePage = vendorPage.map(VendorProfileResponse::toVendorProfileRespose);

        return responsePage;
    }

   @Override
    public List<VendorProfileResponse> searchVendorsByName(String name) {
        // Fetch vendors matching business name
        List<VendorProfile> vendors = vendorProfileRepository
                .findByBusinessNameContainingIgnoreCaseAndApprovalStatusAndIsActiveTrue(
                        name, ApprovalStatus.APPROVED);

        // Convert to response DTOs
        return vendors.stream()
                .map(VendorProfileResponse::toVendorProfileRespose)
                .collect(Collectors.toList());
    }

   @Override
    public List<VendorProfileResponse> filterVendorsByLocation(Double latitude, Double longitude, Double radiusKm) {
        // Fetch vendors within their service radius (query already filters approved + active)
        List<VendorProfile> vendors = vendorProfileRepository.findVendorsWithinRadius(latitude, longitude);

        // Optionally apply additional radius filtering if you want to override vendor.serviceRadiusKm
        List<VendorProfile> filtered = vendors.stream()
                .filter(v -> {
                    if (v.getUser() == null || v.getUser().getLatitude() == null || v.getUser().getLongitude() == null) {
                        return false;
                    }
                    double distance = calculateDistance(latitude, longitude, v.getUser().getLatitude(), v.getUser().getLongitude());
                    return distance <= radiusKm;
                })
                .toList();

        // Convert to response DTOs
        return filtered.stream()
                .map(VendorProfileResponse::toVendorProfileRespose)
                .collect(Collectors.toList());
    }



     
   
}