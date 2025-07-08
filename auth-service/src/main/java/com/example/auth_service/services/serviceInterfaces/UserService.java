package com.example.auth_service.services.serviceInterfaces;

import com.example.auth_service.dto.ApiResponseDto;
import com.example.auth_service.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(long id);
}
