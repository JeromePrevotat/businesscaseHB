package com.humanbooster.businesscase.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humanbooster.businesscase.model.Reservation;
import com.humanbooster.businesscase.model.ReservationState;
import com.humanbooster.businesscase.model.Station;
import com.humanbooster.businesscase.model.User;

import jakarta.transaction.Transactional;

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

    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.state = :newState WHERE r.state = :oldState AND r.endDate <= :now")
    int markExpiredReservations(
        @Param("oldState") ReservationState oldState,
        @Param("newState") ReservationState newState,
        @Param("now") LocalDateTime now
    );
}
