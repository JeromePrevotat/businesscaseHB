package com.humanbooster.businesscase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.businesscase.model.Vehicule;
import com.humanbooster.businesscase.repository.VehiculeRepository;
import com.humanbooster.businesscase.utils.ModelUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing Vehicules.
 * Provides methods to save, retrieve, update, and delete Vehicules.
 */

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeService{
    private final VehiculeRepository vehiculeRepository;

    /**
     * Saves a new Vehicule.
     * @param vehicule the Vehicule to save
     * @return the newly saved Vehicule
     */
    @Transactional
    public Vehicule saveVehicule(Vehicule vehicule){
        return vehiculeRepository.save(vehicule);
    }

    /**
     * Retrieves all Vehicules.
     * @return a list of all Vehicules
     */
    @Transactional(readOnly = true)
    public List<Vehicule> getAllVehicules(){
        return vehiculeRepository.findAll();
    }

    /**
     * Retrieves a Vehicule by its ID.
     * @param id the ID of the Vehicule to retrieve
     * @return  an Optional containing the Vehicule if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<Vehicule> getVehiculeById(Long id){
        return vehiculeRepository.findById(id);
    }

    /**
     * Deletes a Vehicule by its ID.
     * @param id the ID of the Vehicule to delete
     * @return an Optional containing the deleted Vehicule if found, or empty if not found
     */
    @Transactional
    public Optional<Vehicule> deleteVehiculeById(Long id){
        Optional<Vehicule> vehiculeOpt = vehiculeRepository.findById(id);
        vehiculeOpt.ifPresent(vehiculeRepository::delete);
        return vehiculeOpt;
    }

    /**
     * Updates an existing Vehicule.
     * @param id the ID of the Vehicule to update
     * @param newVehicule the new Vehicule data to update
     * @return an Optional containing the updated Vehicule if found, or empty if not found
     */
    @Transactional
    public Optional<Vehicule> updateVehicule(Long id, Vehicule newVehicule){
        return vehiculeRepository.findById(id)
                .map(existingVehicule -> {
                    ModelUtil.copyFields(newVehicule, existingVehicule);
                    return vehiculeRepository.save(existingVehicule);
                });
    }

    /**
     * Checks if a Vehicule exists by its ID.
     * @param id the ID of the Vehicule to check
     * @return true if the Vehicule exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return vehiculeRepository.existsById(id);
    }
}
