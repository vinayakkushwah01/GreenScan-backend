package com.greenscan.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import com.greenscan.dto.response.VendorProfileAdminViewResponse;
import com.greenscan.entity.VendorProfile;

public interface AdminProfileService {

    // Define methods related to admin profile management here
 //MainUser Profile it self is Admin profile
    // delete Admin profile
    // get Admin dto by id
    // get all Admin profiles
    //get pending profile approval of vendors
    Page<VendorProfileAdminViewResponse> getPendingVendorsProfiles(int page);
    public VendorProfileAdminViewResponse getVendorById(Long vendorId);
    String approveVendorProfile(Long vendorId , Long adminId);
    String rejectVendorProfile(Long vendorId , String reason , Long adminId);
    String blockVendorsProfile(Long vendorId , String reason);
    String unblockVendorsProfile(Long vendorId , String reason);
    List<VendorProfileAdminViewResponse> getAllVendorsProfiles();
    List<VendorProfile> getAllBlockedVendorsProfiles();
    List<VendorProfile> getAllRejectedVendorsProfiles();


    //get pending profile approval of NGOs
    // List<NGOsProfile> getPendingNGOsProfiles();
    //get pending profile approval of AdsCompanyProfile
    // List<AdsCompanyProfile> getPendingAdsCompanyProfiles();

}   
