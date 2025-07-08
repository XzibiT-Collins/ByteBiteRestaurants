package com.example.restaurant_service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final String secret="OJLJzlvWjstALHdGyoW7mBdlmqTX71ow0gK8aPzeX28=";
    private SecretKey secretKeyEncoded;

    @PostConstruct
    public void init() {
        //Encode key
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKeyEncoded = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(java.lang.String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role").toString();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims;

        claims = Jwts.parser()
                .verifyWith(secretKeyEncoded)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims;
    }

    public long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return Long.parseLong(claims.get("userId").toString());
    }

    public boolean isTokenExpired(String token){
        Claims claims = extractAllClaims(token);
        return (claims.getExpiration().before(Date.from(Instant.now())));
    }
}
