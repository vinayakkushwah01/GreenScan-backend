package com.greenscan.dto.response;

import lombok.Data;

@Data
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    private String errorDetails;
    public static ApiResponseDTO<Object> error(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'error'");
    }
}
