package com.greenscan.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

// Spring Framework Imports
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

// Jackson (JSON) imports
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

// Custom imports
import com.greenscan.entity.RecyclingData;
import com.greenscan.service.impl.RecyclingDetectionService;

/**
 * REST Controller for handling image uploads and triggering the Gemini AI analysis.
 */
@RestController
@RequestMapping("/api/recycling")
public class RecyclingController {

    private final RecyclingDetectionService detectionService;
    private final ObjectMapper objectMapper;

    // Injects the service and ObjectMapper configured by Spring
    public RecyclingController(RecyclingDetectionService detectionService, ObjectMapper objectMapper) {
        this.detectionService = detectionService;
        this.objectMapper = objectMapper;
    }

    /**
     * Accepts a POST request with an image file, analyzes it, and returns a JSON response.
     * * @param file The image file uploaded from the client (e.g., Postman).
     * @return A ResponseEntity containing the formatted JSON string of the analysis.
     */
    @PostMapping(value = "/analyze", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> analyzeImage(@RequestParam("image") MultipartFile file) {

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image file is required.");
        }
        
        // Create a temporary file to pass to the core analysis service
        // Since the service is written to accept a java.io.File.
        Path tempFile = null;
        File tempIoFile = null;

        try {
            // 1. Save the uploaded MultipartFile to a temporary location
            tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            tempIoFile = tempFile.toFile();

            // 2. Call the analysis service with the temporary file
            RecyclingData result = detectionService.analyzeRecyclingItem(tempIoFile);

            // 3. Convert the Java Record into a readable JSON String for the HTTP response
            String jsonOutput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);

            // 4. Return the formatted JSON string
            return ResponseEntity.ok(jsonOutput);

        } catch (IOException e) {
            // Catches file I/O errors or errors thrown during API call/parsing
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error processing file or analyzing item: " + e.getMessage(), 
                e
            );
        } finally {
            // CRITICAL: Clean up the temporary file
            if (tempIoFile != null && tempIoFile.exists()) {
                try {
                    Files.delete(tempFile);
                } catch (IOException cleanupException) {
                    System.err.println("Warning: Could not delete temporary file: " + tempFile.toString());
                    cleanupException.printStackTrace();
                }
            }
        }
    }
}
