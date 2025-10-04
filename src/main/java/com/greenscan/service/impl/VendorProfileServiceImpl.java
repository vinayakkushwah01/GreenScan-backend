package com.greenscan.service.impl;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.greenscan.dto.request.VendorProfileRequest;
import com.greenscan.dto.response.VendorProfileResponse;
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
   
}