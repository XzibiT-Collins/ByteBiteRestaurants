package com.example.restaurant_service.feignClient;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.dto.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(long id) {
                log.info("Fallback was triggered when fetching user details due to: {}", cause.getMessage());
                return ResponseEntity.ok(ApiResponseDto.success(UserResponseDto
                        .builder()
                        .id(0)
                        .email("abc@gmail.com")
                        .role("NO_ROLE")
                        .username("no user")
                        .build(),HttpStatus.OK.value()));
            }
        };
    }
}
