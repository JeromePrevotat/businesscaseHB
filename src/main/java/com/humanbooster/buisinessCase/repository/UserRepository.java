package com.humanbooster.buisinessCase.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.User;

/**
 * User's Repository. Provides basic CRUD operations for User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUserName(String userName);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUserName(String userName);
    
    boolean existsByEmail(String email);
}
