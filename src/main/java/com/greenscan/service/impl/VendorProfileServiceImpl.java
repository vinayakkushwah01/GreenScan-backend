package com.greenscan.service.impl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.greenscan.dto.request.VendorProfileRequest;
import com.greenscan.dto.response.VendorProfileResponse;
import com.greenscan.entity.CloudinaryFile;
import com.greenscan.entity.MainUser;
import com.greenscan.entity.VendorProfile;
import com.greenscan.exception.custom.DuplicateResourceException;
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

    @Override
    public VendorProfileResponse getVendorProfile(String vendorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVendorProfile'");
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
    public List<VendorProfileResponse> listAllVendors() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listAllVendors'");
    }

    @Override
    public List<VendorProfileResponse> searchVendorsByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchVendorsByName'");
    }

    @Override
    public List<VendorProfileResponse> filterVendorsByLocation(Double latitude, Double longitude, Double radiusKm) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filterVendorsByLocation'");
    }

@Override
public String uploadKycDocument(MultipartFile file, Long mainUserId) {
    // Find the vendor profile
    VendorProfile vendorProfile = vendorProfileRepository.findByUserId(mainUserId)
            .orElseThrow(() -> new RuntimeException(
                    "Vendor profile not found for user ID: " + mainUserId));

    String name = vendorProfile.getId() + "_" + file.getOriginalFilename() + "_"
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
}

     
   
}