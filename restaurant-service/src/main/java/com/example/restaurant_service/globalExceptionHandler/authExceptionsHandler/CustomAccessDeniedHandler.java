package com.example.restaurant_service.globalExceptionHandler.authExceptionsHandler;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.globalExceptionHandler.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message("Access Denied: " + accessDeniedException.getMessage())
                .status(HttpServletResponse.SC_FORBIDDEN)
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), ApiResponseDto.error(errorResponse,null));
    }
}
