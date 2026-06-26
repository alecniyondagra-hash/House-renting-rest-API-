// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.exception;

// thrown when a requested resource (user, house, booking) is not found → 404
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
