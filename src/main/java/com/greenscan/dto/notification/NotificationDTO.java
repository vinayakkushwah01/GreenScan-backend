package com.greenscan.dto.notification;

import com.greenscan.dto.base.BaseDTO;
import com.greenscan.enums.NotificationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotificationDTO extends BaseDTO {
    private Long userId;
    private NotificationType type;
    private String title;
    private String message;
    private String imageUrl;
    private String actionUrl;
    private Boolean isRead;
    private LocalDateTime readAt;
    private LocalDateTime deliveredAt;
    private Boolean isPush;
    private Boolean isEmail;
    private Boolean isSms;
}
