package com.humanbooster.buisinessCase.service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.buisinessCase.model.JwtRefresh;
import com.humanbooster.buisinessCase.repository.JwtRefreshRepository;
import com.humanbooster.buisinessCase.repository.UserRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service class for managing Jwt Refresh Token.
 * Provides methods to save, retrieve, update, and delete Jwt Refresh Token.
 */

@Service
@Transactional
public class JwtRefreshService{
    private final JwtRefreshRepository jwtRefreshRepository;
    private final UserRepository userRepository;
    private final String SECRET_KEY;
    private final Long EXPIRATION_TIME;

    public JwtRefreshService(
            JwtRefreshRepository jwtRefreshRepository,
            UserRepository userRepository,
            @Value("${jwtrefresh.secret}") String secretKey,
            @Value("${jwtrefresh.expiration}") Long expirationTime) {
        this.jwtRefreshRepository = jwtRefreshRepository;
        this.userRepository = userRepository;
        this.SECRET_KEY = secretKey;
        this.EXPIRATION_TIME = expirationTime;
    }

    public String generateToken(String username) {
        if (username == null || username.isEmpty()) throw new IllegalArgumentException("Username must not be null or empty");

        /* Generate a new jwt Refresh token
         *
         * Find User by username
         * Assign it
         */
        Date now = new Date(System.currentTimeMillis());
        Date expireAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireAt)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        JwtRefresh newJwtRefresh = new JwtRefresh();
        newJwtRefresh.setRefreshToken(token);
        newJwtRefresh.setIssuedAt(now);
        newJwtRefresh.setUserId(userRepository.findByUsername(username).get().getId());
        jwtRefreshRepository.save(newJwtRefresh);
        return token;
    }

    private Key getSigningKey() {
        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) throw new IllegalArgumentException("JWT secret key must not be null or empty");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

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

    /* Check jwt validity
    *
    * Check if User has a jwt
    * Check jwt expiration date
    * If expired generate a new jwt
    * If not expired return the existing jwt
    */


    /**
     * Saves a new Jwt Refresh Token.
     * @param jwtRefresh the Jwt Refresh Token to save
     * @return the newly saved Jwt Refresh Token
     */
    @Transactional
    public JwtRefresh saveJwtRefresh(JwtRefresh jwtRefresh){
        return jwtRefreshRepository.save(jwtRefresh);
    }

    /**
     * Retrieves all Jwt Refresh Tokens.
     * @return a list of all Jwt Refresh Tokens
     */
    @Transactional(readOnly = true)
    public List<JwtRefresh> getAllJwtRefreshes(){
        return jwtRefreshRepository.findAll();
    }

    /**
     * Retrieves a Jwt Refresh Token by its ID.
     * @param id the ID of the Jwt Refresh Token to retrieve
     * @return  an Optional containing the Jwt Refresh Token if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<JwtRefresh> getJwtRefreshById(Long id){
        return jwtRefreshRepository.findById(id);
    }

    /**
     * Deletes a Jwt Refresh Token by its ID.
     * @param id the ID of the Jwt Refresh Token to delete
     * @return an Optional containing the deleted Jwt Refresh Token if found, or empty if not found
     */
    @Transactional
    public Optional<JwtRefresh> deleteJwtRefreshById(Long id){
        Optional<JwtRefresh> jwtRefreshOpt = jwtRefreshRepository.findById(id);
        jwtRefreshOpt.ifPresent(jwtRefreshRepository::delete);
        return jwtRefreshOpt;
    }

    /**
     * Updates an existing Jwt Refresh Token.
     * @param id the ID of the Jwt Refresh Token to update
     * @param newJwtRefresh the new Jwt Refresh Token data to update
     * @return an Optional containing the updated Jwt Refresh Token if found, or empty if not found
     */
    @Transactional
    public Optional<JwtRefresh> updateJwtRefresh(Long id, JwtRefresh newJwtRefresh){
        return jwtRefreshRepository.findById(id)
                .map(existingJwtRefresh -> {
                    ModelUtil.copyFields(newJwtRefresh, existingJwtRefresh);
                    return jwtRefreshRepository.save(existingJwtRefresh);
                });
    }

    /**
     * Checks if a Jwt Refresh Token exists by its ID.
     * @param id the ID of the Jwt Refresh Token to check
     * @return true if the Jwt Refresh Token exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return jwtRefreshRepository.existsById(id);
    }



}
