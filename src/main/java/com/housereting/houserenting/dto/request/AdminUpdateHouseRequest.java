// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// request body when admin approves or rejects a house
@Data
public class AdminUpdateHouseRequest {

    @NotBlank(message = "Status is required")
    private String status; // APPROVED or REJECTED
}
