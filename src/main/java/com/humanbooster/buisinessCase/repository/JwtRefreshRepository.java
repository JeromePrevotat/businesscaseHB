package com.humanbooster.buisinessCase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.humanbooster.buisinessCase.model.JwtRefresh;

public interface JwtRefreshRepository extends JpaRepository<JwtRefresh, Long> {
    public List<JwtRefresh> findByUserIdOrderByCreatedAtDesc(Long userId);
}
