package com.example.restaurant_service.globalExceptionHandler;

import lombok.Builder;

@Builder
public record ErrorResponse(
    String message,
    int status
){}
