package com.example.restaurant_service.dto.foodMenuDto.requestDto;

public record FoodMenuUpdateRequest(
        String name,
        String description,
        Double price
) {
}
