package com.greenscan.exception.custom;

public class RecyclingAnalysisException extends RuntimeException {

    private final int statusCode;

    public RecyclingAnalysisException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public RecyclingAnalysisException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }

    public RecyclingAnalysisException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }  
}
