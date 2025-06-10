package com.humanbooster.buisinessCase.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

import com.humanbooster.buisinessCase.mapper.EntityMapper;
import com.humanbooster.buisinessCase.dto.ReservationDTO;
import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing Reservation entities.
 * Provides endpoints for creating, retrieving, updating, and deleting reservations.
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final EntityMapper mapper;


    /**
     * Get all reservations.
     * GET /api/reservations
     * @return ResponseEntity with the list of reservations
     */
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations(){
        List<ReservationDTO> reservationDTOs = reservationService.getAllReservations().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationDTOs);
    }

    /**
     * Get a reservation by ID.
     * GET /api/reservations/{id}
     * @param id The ID of the reservation to retrieve
     * @return ResponseEntity with the reservation if found, or 404 Not Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id){
        return reservationService.getReservationById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new reservation.
     * POST /api/reservations
     * @param reservation The reservation entity to be saved
     * @return ResponseEntity with the saved reservation and 201 Created status
     */
    @PostMapping
    public ResponseEntity<ReservationDTO> saveReservation(@Valid @RequestBody ReservationDTO reservationDTO){
        Reservation newReservation = mapper.toEntity(reservationDTO);
        Reservation savedReservation = reservationService.saveReservation(newReservation);
        ReservationDTO savedReservationDTO = mapper.toDTO(savedReservation);
        // Conform RESTful practices, we should return a URI to the created resource.
        // URI location = URI.create("/api/reservations/" + savedReservation.getId());
        // return ResponseEntity.created(location).body(savedReservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservationDTO);
    }

    /**
     * Delete a reservation by ID.
     * DELETE /api/reservations/{id}
     * @param id The ID of the reservation to delete
     * @return ResponseEntity with the 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationById(@PathVariable Long id){
        return reservationService.deleteReservationById(id).isPresent() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Update a reservation by ID.
     * PUT /api/reservations/{id}
     * @param id The ID of the reservation to update
     * @param newReservation The updated reservation entity
     * @return ResponseEntity with the updated reservation if found, or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id, @Valid @RequestBody Reservation newReservation){
        return reservationService.updateReservation(id, newReservation)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

