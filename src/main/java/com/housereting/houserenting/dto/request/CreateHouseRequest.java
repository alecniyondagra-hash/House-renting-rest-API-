// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// fields required when a landlord creates a new house listing
@Data
public class CreateHouseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Price is required")
    private Double price;

    @NotNull(message = "Number of rooms is required")
    private Integer rooms;

    private Boolean available = true;

    private String imageUrl;
}
