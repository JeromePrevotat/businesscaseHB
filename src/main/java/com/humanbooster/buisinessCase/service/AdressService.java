package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.repository.AdressRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing Adresses.
 * Provides methods to save, retrieve, update, and delete Adresses.
 */

@Service
@RequiredArgsConstructor
@Transactional
public class AdressService{
    private final AdressRepository adressRepository;

    /**
     * Saves an new Adress.
     * @param adress the Adress to save
     * @return the newly saved Adress
     */
    @Transactional
    public Adress saveAdress(Adress adress){
        return adressRepository.save(adress);
    }

    /**
     * Retrieves all Adresses.
     * @return a list of all Adresses
     */
    @Transactional(readOnly = true)
    public List<Adress> getAllAdresses(){
        return adressRepository.findAll();
    }

    /**
     * Retrieves an Adress by its ID.
     * @param id the ID of the Adress to retrieve
     * @return  an Optional containing the Adress if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<Adress> getAdressById(long id){
        return adressRepository.findById(id);
    }

    /**
     * Deletes an Adress by its ID.
     * @param id the ID of the Adress to delete
     * @return an Optional containing the deleted Adress if found, or empty if not found
     */
    @Transactional
    public Optional<Adress> deleteAdressById(long id){
        Optional<Adress> adressOpt = adressRepository.findById(id);
        adressOpt.ifPresent(adressRepository::delete);
        return adressOpt;
    }

    /**
     * Updates an existing Adress.
     * @param id the ID of the Adress to update
     * @param newAdress the new Adress data to update
     * @return an Optional containing the updated Adress if found, or empty if not found
     */
    @Transactional
    public Optional<Adress> updateAdress(long id, Adress newAdress){
        return adressRepository.findById(id)
                .map(existingAdress -> {
                    ModelUtil.copyFields(newAdress, existingAdress);
                    return adressRepository.save(existingAdress);
                });
    }

    /**
     * Checks if an Adress exists by its ID.
     * @param id the ID of the Adress to check
     * @return true if the Adress exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return adressRepository.existsById(id);
    }
}
