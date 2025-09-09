package com.humanbooster.businesscase.controller;

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

import com.humanbooster.businesscase.dto.AccountConfirmationDTO;
import com.humanbooster.businesscase.dto.RefreshTokenDTO;
import com.humanbooster.businesscase.dto.UserDTO;
import com.humanbooster.businesscase.dto.UserRegisterDTO;
import com.humanbooster.businesscase.exception.InvalidTokenException;
import com.humanbooster.businesscase.exception.RefreshTokenExpiredException;
import com.humanbooster.businesscase.mapper.UserMapper;
import com.humanbooster.businesscase.model.RefreshToken;
import com.humanbooster.businesscase.model.User;
import com.humanbooster.businesscase.security.AuthRequestDTO;
import com.humanbooster.businesscase.security.AuthResponseDTO;
import com.humanbooster.businesscase.security.JwtDTO;
import com.humanbooster.businesscase.service.RefreshTokenService;
import com.humanbooster.businesscase.service.RegisterService;
import com.humanbooster.businesscase.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;



@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final RegisterService registerService;
    private final UserMapper userMapper;

    
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
        final String accessToken = refreshTokenService.generateAccessToken(userDetails.getUsername());
        // Put Tokens in AuthResponseDTO
        AuthResponseDTO response = new AuthResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(savedRefreshToken.getToken());

        return ResponseEntity.status(HttpStatus.OK).header("Authorization", "Bearer " + accessToken).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDTO> refreshToken(@RequestBody RefreshTokenDTO refreshToken){
        try{
            // GET THE TOKEN FROM DB NOT FROM BODY IDIOT
            String username = refreshTokenService.extractUsername(refreshToken.getToken());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // GET USER INFO FROM EXPIRED
            if (refreshTokenService.isTokenValid(refreshToken.getToken(), userDetails)) {
                String newAccessToken = refreshTokenService.generateAccessToken(username);
                return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", "Bearer " + newAccessToken)
                .body(new JwtDTO(newAccessToken));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException("Refresh Token has expired", e);
        } catch (JwtException e){
            throw new InvalidTokenException("Token is invalid or malformed", e);
        }
    }

    /**
     * Save a new user.
     * POST /api/users
     * @param user The user entity to be saved
     * @return ResponseEntity with the saved user and 201 Created status
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO){
        User newUser = userMapper.toEntity(userRegisterDTO);
        newUser.setValidationCode(registerService.generateConfirmationCode());
        User savedUser = userService.saveUser(newUser);
        UserDTO savedUserDTO = userMapper.toDTO(savedUser);
        registerService.sendConfirmationCode(savedUser);
        // Conform RESTful practices, we should return a URI to the created resource.
        // URI location = URI.create("/api/users/" + savedUser.getId());
        // return ResponseEntity.created(location).body(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDTO);
    }
    
    @PostMapping("/account-confirmation")
    public ResponseEntity<UserDTO> confirmAccount(@Valid @RequestBody AccountConfirmationDTO dto){
        Optional<User> userOpt = userService.getUserById(dto.getUser_id());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (registerService.validateCode(user, dto.getCode())) {
                UserDTO userDTO = userMapper.toDTO(user);
                return ResponseEntity.ok(userDTO);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}