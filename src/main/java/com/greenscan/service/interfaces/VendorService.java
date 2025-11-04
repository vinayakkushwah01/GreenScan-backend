package com.greenscan.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.greenscan.dto.request.VendorProfileRequest;

import com.greenscan.dto.response.VendorProfileResponse;

public interface VendorService {
    public VendorProfileResponse getVendorProfile(Long  vendorId);
   //public VendorProfileResponse createVendorProfile(VendorProfileRequest request , Long mainUserId);
   //public VendorProfileResponse updateVendorProfile(String vendorId, VendorProfileRequest request);
   //public void deleteVendorProfile(String vendorId);
    public Page<VendorProfileResponse> listAllVendors(int pageNo);
    public List<VendorProfileResponse> searchVendorsByName(String name);
    public List<VendorProfileResponse> filterVendorsByLocation( Double latitude, Double longitude, Double radiusKm);
    VendorProfileResponse createVendorProfile(VendorProfileRequest request);
    String uploadKycDocument(MultipartFile file , Long mainUserId);

}
