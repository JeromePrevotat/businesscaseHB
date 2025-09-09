package com.humanbooster.businesscase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.businesscase.model.PlugType;

/**
 * PlugType's Repository. Provides basic CRUD operations for PlugType entities.
 */
@Repository
public interface PlugTypeRepository extends JpaRepository<PlugType, Long>{

}
