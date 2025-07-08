package com.example.auth_service.services.servicesImplementations;

import com.example.auth_service.dto.ApiResponseDto;
import com.example.auth_service.dto.UserResponseDto;
import com.example.auth_service.mappers.UserMapper;
import com.example.auth_service.models.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.services.serviceInterfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));
        log.error("User found: {}" , user.getEmail());
        return ResponseEntity.ok(ApiResponseDto.success(UserMapper.toUserResponseDto(user), HttpStatus.OK.value()));
    }
}
