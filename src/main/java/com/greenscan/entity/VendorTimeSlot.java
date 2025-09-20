package com.greenscan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "vendor_time_slots", indexes = {
    @Index(name = "idx_vendor_time_slot_vendor", columnList = "vendor_id"),
    @Index(name = "idx_vendor_time_slot_date", columnList = "slot_date"),
    @Index(name = "idx_vendor_time_slot_availability", columnList = "is_available")
})
@EqualsAndHashCode(callSuper = true)
public class VendorTimeSlot extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private VendorProfile vendor;

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "max_bookings")
    private Integer maxBookings = 5;

    @Column(name = "current_bookings")
    private Integer currentBookings = 0;

    @Column(name = "notes", length = 200)
    private String notes;

    public boolean canBook() {
        return isAvailable && currentBookings < maxBookings;
    }

    public void incrementBooking() {
        if (canBook()) {
            currentBookings++;
        }
    }

    public void decrementBooking() {
        if (currentBookings > 0) {
            currentBookings--;
        }
    }
}
