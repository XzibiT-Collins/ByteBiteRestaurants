package com.example.auth_service.security.oauth2;

import com.example.auth_service.dto.ApiResponseDto;
import com.example.auth_service.dto.AuthResponseDto;
import com.example.auth_service.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOauth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;

    public CustomOauth2SuccessHandler(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //get user from the authentication object
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        String email = oidcUser.getEmail();

        //generate jwt Token
        String token = jwtService.generateToken(email);

        //redirect user to the token page
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        AuthResponseDto responseDto = AuthResponseDto
                .builder()
                .email(email)
                .token(token)
                .build();
        new ObjectMapper().writeValue(response.getWriter(), ApiResponseDto.success(responseDto,HttpServletResponse.SC_OK));
    }
}
