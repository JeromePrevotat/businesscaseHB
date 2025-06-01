package com.humanbooster.buisinessCase.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * REST Controller for managing User entities.
 * Provides endpoints for creating, retrieving, updating, and deleting users.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Retrieves all users.
     * GET /api/users
     * @return ResponseEntity containing a list of all users.
     */ 
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieves a user by ID.
     * GET /api/users/{id}
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the user if found, or a 404 Not Found status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id){
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Saves a new user.
     * POST /api/users
     * @param user The user entity to be saved.
     * @return ResponseEntity containing the saved user with a 201 Created status.
     */
    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user){
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    /**
     * Deletes a user by ID.
     * DELETE /api/users/{id}
     * @param id The ID of the user to delete.
     * @return ResponseEntity with 204 No Content status if deleted, or 404 Not Found status if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long id){
        return userService.deleteUserById(id) ?
                    ResponseEntity.noContent().build() :
                    ResponseEntity.notFound().build();
    }

    /**
     * Updates an existing user.
     * PUT /api/users/{id}
     * @param id The ID of the user to update.
     * @param newUser The updated user entity.
     * @return ResponseEntity containing the updated user if found, or a 404 Not Found status if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User newUser){
        return userService.updateUser(id, newUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
