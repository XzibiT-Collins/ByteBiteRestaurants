package com.example.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthRequestDto(
    @Email(message = "Please provide a valid email")
    @NotNull(message = "email cannot be blank")
    String email,

    @Size(max = 50, message = "password must be less than 50 characters")
    @Size(min = 8, message = "password must be greater than 8 characters")
    String password
){}
