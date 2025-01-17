package com.melascan.util;

import com.melascan.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "";
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    // Secure signing key
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * Generate a JWT token for a given user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated JWT token.
     */
    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate and parse the JWT token to retrieve claims.
     *
     * @param token The JWT token to validate.
     * @return The subject (email in this case) from the token, or null if invalid.
     */
    public static String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // Email'i döndür
        } catch (ExpiredJwtException ex) {
            System.out.println("Token has expired: " + ex.getMessage());
            return null;
        } catch (JwtException ex) {
            System.out.println("Invalid JWT: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Check if a token is expired.
     *
     * @param token The JWT token to check.
     * @return True if expired, false otherwise.
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        } catch (JwtException ex) {
            return true;
        }
    }
}
