// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.services;

import com.housereting.houserenting.dto.request.LoginRequest;
import com.housereting.houserenting.dto.request.OtpRequest;
import com.housereting.houserenting.dto.request.ResetPasswordRequest;
import com.housereting.houserenting.dto.request.SignupRequest;
import com.housereting.houserenting.dto.response.ApiResponse;
import com.housereting.houserenting.dto.response.UserResponse;
import com.housereting.houserenting.exception.BadRequestException;
import com.housereting.houserenting.exception.ResourceNotFoundException;
import com.housereting.houserenting.models.User;
import com.housereting.houserenting.repositories.UserRepository;
import com.housereting.houserenting.utils.EmailService;
import com.housereting.houserenting.utils.JwtUtil;
import com.housereting.houserenting.utils.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    // register a new user
    public ApiResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());

        // set role if provided, default to TENANT
        if (request.getRole() != null) {
            try {
                user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                user.setRole(User.Role.TENANT);
            }
        }

        // generate OTP and set expiry to 10 minutes from now
        String otp = OtpGenerator.generateOtp();
        user.setOtp(otp);
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(10));
        user.setIsVerified(false);

        userRepository.save(user);
        emailService.sendOtpEmail(user.getEmail(), otp);

        return new ApiResponse(201, "Account created. Check your email for the verification code.",
                UserResponse.fromUser(user));
    }

    // verify OTP code
    public ApiResponse verifyOtp(OtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getIsVerified()) {
            throw new BadRequestException("Account already verified");
        }

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new BadRequestException("Invalid OTP code");
        }

        // check if OTP has expired
        if (user.getOtpExpiresAt() == null || LocalDateTime.now().isAfter(user.getOtpExpiresAt())) {
            throw new BadRequestException("OTP has expired. Please request a new one");
        }

        user.setIsVerified(true);
        user.setOtp(null);
        user.setOtpExpiresAt(null);
        userRepository.save(user);

        return new ApiResponse(200, "Account verified successfully", UserResponse.fromUser(user));
    }

    // login and return JWT token
    public ApiResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        if (!user.getIsVerified()) {
            throw new BadRequestException("Please verify your email before logging in");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", UserResponse.fromUser(user));

        return new ApiResponse(200, "Login successful", data);
    }

    // send password reset OTP
    public ApiResponse forgotPassword(OtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // generate OTP and set expiry to 10 minutes from now
        String otp = OtpGenerator.generateOtp();
        user.setOtp(otp);
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), otp);

        return new ApiResponse(200, "Password reset code sent to your email", null);
    }

    // reset password using OTP
    public ApiResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new BadRequestException("Invalid OTP code");
        }

        // check if OTP has expired
        if (user.getOtpExpiresAt() == null || LocalDateTime.now().isAfter(user.getOtpExpiresAt())) {
            throw new BadRequestException("OTP has expired. Please request a new one");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiresAt(null);
        userRepository.save(user);

        return new ApiResponse(200, "Password reset successfully", null);
    }
}
