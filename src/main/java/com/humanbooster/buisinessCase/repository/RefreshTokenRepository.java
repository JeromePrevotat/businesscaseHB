package com.humanbooster.buisinessCase.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.RefreshToken;
import com.humanbooster.buisinessCase.model.User;

/**
 * Refresh Token Repository. Provides basic CRUD operations for Refresh Token entities.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);
    Optional<User> findUserByToken(String token);
}