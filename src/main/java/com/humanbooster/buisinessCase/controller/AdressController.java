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

import com.humanbooster.buisinessCase.dto.AdressDTO;
import com.humanbooster.buisinessCase.mapper.EntityMapper;
import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.service.AdressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for managing Adresses.
 * Provides CRUD's endpoints Adresses.
 * Uses DTO to avoid circular references in JSON serialization.
 */
@RestController
@RequestMapping("/api/adresses")
@RequiredArgsConstructor
public class AdressController {
    private final AdressService adressService;
    private final EntityMapper mapper;


    /**
     * Get all Adresses.
     * GET /api/adresses
     * @return a list of all Adresses
     */
    @GetMapping
    public ResponseEntity<List<AdressDTO>> getAllAdresses() {
        List<Adress> adresses = adressService.getAllAdresses();
        List<AdressDTO> adressesDTO = adresses.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(adressesDTO);
    }

    /**
     * Get an Adress by its ID.
     * GET /api/adresses/{id}
     * @param id the ID of the Adress to retrieve
     * @return the Adress if found, or 404 Not Found if not
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdressDTO> getAdressById(@PathVariable long id){
        return adressService.getAdressById(id)
                .map(adress -> ResponseEntity.ok(mapper.toDTO(adress)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new Adress.
     * POST /api/adresses
     * @param adress the Adress to save
     * @return 201 Created if successful
     */
    @PostMapping("/adresses")
    public ResponseEntity<AdressDTO> saveAdresse(@Valid @RequestBody AdressDTO adressDTO) {
        Adress adress = mapper.toEntity(adressDTO);
        Adress savedAdress = adressService.saveAdress(adress);
        AdressDTO savedDTO = mapper.toDTO(savedAdress);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDTO);
    }
    
    /**
     * Delete an Adress by its ID.
     * DELETE /api/adresses/{id}
     * @param id the ID of the Adress to delete
     * @return ResponseEntity with no content or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<AdressDTO> deleteAdressById(@PathVariable long id){
        if (!adressService.existsById(id)) return ResponseEntity.notFound().build();
        adressService.deleteAdressById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update an existing Adress.
     * PUT /api/adresses/{id}
     * @param id the ID of the Adress to update
     * @param newAdress the new Adress data to update
     * @return ResponseEntity with the updated Adress or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdressDTO> updateAdress(@PathVariable long id, @Valid @RequestBody AdressDTO newAdressDTO){
        if (!adressService.existsById(id)) return ResponseEntity.notFound().build();
        Adress adress = mapper.toEntity(newAdressDTO); // From DTO to Entity
        Adress updatedAdress = adressService.updateAdress(id, adress).get(); // Update the Adress
        AdressDTO updatedAdressDTO = mapper.toDTO(updatedAdress); // Back to DTO
        return ResponseEntity.ok(updatedAdressDTO);
    }

}
