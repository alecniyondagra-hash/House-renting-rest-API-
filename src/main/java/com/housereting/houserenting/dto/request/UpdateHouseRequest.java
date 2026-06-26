// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.dto.request;

import lombok.Data;

// All fields are optional - only provided fields will be updated
// This is for PATCH requests (partial update)
@Data
public class UpdateHouseRequest {
    private String title;
    private String description;
    private String location;
    private Double price;
    private Integer rooms;
    private Boolean available;
    private String imageUrl;
}
