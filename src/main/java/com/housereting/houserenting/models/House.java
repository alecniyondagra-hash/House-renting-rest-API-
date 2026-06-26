// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026
// Description: House entity - represents the "houses" table in the database

package com.housereting.houserenting.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "houses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // title of the house listing e.g "Modern 3 bedroom in Kigali"
    @Column(name = "title", nullable = false)
    private String title;

    // detailed description of the house
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // physical location of the house e.g "Nyamirambo, Kigali"
    @Column(name = "location", nullable = false)
    private String location;

    // monthly rent price
    @Column(name = "price", nullable = false)
    private Double price;

    // number of rooms in the house
    @Column(name = "rooms", nullable = false)
    private Integer rooms;

    // true = house is available for booking, false = already rented
    @Column(name = "available")
    private Boolean available = true;

    // URL link to the house image/photo
    @Column(name = "image_url")
    private String imageUrl;

    // Status of the house listing:
    // "pending"  = waiting for admin approval
    // "approved" = visible to tenants
    // "rejected" = not approved by admin
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.PENDING; // every new house starts as pending

    // @ManyToOne = many houses can belong to one landlord
    // @JoinColumn creates a "landlord_id" column in the houses table
    // This links the house to the User who owns it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landlord_id", nullable = false)
    private User landlord;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // runs automatically before a new house is saved
    @PrePersist
    public void beforeSave() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // runs automatically before an existing house is updated
    @PreUpdate
    public void beforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // House status can only be one of these 3 values
    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}
