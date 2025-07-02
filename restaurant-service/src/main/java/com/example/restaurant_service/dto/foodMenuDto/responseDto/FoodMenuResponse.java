package com.example.restaurant_service.dto.foodMenuDto.responseDto;

import lombok.Builder;

@Builder
public record FoodMenuResponse(
        long id,
        String name,
        String description,
        Double price
) {
}
