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
 * ReservationService provides methods to manage Reservations.
 * It includes saving, retrieving, updating, and deleting reservations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;


    /**
     * Saves a new Reservation
     * @param reservation the Reservation object to be saved
     * @return the saved Reservation object
     */
    @Transactional
    public Reservation saveReservation(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    /**
     * Retrieves all Reservations
     * @return a list of all Reservation objects
     */
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    /**
     * Retrieves a Reservation by its ID
     * @param id the ID of the Reservation to retrieve
     * @return an Optional containing the Reservation if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<Reservation> getReservationById(long id){
        return reservationRepository.findById(id);
    }

    /**
     * Deletes a Reservation by its ID
     * @param id the ID of the Reservation to delete
     * @return an Optional containing the deleted Reservation if found, or empty if not found
     */
    @Transactional
    public Optional<Reservation> deleteReservationById(long id){
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        reservationOpt.ifPresent(reservationRepository::delete);
        return reservationOpt;
    }

    /**
     * Updates an existing Reservation
     * @param id the ID of the Reservation to update
     * @param newReservation the new Reservation object with updated fields
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
     * Retrieves a Reservation by its ID, throwing an exception if not found
     * @param id the ID of the Reservation to retrieve
     * @return True if the Reservation exists, False otherwise
     */
    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }

}
