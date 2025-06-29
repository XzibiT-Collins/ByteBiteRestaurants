package com.example.auth_service.globalExceptionHandler.authExceptionsHandler;

import com.example.auth_service.dto.ApiResponseDto;
import com.example.auth_service.globalExceptionHandler.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorResponse errorResponse = ErrorResponse
               .builder()
               .message("Unauthorized Access Authentication Required: " + authException.getMessage())
               .status(HttpServletResponse.SC_UNAUTHORIZED)
               .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), ApiResponseDto.error(errorResponse,null));
    }
}
