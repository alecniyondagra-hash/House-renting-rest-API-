// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026
// Description: Booking entity - represents the "bookings" table in the database

package com.housereting.houserenting.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne = many bookings can be for the same house
    // @JoinColumn creates "house_id" column in bookings table
    // This links the booking to the specific house being booked
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    // @ManyToOne = many bookings can belong to the same tenant
    // @JoinColumn creates "tenant_id" column in bookings table
    // This links the booking to the user who made it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private User tenant;

    // the date and time when the booking was made
    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    // Booking status:
    // "pending"   = booking made, waiting for confirmation
    // "confirmed" = landlord confirmed the booking
    // "cancelled" = booking was cancelled
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.PENDING; // every new booking starts as pending

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // runs automatically before a new booking is saved
    @PrePersist
    public void beforeSave() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.bookingDate = LocalDateTime.now(); // booking date = when it was created
    }


    // runs automatically before an existing booking is updated
    @PreUpdate
    public void beforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    // Booking status can only be one of these 3 values
    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELLED
    }
}
