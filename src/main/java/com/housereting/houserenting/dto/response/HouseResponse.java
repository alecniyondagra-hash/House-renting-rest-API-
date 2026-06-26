// Author: Niyondagara Alec David - 22773/2023
// UNILAK - Advanced Programming Final Project 2025/2026

package com.housereting.houserenting.dto.response;

import com.housereting.houserenting.models.House;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HouseResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Double price;
    private Integer rooms;
    private Boolean available;
    private String imageUrl;
    private String status;
    private Long landlordId;
    private String landlordName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // converts a House entity into a HouseResponse DTO
    public static HouseResponse fromHouse(House house) {
        HouseResponse response = new HouseResponse();
        response.setId(house.getId());
        response.setTitle(house.getTitle());
        response.setDescription(house.getDescription());
        response.setLocation(house.getLocation());
        response.setPrice(house.getPrice());
        response.setRooms(house.getRooms());
        response.setAvailable(house.getAvailable());
        response.setImageUrl(house.getImageUrl());
        response.setStatus(house.getStatus().name());
        response.setLandlordId(house.getLandlord().getId());
        response.setLandlordName(house.getLandlord().getFirstName() + " " + house.getLandlord().getLastName());
        response.setCreatedAt(house.getCreatedAt());
        response.setUpdatedAt(house.getUpdatedAt());
        return response;
    }
}
