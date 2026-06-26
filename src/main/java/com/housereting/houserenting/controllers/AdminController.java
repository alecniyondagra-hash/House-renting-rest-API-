// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.controllers;

import com.housereting.houserenting.dto.request.AdminUpdateHouseRequest;
import com.housereting.houserenting.dto.request.AdminUpdateUserRequest;
import com.housereting.houserenting.dto.response.ApiResponse;
import com.housereting.houserenting.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // GET /api/v1/admin/users - admin sees all users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    // GET /api/v1/admin/houses - admin sees all houses
    // optional: ?status=PENDING to filter by status
    @GetMapping("/houses")
    public ResponseEntity<ApiResponse> getAllHouses(
            @RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(adminService.getHousesByStatus(status));
        }
        return ResponseEntity.ok(adminService.getAllHouses());
    }

    // PATCH /api/v1/admin/users/{id} - admin changes user role
    @PatchMapping("/users/{id}")
    public ResponseEntity<ApiResponse> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateUserRequest request) {
        return ResponseEntity.ok(adminService.updateUserRole(id, request));
    }

    // PATCH /api/v1/admin/houses/{id} - admin approves or rejects house
    @PatchMapping("/houses/{id}")
    public ResponseEntity<ApiResponse> updateHouseStatus(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateHouseRequest request) {
        return ResponseEntity.ok(adminService.updateHouseStatus(id, request));
    }
}
