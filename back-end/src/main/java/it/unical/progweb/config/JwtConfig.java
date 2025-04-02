package it.unical.progweb.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
public class JwtConfig {
    @Bean
    public Key secretKey() {
        return Keys.hmacShaKeyFor("12345678901234567890123456789012".getBytes());
    }
}
