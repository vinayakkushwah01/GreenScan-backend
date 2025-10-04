package com.greenscan.service.interfaces;

import java.util.List;

import com.greenscan.dto.request.VendorProfileRequest;
import com.greenscan.dto.response.VendorProfileResponse;

public interface VendorService {
    public VendorProfileResponse getVendorProfile(String vendorId);
   //public VendorProfileResponse createVendorProfile(VendorProfileRequest request , Long mainUserId);
   //public VendorProfileResponse updateVendorProfile(String vendorId, VendorProfileRequest request);
   //public void deleteVendorProfile(String vendorId);
    public List<VendorProfileResponse> listAllVendors();
    public List<VendorProfileResponse> searchVendorsByName(String name);
    public List<VendorProfileResponse> filterVendorsByLocation( Double latitude, Double longitude, Double radiusKm);
    VendorProfileResponse createVendorProfile(VendorProfileRequest request);

}
