package com.greenscan.entity;

import com.greenscan.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "vendor_profiles", indexes = {
    @Index(name = "idx_vendor_approval_status", columnList = "approval_status"),
    @Index(name = "idx_vendor_service_cities", columnList = "service_cities")
})
@EqualsAndHashCode(callSuper = true)
public class VendorProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private MainUser user;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_registration_number", unique = true)
    private String businessRegistrationNumber;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "pan_number")
    private String panNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Column(name = "approved_by_admin_id")
    private Long approvedByAdminId;

    @Column(name = "approved_at")
    private java.time.LocalDateTime approvedAt;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    // Service Areas
    @Column(name = "service_cities", length = 1000)
    private String serviceCities; // Comma-separated list

    @Column(name = "service_radius_km")
    private Integer serviceRadiusKm = 10;

    // Working Hours
    @Column(name = "working_start_time")
    private LocalTime workingStartTime = LocalTime.of(8, 0);

    @Column(name = "working_end_time")
    private LocalTime workingEndTime = LocalTime.of(18, 0);

    @Column(name = "working_days", length = 50)
    private String workingDays = "Monday,Tuesday,Wednesday,Thursday,Friday,Saturday"; // Comma-separated

    // Capacity
    @Column(name = "daily_pickup_capacity")
    private Integer dailyPickupCapacity = 20;

    @Column(name = "max_weight_per_pickup_kg", precision = 8, scale = 2)
    private BigDecimal maxWeightPerPickupKg = BigDecimal.valueOf(50.0);

    // Financial
    @Column(name = "platform_commission_rate", precision = 5, scale = 2)
    private BigDecimal platformCommissionRate = BigDecimal.valueOf(10.00); // 10%

    @Column(name = "pending_payment_amount", precision = 12, scale = 2)
    private BigDecimal pendingPaymentAmount = BigDecimal.ZERO;

    @Column(name = "payment_limit", precision = 12, scale = 2)
    private BigDecimal paymentLimit = BigDecimal.valueOf(10000.00);

    @Column(name = "total_revenue", precision = 15, scale = 2)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "total_platform_fee_paid", precision = 15, scale = 2)
    private BigDecimal totalPlatformFeePaid = BigDecimal.ZERO;

    // Statistics
    @Column(name = "total_pickups_completed")
    private Integer totalPickupsCompleted = 0;

    @Column(name = "total_weight_collected_kg", precision = 10, scale = 2)
    private BigDecimal totalWeightCollectedKg = BigDecimal.ZERO;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "total_ratings")
    private Integer totalRatings = 0;

    // KYC Documents
   
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id") // foreign key in CloudinaryFile table
    private List<CloudinaryFile> kycFiles;

    @Column(name = "kyc_verified")
    private Boolean kycVerified = false;

    @Column(name = "kyc_verified_at")
    private java.time.LocalDateTime kycVerifiedAt;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PickupAssistant> assistants = new ArrayList<>();


    // Helper methods
    public boolean canAcceptMorePickups() {
        // Logic to check if vendor can accept more pickups based on daily capacity
        return true; // Simplified for now
    }

    public boolean isWithinPaymentLimit(BigDecimal amount) {
        return pendingPaymentAmount.add(amount).compareTo(paymentLimit) <= 0;
    }

    public List<String> getServiceCitiesList() {
        if (serviceCities != null && !serviceCities.trim().isEmpty()) {
            return List.of(serviceCities.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getWorkingDaysList() {
        if (workingDays != null && !workingDays.trim().isEmpty()) {
            return List.of(workingDays.split(","));
        }
        return new ArrayList<>();
    }
}
