package com.greenscan.dto.response;

import lombok.Data;

@Data
public class ApiErrorResponse {
    private int status;
    private String message;
    private long timestamp;

    public ApiErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    // getters and setters
}

