package com.humanbooster.businesscase.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.businesscase.model.User;

/**
 * User's Repository. Provides basic CRUD operations for User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    // @Query("SELECT u FROM User u JOIN fetch u.roleList WHERE u.username = :username")
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
