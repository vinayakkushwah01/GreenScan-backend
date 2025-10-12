package com.greenscan.controller.EndUserOtherController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenscan.dto.response.NGOProfileResponse;
import com.greenscan.service.impl.NGOServiceImpl;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/end_users/ngo")
@RequiredArgsConstructor
public class NGO_EndUserController {

    @Autowired
    private  NGOServiceImpl ngoServiceImpl;


    
    //get all NGOS
    @GetMapping("/list_all_ngo")
    public List<NGOProfileResponse> getAllNGOs() {
        return ngoServiceImpl.getAllNgos();
    }
    
}
