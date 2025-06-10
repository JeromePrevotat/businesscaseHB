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

import com.humanbooster.buisinessCase.dto.VehiculeDTO;
import com.humanbooster.buisinessCase.mapper.EntityMapper;
import com.humanbooster.buisinessCase.model.Vehicule;
import com.humanbooster.buisinessCase.service.VehiculeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing Vehicule entities.
 * Provides endpoints for creating, retrieving, updating, and deleting vehicules.
 */
@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {
    private final VehiculeService vehiculeService;
    private final EntityMapper mapper;


    /**
     * Get all vehicules.
     * GET /api/vehicules
     * @return ResponseEntity with the list of vehicules
     */
    @GetMapping
    public ResponseEntity<List<VehiculeDTO>> getAllVehicules(){
        List<VehiculeDTO> vehiculeDTOs = vehiculeService.getAllVehicules().stream()
                .map(v -> mapper.toDTO((Vehicule) v))
                .collect(Collectors.toList());
        return ResponseEntity.ok(vehiculeDTOs);
    }

    /**
     * Get a vehicule by ID.
     * GET /api/vehicules/{id}
     * @param id The ID of the vehicule to retrieve
     * @return ResponseEntity with the vehicule if found, or 404 Not Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> getVehiculeById(@PathVariable Long id){
        return vehiculeService.getVehiculeById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new vehicule.
     * POST /api/vehicules
     * @param vehicule The vehicule entity to be saved
     * @return ResponseEntity with the saved vehicule and 201 Created status
     */
    @PostMapping
    public ResponseEntity<VehiculeDTO> saveVehicule(@Valid @RequestBody VehiculeDTO vehiculeDTO){
        Vehicule newVehicule = mapper.toEntity(vehiculeDTO);
        Vehicule savedVehicule = vehiculeService.saveVehicule(newVehicule);
        VehiculeDTO savedVehiculeDTO = mapper.toDTO(savedVehicule);
        // Conform RESTful practices, we should return a URI to the created resource.
        // URI location = URI.create("/api/vehicules/" + savedVehicule.getId());
        // return ResponseEntity.created(location).body(savedVehicule);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehiculeDTO);
    }

    /**
     * Delete a vehicule by ID.
     * DELETE /api/vehicules/{id}
     * @param id The ID of the vehicule to delete
     * @return ResponseEntity with the 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehiculeById(@PathVariable Long id){
        return vehiculeService.deleteVehiculeById(id).isPresent() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Update a vehicule by ID.
     * PUT /api/vehicules/{id}
     * @param id The ID of the vehicule to update
     * @param newVehicule The updated vehicule entity
     * @return ResponseEntity with the updated vehicule if found, or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehiculeDTO> updateVehicule(@PathVariable Long id, @Valid @RequestBody Vehicule newVehicule){
        return vehiculeService.updateVehicule(id, newVehicule)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

