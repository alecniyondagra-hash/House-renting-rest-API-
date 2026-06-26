// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

// generates a random 6-digit OTP code for email verification and password reset
@Component
public class OtpGenerator {

    public static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // generates number between 100000-999999
        return String.valueOf(otp);
    }
}
