package com.humanbooster.businesscase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.businesscase.model.Vehicule;

/**
 * Vehicle's Repository. Provides basic CRUD operations for Vehicle entities.
 */
@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long>{

}
