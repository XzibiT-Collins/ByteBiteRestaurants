package com.example.restaurant_service.dto;

import lombok.Builder;

@Builder
public record UserResponseDto(
        long id,
        String email,
        String username,
        String role
) {
}