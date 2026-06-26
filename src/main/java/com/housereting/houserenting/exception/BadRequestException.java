// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.exception;

// thrown when request data is invalid → 400
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
