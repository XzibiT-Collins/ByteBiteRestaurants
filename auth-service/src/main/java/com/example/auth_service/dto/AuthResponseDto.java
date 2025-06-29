package com.example.auth_service.dto;

import lombok.Builder;

@Builder
public record AuthResponseDto(
    String email,
    String token
){}
