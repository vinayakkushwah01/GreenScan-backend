package com.greenscan.service.impl;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenscan.dto.request.VendorProfileRequest;
import com.greenscan.dto.response.VendorProfileResponse;
import com.greenscan.service.interfaces.VendorService;

public class VendorProfileServiceImpl implements VendorService {

    @Override
    public VendorProfileResponse getVendorProfile(String vendorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVendorProfile'");
    }

    @Override
    public VendorProfileResponse createVendorProfile(VendorProfileRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createVendorProfile'");
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