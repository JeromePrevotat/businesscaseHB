package com.humanbooster.buisinessCase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.model.ReservationState;
import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.model.User;

/**
 * Reservation's Repository. Provides basic CRUD operations for Reservation entities.
 * Provides methods to find reservations by User, Station, and State.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    List<Reservation> findByUser(User user);
    
    List<Reservation> findByStation(Station station);
    
    List<Reservation> findByState(ReservationState state);
    
    List<Reservation> findByUserAndState(User user, ReservationState state);
    
    List<Reservation> findByStationAndState(Station station, ReservationState state);
}
