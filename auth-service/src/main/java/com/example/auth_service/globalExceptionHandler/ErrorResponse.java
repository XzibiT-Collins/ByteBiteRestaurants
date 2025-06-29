package com.example.auth_service.globalExceptionHandler;

import lombok.Builder;

import java.util.Date;

@Builder
public record ErrorResponse(
    String message,
    int status
){}
