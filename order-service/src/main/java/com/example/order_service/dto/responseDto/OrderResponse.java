package com.example.order_service.dto.responseDto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record OrderResponse(
        long id,
        long menuItemId,
        long customerId,
        int quantity,
        Double totalPrice,
        String status,
        Instant orderTime
) {
}
