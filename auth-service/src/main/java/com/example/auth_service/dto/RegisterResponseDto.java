package com.example.auth_service.dto;

import lombok.Builder;

@Builder
public record RegisterResponseDto(
        String email,
        String role
) {
}
