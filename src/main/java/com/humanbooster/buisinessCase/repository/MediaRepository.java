package com.humanbooster.buisinessCase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.Media;

/**
 * Media's Repository. Provides basic CRUD operations for Media entities.
 * Provides methods to save, delete, and find Media entities.
 */
@Repository
public interface MediaRepository extends JpaRepository<Media, Long>{
    List<Media> findByType(String type);
}
