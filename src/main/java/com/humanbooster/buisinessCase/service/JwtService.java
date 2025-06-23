package com.humanbooster.buisinessCase.service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET_KEY;
    private final Long EXPIRATION_TIME;

    public JwtService(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") Long expirationTime){
        SECRET_KEY = secretKey;
        EXPIRATION_TIME = expirationTime;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                // Encode 64 bit secret key
                .compact();
    }

    private Key getSigningKey() {
        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) throw new IllegalArgumentException("JWT secret key must not be null or empty");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // OTHER METHODS LIKE VALIDATION, PARSING, ETC. CAN BE ADDED HERE

    // Claims = username, issuedAt, expiration, etc.
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                // INTEGRITY CHECK
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // "Protype" methode to wich we give another method to extract specific claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract the username from the JWT token.
     * This method uses the Generic method extractClaim() to get the subject claim
     * @param token JWT token from which to extract the username
     * @return the username extracted from the token via the Generic method extractClaim()
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
