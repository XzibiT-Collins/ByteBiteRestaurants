package com.example.order_service.globalExceptionHandler;

import lombok.Builder;

@Builder
public record ErrorResponse(
    String message,
    int status
){}
