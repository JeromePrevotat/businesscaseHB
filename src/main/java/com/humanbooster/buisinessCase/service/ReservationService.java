package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.repository.ReservationRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing Reservations.
 * Provides methods to save, retrieve, update, and delete Reservations.
 */

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService{
    private final ReservationRepository reservationRepository;

    /**
     * Saves a new Reservation.
     * @param reservation the Reservation to save
     * @return the newly saved Reservation
     */
    @Transactional
    public Reservation saveReservation(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    /**
     * Retrieves all Reservations.
     * @return a list of all Reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    /**
     * Retrieves a Reservation by its ID.
     * @param id the ID of the Reservation to retrieve
     * @return  an Optional containing the Reservation if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<Reservation> getReservationById(Long id){
        return reservationRepository.findById(id);
    }

    /**
     * Deletes a Reservation by its ID.
     * @param id the ID of the Reservation to delete
     * @return an Optional containing the deleted Reservation if found, or empty if not found
     */
    @Transactional
    public Optional<Reservation> deleteReservationById(Long id){
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        reservationOpt.ifPresent(reservationRepository::delete);
        return reservationOpt;
    }

    /**
     * Updates an existing Reservation.
     * @param id the ID of the Reservation to update
     * @param newReservation the new Reservation data to update
     * @return an Optional containing the updated Reservation if found, or empty if not found
     */
    @Transactional
    public Optional<Reservation> updateReservation(Long id, Reservation newReservation){
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    ModelUtil.copyFields(newReservation, existingReservation);
                    return reservationRepository.save(existingReservation);
                });
    }

    /**
     * Checks if a Reservation exists by its ID.
     * @param id the ID of the Reservation to check
     * @return true if the Reservation exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }
}
