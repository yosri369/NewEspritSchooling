package com.example.user.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Base64-encoded secret key (keep this safe & ideally externalize it)
    private static final String SECRET = "dGhpc19pc19teV9zdXBlcl9zZWN1cmVfNjRfYnl0ZV9rZXlfZm9yX2p3dA==";

    // Access token validity duration in milliseconds (currently 1 minute)
    private static final long ACCESS_TOKEN_EXP_MS = 60 * 1000; // 1 minute

    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

    // Generate JWT Access Token with username and role claims
    public String generateAccessToken(String username, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ACCESS_TOKEN_EXP_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate JWT token signature and expiration
    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // includes ExpiredJwtException, MalformedJwtException, etc.
            return false;
        }
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract username (subject) from the token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract role claim from the token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // Check if the token is expired
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
