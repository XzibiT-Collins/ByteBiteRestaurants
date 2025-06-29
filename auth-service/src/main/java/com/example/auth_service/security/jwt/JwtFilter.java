package com.example.auth_service.security.jwt;

import com.example.auth_service.dto.ApiResponseDto;
import com.example.auth_service.globalExceptionHandler.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final Logger logger = Logger.getLogger(JwtFilter.class.getName());

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authenticationHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        try{
            if(authenticationHeader != null && authenticationHeader.startsWith("Bearer ")){
                token = authenticationHeader.substring(7);
                email = jwtService.extractEmail(token);
                logger.info("Email extracted from token: "+email);
            }
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if(jwtService.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        }catch(ExpiredJwtException e){
            sendErrorResponse(response,"Token Expired: "+e.getMessage(),HttpServletResponse.SC_BAD_REQUEST);
        }catch (JwtException | IllegalArgumentException e){
            sendErrorResponse(response,"Invalid Token: "+e.getMessage(),HttpServletResponse.SC_UNAUTHORIZED);
        }catch (Exception e){
            sendErrorResponse(response,"Authentication ghg Failed: "+e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(message)
                .status(statusCode)
                .build();

        new ObjectMapper().writeValue(response.getWriter(), ApiResponseDto.error(errorResponse,null));
    }
}
