// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// sends emails using the Gmail SMTP config in application.properties
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("House Renting - Verification Code");
        message.setText("Your verification code is: " + otp + "\n\nThis code expires in 10 minutes.");
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("House Renting - Password Reset Code");
        message.setText("Your password reset code is: " + otp + "\n\nThis code expires in 10 minutes.");
        mailSender.send(message);
    }
}
