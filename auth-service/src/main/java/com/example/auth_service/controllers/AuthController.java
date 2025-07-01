package com.example.auth_service.controllers;

import com.example.auth_service.dto.*;
import com.example.auth_service.services.serviceInterfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    protected ResponseEntity<ApiResponseDto<AuthResponseDto>> authenticate(@RequestBody AuthRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(authService.authenticate(requestDto),HttpStatus.OK.value()));
    }

    @PostMapping("/register")
    protected ResponseEntity<ApiResponseDto<RegisterResponseDto>> registerUser(@RequestBody RegisterRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(authService.registerUser(requestDto),HttpStatus.CREATED.value()));
    }

}
