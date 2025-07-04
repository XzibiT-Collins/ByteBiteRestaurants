package com.example.restaurant_service.dto.restaurantDto.kafkaMessageDto;

import lombok.Builder;

@Builder
public record OrderRequest(
        long menuItemId,
        long restaurantId,
        long customerId,
        int quantity,
        Double totalPrice
) {
}