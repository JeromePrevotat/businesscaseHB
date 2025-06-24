package com.humanbooster.buisinessCase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.humanbooster.buisinessCase.model.JwtRefresh;

public interface JwtRefreshRepository extends JpaRepository<JwtRefresh, Long> {
    public List<JwtRefresh> findByUserIdOrderByIssuedAtDesc(Long userId);
}
