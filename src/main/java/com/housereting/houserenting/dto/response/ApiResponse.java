// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This class wraps ALL success responses in the project
// Every endpoint returns this shape: { status, message, data }
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private int status;
    private String message;
    private Object data;
}
