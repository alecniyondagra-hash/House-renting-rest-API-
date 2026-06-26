// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.repositories;

import com.housereting.houserenting.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // tenant sees their own bookings
    List<Booking> findByTenantId(Long tenantId);

    // landlord sees bookings for a specific house
    List<Booking> findByHouseId(Long houseId);

    // landlord sees ALL bookings across all their houses
    List<Booking> findByHouseLandlordId(Long landlordId);

    // check if a tenant already booked a specific house
    boolean existsByHouseIdAndTenantId(Long houseId, Long tenantId);
}
