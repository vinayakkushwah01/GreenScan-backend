package com.greenscan.service.impl;
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
    public String approveVendorProfile(Long vendorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveVendorProfile'");
    }

    @Override
    public String rejectVendorProfile(Long vendorId, String reason) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rejectVendorProfile'");
    }

    @Override
    public String blockVendorsProfile(Long vendorId, String reason) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'blockVendorsProfile'");
    }

    @Override
    public String unblockVendorsProfile(Long vendorId, String reason) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unblockVendorsProfile'");
    }

    @Override
    public List<VendorProfile> getAllVendorsProfiles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllVendorsProfiles'");
    }
    
}
