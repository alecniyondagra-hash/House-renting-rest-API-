// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.services;

import com.housereting.houserenting.dto.request.AdminUpdateHouseRequest;
import com.housereting.houserenting.dto.request.AdminUpdateUserRequest;
import com.housereting.houserenting.dto.response.ApiResponse;
import com.housereting.houserenting.dto.response.HouseResponse;
import com.housereting.houserenting.dto.response.UserResponse;
import com.housereting.houserenting.exception.BadRequestException;
import com.housereting.houserenting.exception.ResourceNotFoundException;
import com.housereting.houserenting.models.House;
import com.housereting.houserenting.models.User;
import com.housereting.houserenting.repositories.HouseRepository;
import com.housereting.houserenting.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository;

    // admin sees all users
    public ApiResponse getAllUsers() {
        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());

        return new ApiResponse(200, "Users retrieved successfully", users);
    }

    // admin sees all houses including PENDING ones
    public ApiResponse getAllHouses() {
        List<HouseResponse> houses = houseRepository.findAll()
                .stream()
                .map(HouseResponse::fromHouse)
                .collect(Collectors.toList());

        return new ApiResponse(200, "All houses retrieved successfully", houses);
    }

    // admin sees houses by status (PENDING, APPROVED, REJECTED)
    public ApiResponse getHousesByStatus(String status) {
        try {
            House.Status houseStatus = House.Status.valueOf(status.toUpperCase());
            List<HouseResponse> houses = houseRepository.findByStatus(houseStatus)
                    .stream()
                    .map(HouseResponse::fromHouse)
                    .collect(Collectors.toList());

            return new ApiResponse(200, "Houses retrieved successfully", houses);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status. Must be PENDING, APPROVED or REJECTED");
        }
    }

    // admin changes a user's role
    public ApiResponse updateUserRole(Long userId, AdminUpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        try {
            User.Role newRole = User.Role.valueOf(request.getRole().toUpperCase());
            user.setRole(newRole);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role. Must be ADMIN, LANDLORD, or TENANT");
        }

        userRepository.save(user);

        return new ApiResponse(200, "User role updated successfully", UserResponse.fromUser(user));
    }

    // admin approves or rejects a house listing
    public ApiResponse updateHouseStatus(Long houseId, AdminUpdateHouseRequest request) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new ResourceNotFoundException("House not found with id: " + houseId));

        try {
            House.Status newStatus = House.Status.valueOf(request.getStatus().toUpperCase());
            house.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status. Must be PENDING, APPROVED, or REJECTED");
        }

        houseRepository.save(house);

        return new ApiResponse(200, "House status updated successfully", HouseResponse.fromHouse(house));
    }
}
