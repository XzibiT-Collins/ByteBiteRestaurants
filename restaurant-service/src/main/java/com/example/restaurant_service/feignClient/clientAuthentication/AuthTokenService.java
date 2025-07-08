package com.example.restaurant_service.feignClient.clientAuthentication;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.dto.AuthRequestDto;
import com.example.restaurant_service.dto.AuthResponseDto;
import com.example.restaurant_service.security.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class AuthTokenService {
    private final WebClient webClient;
    private final JwtService jwtService;
    private String email = "admin@gmail.com";
    private String password = "collinsxzibit1";
    private String token;

    public AuthTokenService(WebClient.Builder webClientBuilder,JwtService jwtService) {
        this.webClient = webClientBuilder.baseUrl("http://auth-service").build();
        this.jwtService = jwtService;
    }

    public String getToken() {
        log.info("Fetching Token");
        if(token != null && !jwtService.isTokenExpired(token)){
            return token;
        }
        log.info("Token is expired. Fetching new token.");
        this.token = fetchNewToken();
        log.info("Token fetched: {}",token);
        return token;
    }

    public String fetchNewToken(){
        log.info("Fetching new token from auth-service");
        AuthRequestDto authRequestDto = new AuthRequestDto(email,password);

        ApiResponseDto<AuthResponseDto> response = webClient.post()
                .uri("api/v1/auth/login")
                .bodyValue(authRequestDto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference <ApiResponseDto<AuthResponseDto>> () {})
                .block();

        if(response.getData() != null && response.getData().token() != null){
            this.token = response.getData().token();
            log.info("Token fetched: {}",token);
        }
        return token;
    }
}
