package com.example.restaurant_service.feignClient;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service",configuration = FeignClientConfig.class, fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {
    @GetMapping("/api/v1/users/{id}")
    ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(@PathVariable("id") long id);
}
