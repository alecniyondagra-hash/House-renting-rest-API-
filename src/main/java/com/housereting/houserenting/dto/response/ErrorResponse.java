// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This class wraps ALL error responses in the project
// Every error returns this shape: { status, error }
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String error;
}
