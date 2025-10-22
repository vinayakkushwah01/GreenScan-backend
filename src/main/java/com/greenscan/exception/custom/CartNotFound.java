package com.greenscan.exception.custom;

import lombok.Data;

@Data
public class CartNotFound extends RuntimeException{
      
    public CartNotFound(String message) {
        super(message);
    }
    
}
