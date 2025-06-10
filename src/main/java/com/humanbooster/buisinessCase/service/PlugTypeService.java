package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.buisinessCase.model.PlugType;
import com.humanbooster.buisinessCase.repository.PlugTypeRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing PlugTypes.
 * Provides methods to save, retrieve, update, and delete PlugTypes.
 */

@Service
@RequiredArgsConstructor
@Transactional
public class PlugTypeService{
    private final PlugTypeRepository plugTypeRepository;

    /**
     * Saves a new PlugType.
     * @param plugType the PlugType to save
     * @return the newly saved PlugType
     */
    @Transactional
    public PlugType savePlugType(PlugType plugType){
        return plugTypeRepository.save(plugType);
    }

    /**
     * Retrieves all PlugTypes.
     * @return a list of all PlugTypes
     */
    @Transactional(readOnly = true)
    public List<PlugType> getAllPlugTypes(){
        return plugTypeRepository.findAll();
    }

    /**
     * Retrieves a PlugType by its ID.
     * @param id the ID of the PlugType to retrieve
     * @return  an Optional containing the PlugType if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<PlugType> getPlugTypeById(Long id){
        return plugTypeRepository.findById(id);
    }

    /**
     * Deletes a PlugType by its ID.
     * @param id the ID of the PlugType to delete
     * @return an Optional containing the deleted PlugType if found, or empty if not found
     */
    @Transactional
    public Optional<PlugType> deletePlugTypeById(Long id){
        Optional<PlugType> plugTypeOpt = plugTypeRepository.findById(id);
        plugTypeOpt.ifPresent(plugTypeRepository::delete);
        return plugTypeOpt;
    }

    /**
     * Updates an existing PlugType.
     * @param id the ID of the PlugType to update
     * @param newPlugType the new PlugType data to update
     * @return an Optional containing the updated PlugType if found, or empty if not found
     */
    @Transactional
    public Optional<PlugType> updatePlugType(Long id, PlugType newPlugType){
        return plugTypeRepository.findById(id)
                .map(existingPlugType -> {
                    ModelUtil.copyFields(newPlugType, existingPlugType);
                    return plugTypeRepository.save(existingPlugType);
                });
    }

    /**
     * Checks if a PlugType exists by its ID.
     * @param id the ID of the PlugType to check
     * @return true if the PlugType exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return plugTypeRepository.existsById(id);
    }
}
