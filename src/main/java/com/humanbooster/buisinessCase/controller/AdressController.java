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

import com.humanbooster.buisinessCase.mapper.AdressMapper;
import com.humanbooster.buisinessCase.dto.AdressDTO;
import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.service.AdressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing Adress entities.
 * Provides endpoints for creating, retrieving, updating, and deleting adresses.
 */
@RestController
@RequestMapping("/api/adresses")
@RequiredArgsConstructor
public class AdressController {
    private final AdressService adressService;
    private final AdressMapper mapper;


    /**
     * Get all adresses.
     * GET /api/adresses
     * @return ResponseEntity with the list of adresses
     */
    @GetMapping
    public ResponseEntity<List<AdressDTO>> getAllAdresses(){
        List<AdressDTO> adressDTOs = adressService.getAllAdresses().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(adressDTOs);
    }

    /**
     * Get a adress by ID.
     * GET /api/adresses/{id}
     * @param id The ID of the adress to retrieve
     * @return ResponseEntity with the adress if found, or 404 Not Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdressDTO> getAdressById(@PathVariable Long id){
        return adressService.getAdressById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new adress.
     * POST /api/adresses
     * @param adress The adress entity to be saved
     * @return ResponseEntity with the saved adress and 201 Created status
     */
    @PostMapping
    public ResponseEntity<AdressDTO> saveAdress(@Valid @RequestBody AdressDTO adressDTO){
        Adress newAdress = mapper.toEntity(adressDTO);
        Adress savedAdress = adressService.saveAdress(newAdress);
        AdressDTO savedAdressDTO = mapper.toDTO(savedAdress);
        // Conform RESTful practices, we should return a URI to the created resource.
        // URI location = URI.create("/api/adresses/" + savedAdress.getId());
        // return ResponseEntity.created(location).body(savedAdress);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdressDTO);
    }

    /**
     * Delete a adress by ID.
     * DELETE /api/adresses/{id}
     * @param id The ID of the adress to delete
     * @return ResponseEntity with the 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdressById(@PathVariable Long id){
        return adressService.deleteAdressById(id).isPresent() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Update a adress by ID.
     * PUT /api/adresses/{id}
     * @param id The ID of the adress to update
     * @param newAdress The updated adress entity
     * @return ResponseEntity with the updated adress if found, or 404 Not Found if not found
     */
    @PutMapping(value= "/{id}", consumes= "application/json")
    public ResponseEntity<AdressDTO> updateAdress(@PathVariable Long id, @Valid @RequestBody AdressDTO newAdressDTO){
        Adress newAdress = mapper.toEntity(newAdressDTO);
        return adressService.updateAdress(id, newAdress)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


