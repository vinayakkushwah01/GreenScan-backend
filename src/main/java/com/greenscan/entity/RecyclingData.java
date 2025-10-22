package com.greenscan.entity;

import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import java.util.List;
import java.util.Map;

/**
 * A Java Record to hold the structured output from the Gemini API.
 */
public record RecyclingData(
        String itemName,
        String materialType,
        double estimatedWeight,
        String aiDetectionData,
        double aiConfidenceScore
) {
    /**
     * Defines the JSON Schema required for the API call. Uses the new Type("...") 
     * pattern confirmed necessary by the previous compilation errors.
     */
    public static Schema getResponseSchema() {
        return Schema.builder()
                .type(new Type("OBJECT")) 
                .properties(Map.of(
                        "itemName", Schema.builder()
                                .type(new Type("STRING")) 
                                .description("The common name of the recyclable item (e.g., Plastic Water Bottle, Cardboard Box).")
                                .build(),
                        "materialType", Schema.builder()
                                .type(new Type("STRING")) 
                                .description("The primary material (e.g., PET Plastic, Aluminum, Cardboard).")
                                .build(),
                        "estimatedWeight", Schema.builder()
                                .type(new Type("NUMBER")) 
                                .description("The estimated weight of the item in grams (g).")
                                .build(),
                        "aiDetectionData", Schema.builder()
                                .type(new Type("STRING")) 
                                .description("A brief description of the item's features detected in the image.")
                                .build(),
                        "aiConfidenceScore", Schema.builder()
                                .type(new Type("NUMBER")) 
                                .description("A score from 0.0 to 1.0 indicating the model's confidence.")
                                .build()
                ))
                .required(List.of("itemName", "materialType", "estimatedWeight", "aiDetectionData", "aiConfidenceScore"))
                .build();
    }
}
