// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.dto.response;

import com.housereting.houserenting.models.Booking;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private Long id;
    private Long houseId;
    private String houseTitle;
    private String houseLocation;
    private Double housePrice;
    private Long tenantId;
    private String tenantName;
    private LocalDateTime bookingDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BookingResponse fromBooking(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setHouseId(booking.getHouse().getId());
        response.setHouseTitle(booking.getHouse().getTitle());
        response.setHouseLocation(booking.getHouse().getLocation());
        response.setHousePrice(booking.getHouse().getPrice());
        response.setTenantId(booking.getTenant().getId());
        response.setTenantName(booking.getTenant().getFirstName() + " " + booking.getTenant().getLastName());
        response.setBookingDate(booking.getBookingDate());
        response.setStatus(booking.getStatus().name());
        response.setCreatedAt(booking.getCreatedAt());
        response.setUpdatedAt(booking.getUpdatedAt());
        return response;
    }
}
