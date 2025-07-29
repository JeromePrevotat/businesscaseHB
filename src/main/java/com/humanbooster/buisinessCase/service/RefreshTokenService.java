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

import com.humanbooster.buisinessCase.model.RefreshToken;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.repository.RefreshTokenRepository;
import com.humanbooster.buisinessCase.repository.UserRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service class for managing Refresh Tokens.
 * Provides methods to save, retrieve, update, and delete Refresh Tokens.
 */

@Service
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final String SECRET_KEY;
    private final Long EXPIRATION_TIME;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            @Value("${jwtrefresh.secret}") String secretKey,
            @Value("${jwtrefresh.expiration}") Long expirationTime) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.SECRET_KEY = secretKey;
        this.EXPIRATION_TIME = expirationTime;
    }

    @Transactional
    public RefreshToken generateToken(String username) {
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
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setToken(token);
        newRefreshToken.setIssuedAt(now);
        newRefreshToken.setUser(userRepository.findByUsername(username).get());
        return newRefreshToken;
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

    public Optional<User> getUserByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(RefreshToken::getUser)
                .or(() -> Optional.empty());
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
     * @param refreshToken the Jwt Refresh Token to save
     * @return the newly saved Jwt Refresh Token
     */
    @Transactional
    public RefreshToken saveRefreshToken(RefreshToken refreshToken){
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Retrieves all Jwt Refresh Tokens.
     * @return a list of all Jwt Refresh Tokens
     */
    @Transactional(readOnly = true)
    public List<RefreshToken> getAllRefreshTokenes(){
        return refreshTokenRepository.findAll();
    }

    /**
     * Retrieves a Jwt Refresh Token by its ID.
     * @param id the ID of the Jwt Refresh Token to retrieve
     * @return  an Optional containing the Jwt Refresh Token if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<RefreshToken> getRefreshTokenById(Long id){
        return refreshTokenRepository.findById(id);
    }

    /**
     * Deletes a Jwt Refresh Token by its ID.
     * @param id the ID of the Jwt Refresh Token to delete
     * @return an Optional containing the deleted Jwt Refresh Token if found, or empty if not found
     */
    @Transactional
    public Optional<RefreshToken> deleteRefreshTokenById(Long id){
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findById(id);
        refreshTokenOpt.ifPresent(refreshTokenRepository::delete);
        return refreshTokenOpt;
    }

    /**
     * Updates an existing Jwt Refresh Token.
     * @param id the ID of the Jwt Refresh Token to update
     * @param newRefreshToken the new Jwt Refresh Token data to update
     * @return an Optional containing the updated Jwt Refresh Token if found, or empty if not found
     */
    @Transactional
    public Optional<RefreshToken> updateRefreshToken(Long id, RefreshToken newRefreshToken){
        return refreshTokenRepository.findById(id)
                .map(existingRefreshToken -> {
                    ModelUtil.copyFields(newRefreshToken, existingRefreshToken);
                    return refreshTokenRepository.save(existingRefreshToken);
                });
    }

    /**
     * Checks if a Jwt Refresh Token exists by its ID.
     * @param id the ID of the Jwt Refresh Token to check
     * @return true if the Jwt Refresh Token exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return refreshTokenRepository.existsById(id);
    }
}
