package com.humanbooster.buisinessCase.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.humanbooster.buisinessCase.model.RefreshToken;
import com.humanbooster.buisinessCase.model.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public List<RefreshToken> findByUserIdOrderByIssuedAtDesc(Long userId);
    Optional<RefreshToken> findByUserId(Long userId);
    Optional<RefreshToken> findByToken(String refreshToken);
    Optional<User> findUserByToken(String token);
}
