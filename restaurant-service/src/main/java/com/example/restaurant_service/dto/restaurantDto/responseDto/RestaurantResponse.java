package com.example.restaurant_service.dto.restaurantDto.responseDto;

import lombok.Builder;

@Builder
public record RestaurantResponse(
        long id,
        String name,
        String location,
        String phoneNumber,
        String email,
        long owner
) {
}
