package com.humanbooster.buisinessCase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.Spot;

/**
 * Spot's Repository. Provides basic CRUD operations for Spot entities.
 */
@Repository
public interface SpotRepository extends JpaRepository<Spot, Long>{

}
