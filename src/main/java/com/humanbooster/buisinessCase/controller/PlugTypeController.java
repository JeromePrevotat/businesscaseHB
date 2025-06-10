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

import com.humanbooster.buisinessCase.mapper.PlugTypeMapper;
import com.humanbooster.buisinessCase.dto.PlugTypeDTO;
import com.humanbooster.buisinessCase.model.PlugType;
import com.humanbooster.buisinessCase.service.PlugTypeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing PlugType entities.
 * Provides endpoints for creating, retrieving, updating, and deleting plug types.
 */
@RestController
@RequestMapping("/api/plug-types")
@RequiredArgsConstructor
public class PlugTypeController {
    private final PlugTypeService plugTypeService;
    private final PlugTypeMapper mapper;


    /**
     * Get all plug types.
     * GET /api/plug-types
     * @return ResponseEntity with the list of plug types
     */
    @GetMapping
    public ResponseEntity<List<PlugTypeDTO>> getAllPlugTypes(){
        List<PlugTypeDTO> plugTypeDTOs = plugTypeService.getAllPlugTypes().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(plugTypeDTOs);
    }

    /**
     * Get a plug type by ID.
     * GET /api/plug-types/{id}
     * @param id The ID of the plug type to retrieve
     * @return ResponseEntity with the plug type if found, or 404 Not Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlugTypeDTO> getPlugTypeById(@PathVariable Long id){
        return plugTypeService.getPlugTypeById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new plug type.
     * POST /api/plug-types
     * @param plugType The plug type entity to be saved
     * @return ResponseEntity with the saved plug type and 201 Created status
     */
    @PostMapping
    public ResponseEntity<PlugTypeDTO> savePlugType(@Valid @RequestBody PlugTypeDTO plugTypeDTO){
        PlugType newPlugType = mapper.toEntity(plugTypeDTO);
        PlugType savedPlugType = plugTypeService.savePlugType(newPlugType);
        PlugTypeDTO savedPlugTypeDTO = mapper.toDTO(savedPlugType);
        // Conform RESTful practices, we should return a URI to the created resource.
        // URI location = URI.create("/api/plug-types/" + savedPlugType.getId());
        // return ResponseEntity.created(location).body(savedPlugType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlugTypeDTO);
    }

    /**
     * Delete a plug type by ID.
     * DELETE /api/plug-types/{id}
     * @param id The ID of the plug type to delete
     * @return ResponseEntity with the 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlugTypeById(@PathVariable Long id){
        return plugTypeService.deletePlugTypeById(id).isPresent() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Update a plug type by ID.
     * PUT /api/plug-types/{id}
     * @param id The ID of the plug type to update
     * @param newPlugType The updated plug type entity
     * @return ResponseEntity with the updated plug type if found, or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlugTypeDTO> updatePlugType(@PathVariable Long id, @Valid @RequestBody PlugType newPlugType){
        return plugTypeService.updatePlugType(id, newPlugType)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

