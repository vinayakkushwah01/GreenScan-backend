package com.greenscan.exception.custom;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    

    public DuplicateResourceException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s profile already exists with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}