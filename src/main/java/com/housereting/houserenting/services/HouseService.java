// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.services;

import com.housereting.houserenting.dto.request.CreateHouseRequest;
import com.housereting.houserenting.dto.request.UpdateHouseRequest;
import com.housereting.houserenting.dto.response.ApiResponse;
import com.housereting.houserenting.dto.response.HouseResponse;
import com.housereting.houserenting.exception.BadRequestException;
import com.housereting.houserenting.exception.ForbiddenException;
import com.housereting.houserenting.exception.ResourceNotFoundException;
import com.housereting.houserenting.models.House;
import com.housereting.houserenting.models.User;
import com.housereting.houserenting.repositories.HouseRepository;
import com.housereting.houserenting.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HouseService {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    // landlord creates a new house listing
    public ApiResponse createHouse(CreateHouseRequest request, String landlordEmail) {
        User landlord = userRepository.findByEmail(landlordEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (landlord.getRole() != User.Role.LANDLORD) {
            throw new ForbiddenException("Only landlords can create house listings");
        }

        House house = new House();
        house.setTitle(request.getTitle());
        house.setDescription(request.getDescription());
        house.setLocation(request.getLocation());
        house.setPrice(request.getPrice());
        house.setRooms(request.getRooms());
        house.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        house.setImageUrl(request.getImageUrl());
        house.setLandlord(landlord);
        house.setStatus(House.Status.PENDING);

        houseRepository.save(house);

        return new ApiResponse(201, "House created successfully. Waiting for admin approval.",
                HouseResponse.fromHouse(house));
    }

    // get all APPROVED and AVAILABLE houses with pagination (for tenants)
    public ApiResponse getAllHouses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<House> housePage = houseRepository.findByStatusAndAvailable(
                House.Status.APPROVED, true, pageable);

        List<HouseResponse> houses = housePage.getContent()
                .stream()
                .map(HouseResponse::fromHouse)
                .collect(Collectors.toList());

        return new ApiResponse(200, "Houses retrieved successfully - Page " + (page + 1) +
                " of " + housePage.getTotalPages(), houses);
    }

    // landlord sees all their own houses
    public ApiResponse getMyHouses(String landlordEmail) {
        User landlord = userRepository.findByEmail(landlordEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<HouseResponse> houses = houseRepository.findByLandlordId(landlord.getId())
                .stream()
                .map(HouseResponse::fromHouse)
                .collect(Collectors.toList());

        return new ApiResponse(200, "Your houses retrieved successfully", houses);
    }

    // get a single house by id
    public ApiResponse getHouseById(Long id) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found with id: " + id));

        return new ApiResponse(200, "House retrieved successfully", HouseResponse.fromHouse(house));
    }

    // landlord updates their own house
    public ApiResponse updateHouse(Long id, UpdateHouseRequest request, String landlordEmail) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found with id: " + id));

        if (!house.getLandlord().getEmail().equals(landlordEmail)) {
            throw new ForbiddenException("You can only update your own houses");
        }

        if (request.getTitle() != null) house.setTitle(request.getTitle());
        if (request.getDescription() != null) house.setDescription(request.getDescription());
        if (request.getLocation() != null) house.setLocation(request.getLocation());
        if (request.getPrice() != null) house.setPrice(request.getPrice());
        if (request.getRooms() != null) house.setRooms(request.getRooms());
        if (request.getAvailable() != null) house.setAvailable(request.getAvailable());
        if (request.getImageUrl() != null) house.setImageUrl(request.getImageUrl());

        houseRepository.save(house);

        return new ApiResponse(200, "House updated successfully", HouseResponse.fromHouse(house));
    }

    // landlord deletes their own house
    public ApiResponse deleteHouse(Long id, String landlordEmail) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found with id: " + id));

        if (!house.getLandlord().getEmail().equals(landlordEmail)) {
            throw new ForbiddenException("You can only delete your own houses");
        }

        if (!house.getAvailable()) {
            throw new BadRequestException("Cannot delete a house that is currently rented");
        }

        houseRepository.delete(house);

        return new ApiResponse(200, "House deleted successfully", null);
    }

    // search houses - only APPROVED and AVAILABLE
    public ApiResponse searchHouses(Double minPrice, Double maxPrice, String location) {
        List<House> houses;

        if (minPrice != null && maxPrice != null && location != null) {
            houses = houseRepository.findByPriceBetweenAndLocationAndStatusAndAvailable(
                    minPrice, maxPrice, location, House.Status.APPROVED, true);
        } else if (minPrice != null && maxPrice != null) {
            houses = houseRepository.findByPriceBetweenAndStatusAndAvailable(
                    minPrice, maxPrice, House.Status.APPROVED, true);
        } else if (location != null) {
            houses = houseRepository.findByLocationContainingIgnoreCaseAndStatusAndAvailable(
                    location, House.Status.APPROVED, true);
        } else {
            houses = houseRepository.findByStatusAndAvailable(House.Status.APPROVED, true);
        }

        List<HouseResponse> response = houses.stream()
                .map(HouseResponse::fromHouse)
                .collect(Collectors.toList());

        return new ApiResponse(200, "Houses retrieved successfully", response);
    }
}
