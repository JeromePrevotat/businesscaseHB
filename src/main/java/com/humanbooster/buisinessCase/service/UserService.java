package com.humanbooster.buisinessCase.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.buisinessCase.exception.UserValidationException;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.repository.UserRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing Users.
 * Provides methods to save, retrieve, update, and delete Users.
 */

@Service
@RequiredArgsConstructor
@Transactional
public class UserService{
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Saves a new User.
     * @param user the User to save
     * @return the newly saved User
     */
    @Transactional
    public User saveUser(User user){
        if (user != null){
            Map<String, String> errors = new HashMap<>();
            if (userRepository.existsByUsername(user.getUsername())) errors.put("username", "Username already exists");
            if (userRepository.existsByEmail(user.getEmail())) errors.put("email", "Email already exists");
            if (!errors.isEmpty()) throw new UserValidationException(errors);    
            if(user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return userRepository.save(user);
        }
        throw new IllegalArgumentException("User must not be null");
    }

    /**
     * Retrieves all Users.
     * @return a list of all Users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /**
     * Retrieves a User by its ID.
     * @param id the ID of the User to retrieve
     * @return  an Optional containing the User if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    /**
     * Retrieves a User by its Token.
     * @param token the Token of the User to retrieve
     * @return  an Optional containing the User if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByToken(String token){
        return refreshTokenService.getUserByToken(token);
    }
    
    /**
     * Retrieves a User by its Username.
     * @param username the Username of the User to retrieve
     * @return  an Optional containing the User if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    /**
     * Deletes a User by its ID.
     * @param id the ID of the User to delete
     * @return an Optional containing the deleted User if found, or empty if not found
     */
    @Transactional
    public Optional<User> deleteUserById(Long id){
        Optional<User> userOpt = userRepository.findById(id);
        userOpt.ifPresent(userRepository::delete);
        return userOpt;
    }

    /**
     * Updates an existing User.
     * @param id the ID of the User to update
     * @param newUser the new User data to update
     * @return an Optional containing the updated User if found, or empty if not found
     */
    @Transactional
    public Optional<User> updateUser(Long id, User newUser){
        return userRepository.findById(id)
                .map(existingUser -> {
                    ModelUtil.copyFields(newUser, existingUser);
                    return userRepository.save(existingUser);
                });
    }

    /**
     * Checks if a User exists by its ID.
     * @param id the ID of the User to check
     * @return true if the User exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
