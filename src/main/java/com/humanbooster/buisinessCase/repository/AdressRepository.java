package com.humanbooster.buisinessCase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.Adress;

/**
 * Adress's Repository. Provides basic CRUD operations for Adress entities.
 */

@Repository
public interface AdressRepository extends JpaRepository<Adress, Long>{
    
}
