package com.greenscan.dto.response;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class AIDetectionResponse {
    private String itemName;
    private String materialType;
    private Double estimatedWeight;
    private Boolean isRecyclable;
    private Double confidenceScore;
    private String detectionId;
    private LocalDateTime processedAt;
}
