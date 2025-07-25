package com.example.api_gateway.jwtAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
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

    public String extractUserName(String token) {

        return extractClaim(token, Claims::getSubject);
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

    public boolean validateToken(String token) {
        final String userName = extractUserName(token);
        return (userName!=null && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
