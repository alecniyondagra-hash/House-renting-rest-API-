// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.controllers;

import com.housereting.houserenting.dto.request.LoginRequest;
import com.housereting.houserenting.dto.request.OtpRequest;
import com.housereting.houserenting.dto.request.ResetPasswordRequest;
import com.housereting.houserenting.dto.request.SignupRequest;
import com.housereting.houserenting.dto.response.ApiResponse;
import com.housereting.houserenting.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController means this class handles HTTP requests and returns JSON
// @RequestMapping sets the base URL for all endpoints in this controller
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST /api/v1/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(201).body(authService.signup(request));
    }

    // POST /api/v1/auth/verify-otp
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    // POST /api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // POST /api/v1/auth/forgot-password
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    // POST /api/v1/auth/reset-password
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }
}
