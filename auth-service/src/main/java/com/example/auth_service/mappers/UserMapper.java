package com.example.auth_service.mappers;

import com.example.auth_service.dto.AuthResponseDto;
import com.example.auth_service.dto.RegisterRequestDto;
import com.example.auth_service.dto.RegisterResponseDto;
import com.example.auth_service.dto.UserResponseDto;
import com.example.auth_service.models.User;

public class UserMapper {
    public static User toUser(RegisterRequestDto request){
        return User
                .builder()
                .email(request.email())
                .username(request.username())
                .password(request.password())
                .role(String.valueOf(request.role()))
                .build();
    }

    public static RegisterResponseDto toRegisterResponseDto(User user){
        return RegisterResponseDto
                .builder()
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public static UserResponseDto toUserResponseDto(User user){
        return UserResponseDto
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
