package com.greenscan.exception.custom;

public class CompressionError extends RuntimeException {
    public CompressionError(String msg){
        super(msg);
    }
}
