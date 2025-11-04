package com.greenscan.controller.EndUserOtherController;

import com.greenscan.dto.request.VendorProfileRequest;
import com.greenscan.dto.response.VendorProfileResponse;
import com.greenscan.service.interfaces.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/end_users/vendors")
public class VendorInfoController {

    @Autowired
    private VendorService vendorService;

    @GetMapping("/{vendorId}")
    public ResponseEntity<VendorProfileResponse> getVendorProfile(@PathVariable Long vendorId) {
        VendorProfileResponse response = vendorService.getVendorProfile(vendorId);
        return ResponseEntity.ok(response);
    }

   
    @GetMapping("/list")
    public ResponseEntity<Page<VendorProfileResponse>> listAllVendors(
            @RequestParam(defaultValue = "0") int pageNo) {
        Page<VendorProfileResponse> response = vendorService.listAllVendors(pageNo);
        return ResponseEntity.ok(response);
    }

  
    @GetMapping("/search")
    public ResponseEntity<List<VendorProfileResponse>> searchVendors(
            @RequestParam String name) {
        List<VendorProfileResponse> response = vendorService.searchVendorsByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter/location")
    public ResponseEntity<List<VendorProfileResponse>> filterByLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10") Double radiusKm) {

        List<VendorProfileResponse> response = vendorService.filterVendorsByLocation(latitude, longitude, radiusKm);
        return ResponseEntity.ok(response);
    }
}
