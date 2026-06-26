// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.repositories;

import com.housereting.houserenting.models.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    // get all houses owned by a specific landlord
    List<House> findByLandlordId(Long landlordId);

    // get all approved and available houses with pagination (for tenants)
    Page<House> findByStatusAndAvailable(House.Status status, Boolean available, Pageable pageable);

    // get all approved and available houses without pagination
    List<House> findByStatusAndAvailable(House.Status status, Boolean available);

    // admin gets all houses by status
    List<House> findByStatus(House.Status status);

    // search by location - only approved and available
    List<House> findByLocationContainingIgnoreCaseAndStatusAndAvailable(
            String location, House.Status status, Boolean available);

    // search by price range - only approved and available
    List<House> findByPriceBetweenAndStatusAndAvailable(
            Double minPrice, Double maxPrice, House.Status status, Boolean available);

    // search by price range AND location - only approved and available
    @Query("SELECT h FROM House h WHERE h.price BETWEEN :minPrice AND :maxPrice " +
           "AND LOWER(h.location) LIKE LOWER(CONCAT('%', :location, '%')) " +
           "AND h.status = :status AND h.available = :available")
    List<House> findByPriceBetweenAndLocationAndStatusAndAvailable(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("location") String location,
            @Param("status") House.Status status,
            @Param("available") Boolean available
    );
}
