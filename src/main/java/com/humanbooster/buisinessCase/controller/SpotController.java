package com.humanbooster.buisinessCase.controller;

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

import com.humanbooster.buisinessCase.dto.SpotDTO;
import com.humanbooster.buisinessCase.mapper.EntityMapper;
import com.humanbooster.buisinessCase.model.Spot;
import com.humanbooster.buisinessCase.service.SpotService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * REST controller for managing Spot entities.
 * Provides endpoints to create, read, update, and delete Spots.
 */
@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
public class SpotController {
    private final SpotService spotService;
    private final EntityMapper mapper;
    
    /**
     * Get all Spots.
     * GET /api/spots
     * @return List of SpotDTOs
     */
    @GetMapping()
    public ResponseEntity<List<SpotDTO>> getAllSpots(){
        List<Spot> spots = spotService.getAllSpots();
        List<SpotDTO> spotDTOs = spots.stream()
                                       .map(mapper::toDTO)
                                       .collect(Collectors.toList());
        return ResponseEntity.ok(spotDTOs);
    }

    /**
     * Get a Spot by ID.
     * GET /api/spots/{id}
     * @param id the Spot ID
     * @return the Spot
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpotDTO> getSpotById(@PathVariable Long id){
        return spotService.getSpotById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new Spot.
     * POST /api/spots
     * @param spot the Spot to save
     * @return the newly saved Spot
     */
    @PostMapping()
    public ResponseEntity<SpotDTO> saveSpot(@Valid @RequestBody SpotDTO spotDTO){
        Spot newSpot = mapper.toEntity(spotDTO);
        Spot savedSpot = spotService.saveSpot(newSpot);
        SpotDTO savedSpotDTO = mapper.toDTO(savedSpot);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSpotDTO);
    }

    /**
     * Delete a Spot by ID.
     * DELETE /api/spots/{id}
     * @param id the Spot ID
     * @return ResponseEntity with status OK if deleted, NOT_FOUND if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpotById(@PathVariable Long id){
        if (!spotService.existsById(id)) return ResponseEntity.notFound().build();
        spotService.deleteSpotById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Update an existing Spot.
     * PUT /api/spots/{id}
     * @param id the Spot ID to update
     * @param newSpot the updated Spot data
     * @return ResponseEntity with the updated Spot or NOT_FOUND if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpotDTO> updateSpot(@PathVariable Long id, @Valid @RequestBody Spot newSpot){
        return spotService.updateSpot(id, newSpot)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
