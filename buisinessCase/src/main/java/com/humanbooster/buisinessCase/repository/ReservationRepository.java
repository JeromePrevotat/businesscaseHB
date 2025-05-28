package com.humanbooster.buisinessCase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>{

}
