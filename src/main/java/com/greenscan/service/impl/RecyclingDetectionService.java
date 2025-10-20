package com.greenscan.service.impl;

// Java Standard Imports
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

// Spring Framework Imports
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// JSON/Serialization Imports (Requires Jackson dependency)
import com.fasterxml.jackson.databind.ObjectMapper;

// Google GenAI SDK Imports (We only import available classes)
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;

// Custom Data Record Import
import com.greenscan.entity.RecyclingData;

@Service
public class RecyclingDetectionService {

    private final Client geminiClient;
    private final ObjectMapper objectMapper;
    private final String MODEL_NAME = "gemini-2.5-flash"; 

    /**
     * Initializes the service using the Client.builder() pattern
     * and injects the API key from application.properties.
     */
    public RecyclingDetectionService(
            @Value("${ai_detection_api_key}") String apiKey) {
        
        this.geminiClient = Client.builder()
                .apiKey(apiKey)
                .build();
                
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Analyzes an image of a recyclable item using Gemini and returns structured data.
     */
    public RecyclingData analyzeRecyclingItem(File imageFile) throws IOException {
        
        // --- 1. Prepare the Image Content ---
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String mimeType = Files.probeContentType(imageFile.toPath());
        
        Part imagePart = Part.fromBytes(imageBytes, mimeType != null ? mimeType : "image/jpeg");
        
        // --- 2. Define the Text Prompt ---
        String prompt = "Analyze the item for recycling. Determine its name, specific material type, estimated weight (in grams), and provide a brief description of its condition. Return the result ONLY as a JSON object.";

        // --- 3. Configure for Structured JSON Output ---
        GenerateContentConfig config = GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(RecyclingData.getResponseSchema())
                .build();
        
        // --- 4. Build the Full Multimodal Request Content ---
        List<Content> contents = List.of(
            Content.builder()
                .parts(imagePart, Part.fromText(prompt))
                .role("user")
                .build()
        );

        // --- 5. Call the Gemini API ---
        // FIX: Using the most common, simple direct method overload: 
        // generateContent(modelId, contentsList, config)
        GenerateContentResponse response = geminiClient.models.generateContent(
                MODEL_NAME, 
                contents, 
                config
        );

        // --- 6. Parse the Structured JSON Output ---
        String jsonText = response.text();
        
        try {
             return objectMapper.readValue(jsonText, RecyclingData.class);
        } catch (Exception e) {
             System.err.println("Gemini API returned unparseable JSON:\n" + jsonText);
             throw new IOException("Failed to parse structured JSON response from AI model.", e);
        }
    }
}