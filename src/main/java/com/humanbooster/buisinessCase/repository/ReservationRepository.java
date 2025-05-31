package com.humanbooster.buisinessCase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.Reservation;

/**
 * Reservation's Repository. Provides basic CRUD operations for Reservation entities.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}
