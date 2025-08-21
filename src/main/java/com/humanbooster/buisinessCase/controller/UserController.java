package com.humanbooster.buisinessCase.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humanbooster.buisinessCase.dto.UserDTO;
import com.humanbooster.buisinessCase.dto.UserRegisterDTO;
import com.humanbooster.buisinessCase.mapper.UserMapper;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.service.RefreshTokenService;
import com.humanbooster.buisinessCase.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing User entities.
 * Provides endpoints for creating, retrieving, updating, and deleting users    .
 */
@RestController
@RestControllerAdvice
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final UserMapper mapper;


    /**
     * Get all users.
     * GET /api/users
     * @return ResponseEntity with the list of users
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> userDTOs = userService.getAllUsers().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    /**
     * Get a user by ID.
     * GET /api/users/{id}
     * @param id The ID of the user to retrieve
     * @return ResponseEntity with the user if found, or 404 Not Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return userService.getUserById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get a user by Token.
     * GET /api/users/me
     * @param token The token of the user to retrieve
     * @return ResponseEntity with the user if found, or 404 Not Found if not found
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserByToken(@AuthenticationPrincipal UserDetails userDetails){
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    
        return userService.getUserByUsername(userDetails.getUsername())
            .map(mapper::toDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new user.
     * POST /api/users
     * @param user The user entity to be saved
     * @return ResponseEntity with the saved user and 201 Created status
     */
    @PostMapping
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO){
        User newUser = mapper.toEntity(userRegisterDTO);
        User savedUser = userService.saveUser(newUser);
        UserDTO savedUserDTO = mapper.toDTO(savedUser);
        // Conform RESTful practices, we should return a URI to the created resource.
        // URI location = URI.create("/api/users/" + savedUser.getId());
        // return ResponseEntity.created(location).body(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDTO);
    }

    /**
     * Delete a user by ID.
     * DELETE /api/users/{id}
     * @param id The ID of the user to delete
     * @return ResponseEntity with the 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
        return userService.deleteUserById(id).isPresent() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Update a user by ID.
     * PUT /api/users/{id}
     * @param id The ID of the user to update
     * @param newUser The updated user entity
     * @return ResponseEntity with the updated user if found, or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO newUserDTO){
        return userService.updateUser(id, mapper.toEntity(newUserDTO))
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

