package com.example.restaurant_service.dto;

import jakarta.validation.constraints.NotNull;


public record AuthRequestDto(
        @NotNull
        String email,
        @NotNull
        String password
) {
}
