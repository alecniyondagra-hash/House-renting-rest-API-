// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.controllers;

import com.housereting.houserenting.dto.request.CreateHouseRequest;
import com.housereting.houserenting.dto.request.UpdateHouseRequest;
import com.housereting.houserenting.dto.response.ApiResponse;
import com.housereting.houserenting.services.HouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/houses")
public class HouseController {

    @Autowired
    private HouseService houseService;

    // POST /api/v1/houses - landlord creates a house
    @PostMapping
    public ResponseEntity<ApiResponse> createHouse(
            @Valid @RequestBody CreateHouseRequest request,
            Principal principal) {
        return ResponseEntity.status(201)
                .body(houseService.createHouse(request, principal.getName()));
    }

    // GET /api/v1/houses - public, only APPROVED + available with pagination
    // also handles search: ?location=Kigali&minPrice=100&maxPrice=500
    @GetMapping
    public ResponseEntity<ApiResponse> getAllHouses(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (minPrice != null || maxPrice != null || location != null) {
            return ResponseEntity.ok(houseService.searchHouses(minPrice, maxPrice, location));
        }
        return ResponseEntity.ok(houseService.getAllHouses(page, size));
    }

    // GET /api/v1/houses/my-houses - landlord sees their own houses
    @GetMapping("/my-houses")
    public ResponseEntity<ApiResponse> getMyHouses(Principal principal) {
        return ResponseEntity.ok(houseService.getMyHouses(principal.getName()));
    }

    // GET /api/v1/houses/{id} - public
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getHouseById(@PathVariable Long id) {
        return ResponseEntity.ok(houseService.getHouseById(id));
    }

    // PATCH /api/v1/houses/{id} - landlord updates their house
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateHouse(
            @PathVariable Long id,
            @RequestBody UpdateHouseRequest request,
            Principal principal) {
        return ResponseEntity.ok(houseService.updateHouse(id, request, principal.getName()));
    }

    // DELETE /api/v1/houses/{id} - landlord deletes their house
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteHouse(
            @PathVariable Long id,
            Principal principal) {
        return ResponseEntity.ok(houseService.deleteHouse(id, principal.getName()));
    }
}
