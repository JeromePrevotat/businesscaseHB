package com.humanbooster.buisinessCase.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * REST controller for managing Reservations.
 * Provides endpoints to create, read, update, and delete Reservations.
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    /**
     * Endpoint to retrieve all reservations.
     * GET /api/reservations
     * @return ResponseEntity containing a list of all reservations.
     */
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(){
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    /**
     * Endpoint to retrieve a reservation by its ID.
     * GET /api/reservations/{id}
     * @param id the ID of the reservation to retrieve
     * @return ResponseEntity containing the reservation if found, or 404 Not Found if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id){
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
/**
     * Endpoint to save a new reservation.
     * POST /api/reservations
     * @param reservation the reservation object to save
     */
    @PostMapping
    public ResponseEntity<Reservation> saveReservation(@Valid @RequestBody Reservation reservation){
        Reservation savedReservation = reservationService.saveReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
    }

    /**
     * Endpoint to delete a reservation by its ID.
     * DELETE /api/reservations/{id}
     * @param id the ID of the reservation to delete
     * @return ResponseEntity containing the deleted reservation if found, or 404 Not Found if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Reservation> deleteReservationById(@PathVariable Long id){
        if (!reservationService.existsById(id)) return ResponseEntity.notFound().build();
        reservationService.deleteReservationById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to update an existing reservation.
     * PUT /api/reservations/{id}
     * @param id the ID of the reservation to update
     * @param newReservation the new reservation object with updated fields
     * @return ResponseEntity containing the updated reservation if found, or 404 Not Found if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @Valid @RequestBody Reservation newReservation){
        return reservationService.updateReservation(id, newReservation)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
