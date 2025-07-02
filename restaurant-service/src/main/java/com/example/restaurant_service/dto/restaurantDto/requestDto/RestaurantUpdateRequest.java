package com.example.restaurant_service.dto.restaurantDto.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RestaurantUpdateRequest(
        String name,
        String location,
        String phoneNumber,
        String email
) {
}
