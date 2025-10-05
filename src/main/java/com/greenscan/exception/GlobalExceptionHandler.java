package com.greenscan.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.greenscan.dto.response.ApiResponse;
import com.greenscan.exception.custom.DuplicateResourceException;
import com.greenscan.exception.custom.EmailAlreadyExistsException;
import com.greenscan.exception.custom.FileUploadException;
import com.greenscan.exception.custom.MobileAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }
        @ExceptionHandler(MobileAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleMobileAlreadyExists(MobileAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

     @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid email or password");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
       
       System.out.println(ex.getLocalizedMessage());
       System.err.println(ex);
       
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Something went wrong. Please try again later."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


     @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> handleDuplicateResourceException(DuplicateResourceException ex) {
        
        HttpStatus status = HttpStatus.CONFLICT; 
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", "Conflict");
        body.put("message", ex.getMessage()); 
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(FileUploadException.class)
public ResponseEntity<Map<String, Object>> handleFileUploadException(FileUploadException ex) {
    HttpStatus status =HttpStatus.BAD_REQUEST;
    Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", ex.getMessage()); // includes the file name
        body.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(body, status);
    }


}
