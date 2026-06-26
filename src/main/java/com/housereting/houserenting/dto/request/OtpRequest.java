// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// used for: verify OTP and forgot password requests
@Data
public class OtpRequest {

    @NotBlank(message = "Email is required")
    private String email;

    private String otp;
}
