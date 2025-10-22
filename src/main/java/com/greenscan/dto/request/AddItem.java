package com.greenscan.dto.request;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class AddItem {
    Long cartId;
    File img;
    String imgPath;
   

    
    
}
