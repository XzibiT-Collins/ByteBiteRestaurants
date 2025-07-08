package com.example.restaurant_service.feignClient;

import com.example.restaurant_service.feignClient.clientAuthentication.AuthTokenService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor(AuthTokenService authTokenService) {
        return requestTemplate -> {
            String token = authTokenService.getToken();
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}
