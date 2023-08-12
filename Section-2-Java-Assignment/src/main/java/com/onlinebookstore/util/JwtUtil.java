package com.onlinebookstore.util;

import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtil {
    private final String secretKey = "QWOUIOAIZUSAJSLKAHWKQIOJKLZJSPAIOJSLKAJSOUQIOWJSLKAJS";
    public String generateToken(Authentication auth) {
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

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            // Handle invalid JWT cases
            return false;
        }
    }

    // ... other utility methods for JWT like validateToken, extractUsername, etc.
}
