package com.humanbooster.buisinessCase.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humanbooster.buisinessCase.dto.RefreshTokenDTO;
import com.humanbooster.buisinessCase.model.RefreshToken;
import com.humanbooster.buisinessCase.security.AuthRequestDTO;
import com.humanbooster.buisinessCase.security.AuthResponseDTO;
import com.humanbooster.buisinessCase.security.JwtDTO;
import com.humanbooster.buisinessCase.service.RefreshTokenService;

import lombok.AllArgsConstructor;



@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody AuthRequestDTO authRequest) {
        // Authenticate the user with Spring Manager
        // CAN THROW BADCREDENTIALSEXCEPTION IF AUTHENTICATION FAILS
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (DisabledException | LockedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
        // If authentication is successful, generate a Refresh Token
        final RefreshToken refreshToken = refreshTokenService.generateToken(authRequest.getUsername());
        // Retrieves & Deletes the old token
        Long tokenOwnerId = refreshToken.getUser().getId();
        refreshTokenService.deleteRefreshTokenByUserId(tokenOwnerId);
        // Saves the newly generated Refresh Token
        RefreshToken savedRefreshToken = refreshTokenService.createOrUpdateRefreshToken(tokenOwnerId);
        // Also generate a JWT Token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String accessToken = refreshTokenService.generateToken(userDetails.getUsername()).getToken();
        // Put Tokens in AuthResponseDTO
        AuthResponseDTO response = new AuthResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(savedRefreshToken.getToken());

        return ResponseEntity.status(HttpStatus.OK).header("Authorization", "Bearer " + accessToken).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDTO> refreshToken(@RequestBody RefreshTokenDTO refreshToken){
        // GET THE TOKEN FROM DB NOT FROM BODY IDIOT
        String username = refreshTokenService.extractUsername(refreshToken.getToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // GET USER INFO FROM EXPIRED
        if (refreshTokenService.isTokenValid(refreshToken.getToken(), userDetails)) {
            String newAccessToken = refreshTokenService.generateToken(username).getToken();
            return ResponseEntity.status(HttpStatus.OK)
            .header("Authorization", "Bearer " + newAccessToken)
            .body(new JwtDTO(newAccessToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}