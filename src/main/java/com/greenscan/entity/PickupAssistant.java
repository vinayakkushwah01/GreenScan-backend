package com.greenscan.entity;

import com.greenscan.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "pickup_assistants", indexes = {
    @Index(name = "idx_pickup_assistant_vendor", columnList = "vendor_id"),
    @Index(name = "idx_pickup_assistant_user", columnList = "user_id"),
    @Index(name = "idx_pickup_assistant_status", columnList = "approval_status")
})
@EqualsAndHashCode(callSuper = true)
public class PickupAssistant extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private MainUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private VendorProfile vendor;

    @Column(name = "assistant_code", unique = true, nullable = false)
    private String assistantCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Column(name = "employee_id")
    private String employeeId;

    // Current location tracking
    @Column(name = "current_latitude")
    private Double currentLatitude;

    @Column(name = "current_longitude")
    private Double currentLongitude;

    @Column(name = "location_updated_at")
    private java.time.LocalDateTime locationUpdatedAt;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "is_on_duty")
    private Boolean isOnDuty = false;

    // Current assignment
    @Column(name = "current_cart_id")
    private Long currentCartId;

    // Statistics
    @Column(name = "total_pickups_completed")
    private Integer totalPickupsCompleted = 0;

    @Column(name = "total_distance_covered_km", precision = 10, scale = 2)
    private BigDecimal totalDistanceCoveredKm = BigDecimal.ZERO;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "total_ratings")
    private Integer totalRatings = 0;

    // Performance metrics
    @Column(name = "on_time_pickup_percentage", precision = 5, scale = 2)
    private BigDecimal onTimePickupPercentage = BigDecimal.ZERO;

    @Column(name = "successful_pickup_percentage", precision = 5, scale = 2)
    private BigDecimal successfulPickupPercentage = BigDecimal.ZERO;

    // Vehicle information
    @Column(name = "vehicle_type")
    private String vehicleType; // Bike, Auto, Van, etc.

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "vehicle_capacity_kg", precision = 8, scale = 2)
    private BigDecimal vehicleCapacityKg;

    // Emergency contact
    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_mobile")
    private String emergencyContactMobile;

    public void updateLocation(Double latitude, Double longitude) {
        this.currentLatitude = latitude;
        this.currentLongitude = longitude;
        this.locationUpdatedAt = java.time.LocalDateTime.now();
    }

    public boolean isLocationStale(int minutesThreshold) {
        if (locationUpdatedAt == null) return true;
        return locationUpdatedAt.plusMinutes(minutesThreshold)
            .isBefore(java.time.LocalDateTime.now());
    }
}
