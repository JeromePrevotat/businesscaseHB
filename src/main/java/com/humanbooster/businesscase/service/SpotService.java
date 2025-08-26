package com.humanbooster.businesscase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.businesscase.model.Spot;
import com.humanbooster.businesscase.repository.SpotRepository;
import com.humanbooster.businesscase.utils.ModelUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing Spots.
 * Provides methods to save, retrieve, update, and delete Spots.
 */

@Service
@RequiredArgsConstructor
@Transactional
public class SpotService{
    private final SpotRepository spotRepository;

    /**
     * Saves a new Spot.
     * @param spot the Spot to save
     * @return the newly saved Spot
     */
    @Transactional
    public Spot saveSpot(Spot spot){
        return spotRepository.save(spot);
    }

    /**
     * Retrieves all Spots.
     * @return a list of all Spots
     */
    @Transactional(readOnly = true)
    public List<Spot> getAllSpots(){
        return spotRepository.findAll();
    }

    /**
     * Retrieves a Spot by its ID.
     * @param id the ID of the Spot to retrieve
     * @return  an Optional containing the Spot if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<Spot> getSpotById(Long id){
        return spotRepository.findById(id);
    }

    /**
     * Deletes a Spot by its ID.
     * @param id the ID of the Spot to delete
     * @return an Optional containing the deleted Spot if found, or empty if not found
     */
    @Transactional
    public Optional<Spot> deleteSpotById(Long id){
        Optional<Spot> spotOpt = spotRepository.findById(id);
        spotOpt.ifPresent(spotRepository::delete);
        return spotOpt;
    }

    /**
     * Updates an existing Spot.
     * @param id the ID of the Spot to update
     * @param newSpot the new Spot data to update
     * @return an Optional containing the updated Spot if found, or empty if not found
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
     * @param id the ID of the Spot to check
     * @return true if the Spot exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return spotRepository.existsById(id);
    }
}
