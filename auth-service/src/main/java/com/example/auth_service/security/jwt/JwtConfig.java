package com.example.auth_service.security.jwt;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
//@ConfigurationProperties(prefix = "jwt")
//@Setter
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;
    private SecretKey secretKeyEncoded;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKeyEncoded = Keys.hmacShaKeyFor(keyBytes);
    }

    public SecretKey getSecreteKey() {
        return this.secretKeyEncoded;
    }
}
