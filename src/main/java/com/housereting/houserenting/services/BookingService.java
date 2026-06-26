// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.services;

import com.housereting.houserenting.dto.request.CreateBookingRequest;
import com.housereting.houserenting.dto.response.ApiResponse;
import com.housereting.houserenting.dto.response.BookingResponse;
import com.housereting.houserenting.exception.BadRequestException;
import com.housereting.houserenting.exception.ForbiddenException;
import com.housereting.houserenting.exception.ResourceNotFoundException;
import com.housereting.houserenting.models.Booking;
import com.housereting.houserenting.models.House;
import com.housereting.houserenting.models.User;
import com.housereting.houserenting.repositories.BookingRepository;
import com.housereting.houserenting.repositories.HouseRepository;
import com.housereting.houserenting.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    // tenant books a house
    public ApiResponse createBooking(CreateBookingRequest request, String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (tenant.getRole() != User.Role.TENANT) {
            throw new ForbiddenException("Only tenants can book houses");
        }

        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() -> new ResourceNotFoundException("House not found with id: " + request.getHouseId()));

        if (house.getStatus() != House.Status.APPROVED) {
            throw new BadRequestException("This house is not available for booking yet");
        }

        if (!house.getAvailable()) {
            throw new BadRequestException("This house is already rented");
        }

        if (bookingRepository.existsByHouseIdAndTenantId(house.getId(), tenant.getId())) {
            throw new BadRequestException("You have already booked this house");
        }

        Booking booking = new Booking();
        booking.setHouse(house);
        booking.setTenant(tenant);
        booking.setBookingDate(request.getBookingDate());
        booking.setStatus(Booking.Status.PENDING);

        bookingRepository.save(booking);

        return new ApiResponse(201, "Booking created successfully", BookingResponse.fromBooking(booking));
    }

    // tenant sees their own bookings
    public ApiResponse getMyBookings(String tenantEmail) {
        User tenant = userRepository.findByEmail(tenantEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<BookingResponse> bookings = bookingRepository.findByTenantId(tenant.getId())
                .stream()
                .map(BookingResponse::fromBooking)
                .collect(Collectors.toList());

        return new ApiResponse(200, "Bookings retrieved successfully", bookings);
    }

    // landlord sees all bookings for a specific house
    public ApiResponse getBookingsByHouse(Long houseId, String landlordEmail) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new ResourceNotFoundException("House not found with id: " + houseId));

        if (!house.getLandlord().getEmail().equals(landlordEmail)) {
            throw new ForbiddenException("You can only view bookings for your own houses");
        }

        List<BookingResponse> bookings = bookingRepository.findByHouseId(houseId)
                .stream()
                .map(BookingResponse::fromBooking)
                .collect(Collectors.toList());

        return new ApiResponse(200, "Bookings retrieved successfully", bookings);
    }

    // landlord sees ALL bookings across all their houses
    public ApiResponse getAllMyHouseBookings(String landlordEmail) {
        User landlord = userRepository.findByEmail(landlordEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (landlord.getRole() != User.Role.LANDLORD) {
            throw new ForbiddenException("Only landlords can access this");
        }

        List<BookingResponse> bookings = bookingRepository.findByHouseLandlordId(landlord.getId())
                .stream()
                .map(BookingResponse::fromBooking)
                .collect(Collectors.toList());

        return new ApiResponse(200, "All bookings retrieved successfully", bookings);
    }

    // landlord confirms or cancels a booking
    public ApiResponse updateBookingStatus(Long bookingId, String status, String landlordEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        House house = booking.getHouse();

        if (!house.getLandlord().getEmail().equals(landlordEmail)) {
            throw new ForbiddenException("You can only manage bookings for your own houses");
        }

        try {
            Booking.Status newStatus = Booking.Status.valueOf(status.toUpperCase());

            if (newStatus == Booking.Status.CONFIRMED) {
                booking.setStatus(Booking.Status.CONFIRMED);
                house.setAvailable(false);
                houseRepository.save(house);
            } else if (newStatus == Booking.Status.CANCELLED) {
                booking.setStatus(Booking.Status.CANCELLED);
                house.setAvailable(true);
                houseRepository.save(house);
            } else {
                throw new BadRequestException("Status must be CONFIRMED or CANCELLED");
            }

        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status. Must be CONFIRMED or CANCELLED");
        }

        bookingRepository.save(booking);

        return new ApiResponse(200, "Booking status updated successfully", BookingResponse.fromBooking(booking));
    }

    // tenant cancels their own booking
    public ApiResponse cancelMyBooking(Long bookingId, String tenantEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (!booking.getTenant().getEmail().equals(tenantEmail)) {
            throw new ForbiddenException("You can only cancel your own bookings");
        }

        if (booking.getStatus() == Booking.Status.CONFIRMED) {
            throw new BadRequestException("Cannot cancel a confirmed booking. Contact the landlord.");
        }

        if (booking.getStatus() == Booking.Status.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        booking.setStatus(Booking.Status.CANCELLED);
        bookingRepository.save(booking);

        return new ApiResponse(200, "Booking cancelled successfully", BookingResponse.fromBooking(booking));
    }
}
