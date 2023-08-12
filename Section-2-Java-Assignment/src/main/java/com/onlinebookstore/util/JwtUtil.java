package com.onlinebookstore.util;

import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
@Component
public class JwtUtil {

    public String generateToken(Authentication auth) {
        // Consider moving this to a properties file
        String secretKey = "QWOUIOAIZUSAJSLKAHWKQIOJKLZJSPAIOJSLKAJSOUQIOWJSLKAJS";
        // 1h
        long validityInMilliseconds = 3600000;
        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("roles", auth.getAuthorities())  // storing user roles in the token
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // ... other utility methods for JWT like validateToken, extractUsername, etc.
}
