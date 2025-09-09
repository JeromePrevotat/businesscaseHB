package com.humanbooster.businesscase.controller;

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

import com.humanbooster.businesscase.dto.SpotDTO;
import com.humanbooster.businesscase.mapper.SpotMapper;
import com.humanbooster.businesscase.model.Spot;
import com.humanbooster.businesscase.service.SpotService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing Spot entities.
 * Provides endpoints for creating, retrieving, updating, and deleting spots.
 */
@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
public class SpotController {
    private final SpotService spotService;
    private final SpotMapper mapper;

    /**
     * Get all spots.
     * GET /api/spots
     * @return ResponseEntity with the list of spots
     */
    @GetMapping
    public ResponseEntity<List<SpotDTO>> getAllSpots(){
        List<SpotDTO> spotDTOs = spotService.getAllSpots().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(spotDTOs);
    }

    /**
     * Get a spot by ID.
     * GET /api/spots/{id}
     * @param id The ID of the spot to retrieve
     * @return ResponseEntity with the spot if found, or 404 Not Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpotDTO> getSpotById(@PathVariable Long id){
        return spotService.getSpotById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new spot.
     * POST /api/spots
     * @param spot The spot entity to be saved
     * @return ResponseEntity with the saved spot and 201 Created status
     */
    @PostMapping
    public ResponseEntity<SpotDTO> saveSpot(@Valid @RequestBody SpotDTO spotDTO){
        Spot newSpot = mapper.toEntity(spotDTO);
        Spot savedSpot = spotService.saveSpot(newSpot);
        SpotDTO savedSpotDTO = mapper.toDTO(savedSpot);
        // Conform RESTful practices, we should return a URI to the created resource.
        // URI location = URI.create("/api/spots/" + savedSpot.getId());
        // return ResponseEntity.created(location).body(savedSpot);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSpotDTO);
    }

    /**
     * Delete a spot by ID.
     * DELETE /api/spots/{id}
     * @param id The ID of the spot to delete
     * @return ResponseEntity with the 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpotById(@PathVariable Long id){
        return spotService.deleteSpotById(id).isPresent() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Update a spot by ID.
     * PUT /api/spots/{id}
     * @param id The ID of the spot to update
     * @param newSpot The updated spot entity
     * @return ResponseEntity with the updated spot if found, or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpotDTO> updateSpot(@PathVariable Long id, @Valid @RequestBody SpotDTO newSpotDTO){
        return spotService.updateSpot(id, mapper.toEntity(newSpotDTO))
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

