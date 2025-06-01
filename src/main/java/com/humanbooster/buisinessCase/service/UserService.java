package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.repository.UserRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import lombok.RequiredArgsConstructor;


/**
 * User's Service
 * This class provides methods to manage User entities, including saving, retrieving, updating, and deleting users.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    /**
     * Saves a new User
     * @param user The User entity to be saved
     * @return The saved User entity
     */
    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

    /**
     * Retrieves all Users
     * @return A list of all User entities
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /**
     * Retrieves a User by its ID
     * @param id The ID of the User to retrieve
     * @return An Optional containing the User if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    /**
     * Deletes a User by its ID
     * @param id The ID of the User to delete
     * @return true if the User was deleted, false if not found
     */
    @Transactional
    public boolean deleteUserById(Long id){
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

    /**
     * Updates an existing User
     * @param id The ID of the User to update
     * @param newUser The User entity with updated fields
     * @return An Optional containing the updated User if found, or empty if not found
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
     * Checks if a User exists by its ID
     * @param id The ID of the User to check
     * @return true if the User exists, false otherwise
     */
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
