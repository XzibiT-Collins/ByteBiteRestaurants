package com.example.restaurant_service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
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

    public String generateToken(User userDetails) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",1L);
        claims.put("role",userDetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElseThrow(()->new IllegalStateException("Invalid Role")));

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .and()
                .signWith(secretKeyEncoded)
                .compact();
    }
}
