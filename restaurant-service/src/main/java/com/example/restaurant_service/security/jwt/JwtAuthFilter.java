package com.example.restaurant_service.security.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final Logger logger = Logger.getLogger(JwtAuthFilter.class.getName());

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("AUTHORIZATION");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        try{
            //create authObj with claims
            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);
            logger.info("Roles: "+ role);


            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.singleton(new SimpleGrantedAuthority(role)));

            SecurityContextHolder.getContext().setAuthentication(auth);
        }catch (JwtException e){
            logger.warning("Invalid Token: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
        }catch (Exception e){
            logger.warning("Internal Server Error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
        filterChain.doFilter(request, response);
    }
}
