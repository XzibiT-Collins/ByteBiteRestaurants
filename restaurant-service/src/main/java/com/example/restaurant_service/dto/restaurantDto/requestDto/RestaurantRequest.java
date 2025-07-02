package com.example.restaurant_service.dto.restaurantDto.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RestaurantRequest(
        @NotBlank(message = "Restaurant name can not be blank")
        String name,
        String location,
        @NotBlank(message = "Phone number can not be blank")
        String phoneNumber,
        @Email(message = "Please provide a valid email")
        String email
) {
}
