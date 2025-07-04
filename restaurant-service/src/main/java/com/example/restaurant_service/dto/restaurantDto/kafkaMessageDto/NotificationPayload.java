package com.example.restaurant_service.dto.restaurantDto.kafkaMessageDto;

public record NotificationPayload(
        String email,
        String message
) {
}
