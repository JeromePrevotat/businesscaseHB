package com.humanbooster.buisinessCase.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humanbooster.buisinessCase.dto.JwtRefreshDTO;
import com.humanbooster.buisinessCase.model.JwtRefresh;
import com.humanbooster.buisinessCase.security.AuthRequestDTO;
import com.humanbooster.buisinessCase.security.AuthResponseDTO;
import com.humanbooster.buisinessCase.security.JwtDTO;
import com.humanbooster.buisinessCase.service.JwtRefreshService;
import com.humanbooster.buisinessCase.service.JwtService;

import lombok.AllArgsConstructor;



@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final JwtRefreshService jwtRefreshService;
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
        final JwtRefresh refreshToken = jwtRefreshService.generateToken(authRequest.getUsername());
        JwtRefresh savedRefreshToken = jwtRefreshService.saveJwtRefresh(refreshToken);
        // Also generate a JWT Token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String accessToken = jwtService.generateToken(userDetails.getUsername());
        // Put Tokens in AuthResponseDTO
        AuthResponseDTO response = new AuthResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(savedRefreshToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).header("Authorization", "Bearer " + accessToken).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDTO> refreshToken(@RequestBody JwtRefreshDTO refreshToken){
        // GET THE TOKEN FROM DB NOT FROM BODY IDIOT
        String username = jwtRefreshService.extractUsername(refreshToken.getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // GET USER INFO FROM EXPIRED
        if (jwtRefreshService.isTokenValid(refreshToken.getRefreshToken(), userDetails)) {
            String newAccessToken = jwtService.generateToken(username);
            return ResponseEntity.status(HttpStatus.OK)
            .header("Authorization", "Bearer " + newAccessToken)
            .body(new JwtDTO(newAccessToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}