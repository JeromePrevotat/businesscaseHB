package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.buisinessCase.model.Spot;
import com.humanbooster.buisinessCase.repository.SpotRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing Spot entities.
 * Provides methods to save, retrieve, update, and delete Spots.
 */

@Service
@RequiredArgsConstructor
@Transactional
public class SpotService {
    private final SpotRepository spotRepository;

    /**
     * Gets all Spots
     * @return List of all Spot entities
     */
    @Transactional(readOnly = true)
    public List<Spot> getAllSpots(){
        return spotRepository.findAll();
    }

    /**
     * Gets a Spot by its ID
     * @param id the ID of the Spot
     * @return Optional containing the Spot if found, or empty if not
     */
    @Transactional(readOnly = true)
    public Optional<Spot> getSpotById(Long id){
        return spotRepository.findById(id);
    }

    /**
     * Saves a Spot entity to the database.
     * @param spot the Spot entity to save
     * @return the saved Spot entity
     */
    @Transactional
    public Spot saveSpot(Spot spot){
        return spotRepository.save(spot);
    }

    /**
     * Deletes a Spot by its ID.
     * @param id the ID of the Spot to delete
     * @return Optional containing the deleted Spot if found, or empty if not
     */
    @Transactional
    public Optional<Spot> deleteSpotById(Long id){
        Optional<Spot> spotOpt = spotRepository.findById(id);
        spotOpt.ifPresent(spotRepository::delete);
        return spotOpt;
    }

    /**
     * Updates an existing Spot entity.
     * @param id the ID of the Spot to update
     * @param newSpot the new Spot entity with updated fields
     * @return Optional containing the updated Spot if found, or empty if not
     */
    @Transactional
    public Optional<Spot> updateSpot(Long id, Spot newSpot){
        return spotRepository.findById(id)
                .map(existingSpot -> {
                    ModelUtil.copyFields(newSpot, existingSpot);
                    return spotRepository.save(existingSpot);
                });
    }

    /**
     * Checks if a Spot exists by its ID.
     * @param id the ID of the Spot
     * @return true if the Spot exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return spotRepository.existsById(id);
    }

}