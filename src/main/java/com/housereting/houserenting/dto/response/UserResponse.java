// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.dto.response;

import com.housereting.houserenting.models.User;
import lombok.Data;

import java.time.LocalDateTime;

// We never send the password back to the client
// This DTO controls exactly what user fields are exposed
@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
    private Boolean isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // converts a User entity into a UserResponse DTO
    public static UserResponse fromUser(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole().name());
        response.setIsVerified(user.getIsVerified());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
