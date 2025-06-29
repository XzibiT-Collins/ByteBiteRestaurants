package com.example.auth_service.services.serviceInterfaces;

import com.example.auth_service.dto.AuthRequestDto;
import com.example.auth_service.dto.AuthResponseDto;
import com.example.auth_service.dto.RegisterRequestDto;
import com.example.auth_service.dto.RegisterResponseDto;

public interface AuthService {
    RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto);
    AuthResponseDto authenticate(AuthRequestDto requestDto);
}
