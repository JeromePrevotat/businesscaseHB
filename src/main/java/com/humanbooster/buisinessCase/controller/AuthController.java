package com.humanbooster.buisinessCase.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humanbooster.buisinessCase.service.JwtService;

import lombok.AllArgsConstructor;



@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> authenticate(@RequestBody AuthRequest authRequest) {
        // Authenticate the user with Spring Manager
        // CAN THROW BADCREDENTIALSEXCEPTION IF AUTHENTICATION FAILS
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        // If authentication is successful, generate a JWT token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwtToken = jwtService.generateToken(userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(new TokenDTO(jwtToken));
    }
}

// DTO for authentication request
record AuthRequest(String username, String password) {

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

record TokenDTO (String token) {

    public String getToken() {
        return token;
    }
    
}