// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.controllers;

import com.housereting.houserenting.dto.request.CreateBookingRequest;
import com.housereting.houserenting.dto.response.ApiResponse;
import com.housereting.houserenting.services.BookingService;
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
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // POST /api/v1/bookings - tenant books a house
    @PostMapping
    public ResponseEntity<ApiResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            Principal principal) {
        return ResponseEntity.status(201)
                .body(bookingService.createBooking(request, principal.getName()));
    }

    // GET /api/v1/bookings - tenant sees their own bookings
    @GetMapping
    public ResponseEntity<ApiResponse> getMyBookings(Principal principal) {
        return ResponseEntity.ok(bookingService.getMyBookings(principal.getName()));
    }

    // GET /api/v1/bookings/my-houses - landlord sees ALL bookings across all houses
    @GetMapping("/my-houses")
    public ResponseEntity<ApiResponse> getAllMyHouseBookings(Principal principal) {
        return ResponseEntity.ok(bookingService.getAllMyHouseBookings(principal.getName()));
    }

    // GET /api/v1/bookings/house/{houseId} - landlord sees bookings for a specific house
    @GetMapping("/house/{houseId}")
    public ResponseEntity<ApiResponse> getBookingsByHouse(
            @PathVariable Long houseId,
            Principal principal) {
        return ResponseEntity.ok(bookingService.getBookingsByHouse(houseId, principal.getName()));
    }

    // PATCH /api/v1/bookings/{id} - landlord confirms or cancels a booking
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Principal principal) {
        String status = body.get("status");
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, status, principal.getName()));
    }

    // DELETE /api/v1/bookings/{id} - tenant cancels their own booking
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> cancelMyBooking(
            @PathVariable Long id,
            Principal principal) {
        return ResponseEntity.ok(bookingService.cancelMyBooking(id, principal.getName()));
    }
}
