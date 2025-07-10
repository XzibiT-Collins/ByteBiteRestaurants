package com.example.restaurant_service.dto.foodMenuDto.requestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FoodMenuRequest(
        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        String name,
        String description,
        @NotBlank(message = "Price cannot be blank")
        @Min(value = 0, message = "Price cannot be negative")
        Double price
) {
}
