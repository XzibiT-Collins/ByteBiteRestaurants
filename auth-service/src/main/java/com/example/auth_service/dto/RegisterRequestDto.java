package com.example.auth_service.dto;

import com.example.auth_service.utils.RoleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
    @Email(message = "Please provide a valid email")
    @NotBlank(message = "email cannot be blank")
    String email,

    @NotBlank(message = "username cannot be blank")
    String username,

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, message = "password must be greater than 8 characters")
    @Size(max = 50, message = "password must be less than 50 characters")
    String password,

    @NotBlank(message = "role cannot be blank")
    @Enumerated(EnumType.STRING)
    RoleEnum role
){}
