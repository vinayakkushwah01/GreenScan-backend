package com.greenscan.entity;

import com.greenscan.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_email", columnList = "email"),
    @Index(name = "idx_user_mobile", columnList = "mobile"),
    @Index(name = "idx_user_role", columnList = "role")
})
@EqualsAndHashCode(callSuper = true)
public class MainUser extends BaseEntity {

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mobile", unique = true)
    private String mobile;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;

    @Column(name = "is_mobile_verified")
    private Boolean isMobileVerified = false;

    @Column(name = "last_login")
    private java.time.LocalDateTime lastLogin;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked_until")
    private java.time.LocalDateTime accountLockedUntil;

    // Address fields
    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "country")
    private String country = "India";

    // Location fields
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "location_verified")
    private Boolean locationVerified = false;

    @Column(name = "location_updated_at")
    private java.time.LocalDateTime locationUpdatedAt;

    // FCM Token for push notifications
    @Column(name = "fcm_token")
    private String fcmToken;

    // Preferences
    @Column(name = "email_notifications_enabled")
    private Boolean emailNotificationsEnabled = true;

    @Column(name = "push_notifications_enabled")
    private Boolean pushNotificationsEnabled = true;

    @Column(name = "sms_notifications_enabled")
    private Boolean smsNotificationsEnabled = false;

    public boolean isAccountLocked() {
        return accountLockedUntil != null && accountLockedUntil.isAfter(java.time.LocalDateTime.now());
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return getIsActive() && isEmailVerified;
    }
}
