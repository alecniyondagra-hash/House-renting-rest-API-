// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// request body when admin changes a user's role
@Data
public class AdminUpdateUserRequest {

    @NotBlank(message = "Role is required")
    private String role; // ADMIN, LANDLORD, TENANT
}
