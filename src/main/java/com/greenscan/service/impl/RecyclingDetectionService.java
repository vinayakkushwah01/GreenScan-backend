package com.greenscan.service.impl;

// Java Imports
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Spring Imports
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// JSON Handling
import com.fasterxml.jackson.databind.ObjectMapper;

// Google GenAI SDK
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;

// Custom Imports
import com.greenscan.entity.RecyclingData;
import com.greenscan.enums.MaterialType;
import com.greenscan.exception.custom.RecyclingAnalysisException;

@Service
public class RecyclingDetectionService {

    private final Client geminiClient;
    private final ObjectMapper objectMapper;
    private final String MODEL_NAME = "gemini-2.5-flash";

    public RecyclingDetectionService(@Value("${ai_detection_api_key}") String apiKey) {
        this.geminiClient = Client.builder()
                .apiKey(apiKey)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Analyze an uploaded recycling image using Gemini and classify material type.
     */
    public RecyclingData analyzeRecyclingItem(File imageFile) throws IOException {
         try {
        // 1️⃣ Read Image File
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String mimeType = Files.probeContentType(imageFile.toPath());
        Part imagePart = Part.fromBytes(imageBytes, mimeType != null ? mimeType : "image/jpeg");

        // 2️⃣ Build enum list for Gemini to reference
        String allowedEnumValues = Stream.of(MaterialType.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        // 3️⃣ Construct AI Prompt with dynamic enum list
        String prompt = """
        You are an expert recycling classification system.
        Analyze the provided image and identify:
        - itemName: the common name of the object.
        - materialType: choose EXACTLY one value from this enum list:
        """ + allowedEnumValues + """
        
        - estimatedWeight: approximate weight of the item in kilograms (kg, decimals allowed, e.g., 0.35).
        - conditionDescription: short description of its physical condition.

        Important Rules:
        - The 'materialType' value MUST match one of the enum names exactly (case-sensitive).
        - Return ONLY valid JSON (no markdown or text outside JSON).
        - Do not add explanations or extra fields.
        """;

        // 4️⃣ JSON Schema (auto-updated with enum names)
        String enumArray = Stream.of(MaterialType.values())
                .map(e -> "\"" + e.name() + "\"")
                .collect(Collectors.joining(", "));
        String responseSchema = """
        {
          "type": "object",
          "properties": {
            "itemName": { "type": "string" },
            "materialType": { "type": "string", "enum": [%s] },
            "estimatedWeight": { "type": "number" },
            "conditionDescription": { "type": "string" }
          },
          "required": ["itemName", "materialType", "estimatedWeight", "conditionDescription"]
        }
        """.formatted(enumArray);

        // 5️⃣ Configure Gemini for structured JSON output
      GenerateContentConfig config = GenerateContentConfig.builder()
        .responseMimeType("application/json")
        .responseSchema(RecyclingData.getResponseSchema()) // now compatible
        .build();

        // 6️⃣ Combine text and image inputs
        List<Content> contents = List.of(
                Content.builder()
                        .parts(imagePart, Part.fromText(prompt))
                        .role("user")
                        .build()
        );

        // 7️⃣ Call Gemini API
        GenerateContentResponse response = geminiClient.models.generateContent(
                MODEL_NAME,
                contents,
                config
        );

        String jsonText = response.text();

        // 8️⃣ Deserialize into Java object
        try {
            return objectMapper.readValue(jsonText, RecyclingData.class);
        } catch (Exception e) {
            System.err.println("⚠️ Unparseable JSON returned from Gemini:\n" + jsonText);
            throw new IOException("Failed to parse structured JSON from Gemini.", e);
        }
    } catch (IOException e) {
        throw new RecyclingAnalysisException("Failed to read image or parse AI response", e);
    } catch (com.google.genai.errors.ClientException e) {
        throw new RecyclingAnalysisException("AI API Error: " + e.getMessage(), e);
    } catch (Exception e) {
        throw new RecyclingAnalysisException("Unexpected error during recycling analysis", e);
    }
    }
}
