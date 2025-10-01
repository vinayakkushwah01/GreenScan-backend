package com.greenscan.dto.response;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class NotificationResponse {
    private Long id;
    private String notificationType;
    private String title;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private String actionUrl;
    private Integer priorityLevel;   
}
