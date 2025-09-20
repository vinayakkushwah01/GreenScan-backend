package com.greenscan.entity;

import com.greenscan.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notification_user", columnList = "user_id"),
    @Index(name = "idx_notification_type", columnList = "notification_type"),
    @Index(name = "idx_notification_read", columnList = "is_read"),
    @Index(name = "idx_notification_created", columnList = "created_at")
})
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private MainUser user;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "action_url")
    private String actionUrl;

    @Column(name = "action_data", columnDefinition = "TEXT")
    private String actionData; // JSON for additional data

    @Column(name = "priority_level")
    private Integer priorityLevel = 1; // 1-5, higher number = higher priority

    @Column(name = "sent_via_email")
    private Boolean sentViaEmail = false;

    @Column(name = "sent_via_push")
    private Boolean sentViaPush = false;

    @Column(name = "sent_via_sms")
    private Boolean sentViaSms = false;

    // Related entity references
    @Column(name = "related_cart_id")
    private Long relatedCartId;

    @Column(name = "related_transaction_id")
    private Long relatedTransactionId;

    @Column(name = "related_campaign_id")
    private Long relatedCampaignId;

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
}
