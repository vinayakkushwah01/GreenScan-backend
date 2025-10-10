package com.greenscan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenscan.dto.request.NGOProfileRequest;
import com.greenscan.dto.response.NGOProfileResponse;
import com.greenscan.service.impl.NGOServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ngo")
@RequiredArgsConstructor
public class NGOController {
    @Autowired
    private NGOServiceImpl ngoServiceImpl;

    // Create a Ngo
    @PostMapping("/create_ngo")
    public ResponseEntity<NGOProfileResponse> createNGO(@RequestBody NGOProfileRequest request){
        NGOProfileResponse response = ngoServiceImpl.createNgo(request);
        return ResponseEntity.ok(response);
    }
    //Get all other NGos 
    @GetMapping("/get_all_ngos")
    public ResponseEntity<List<NGOProfileResponse>> getAllOtherNGOs(){
        List<NGOProfileResponse> response = ngoServiceImpl.getAllNgoForNgo();
        return ResponseEntity.ok(response);
    }
    
}
