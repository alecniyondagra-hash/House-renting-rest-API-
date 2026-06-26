// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateBookingRequest {

    @NotNull(message = "House ID is required")
    private Long houseId;

    @NotNull(message = "Booking date is required")
    private LocalDateTime bookingDate;
}
