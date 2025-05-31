package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.repository.StationRepository;

import com.humanbooster.buisinessCase.utils.ModelUtil;
import lombok.RequiredArgsConstructor;

/**
 * Service class for managing Stations.
 * Provides methods to save, retrieve, update, and delete Stations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class StationService{
    private final StationRepository stationRepository;

    /**
     * Finds all Stations.
     * @return a list of all Stations
     */
    @Transactional(readOnly = true)
    public List<Station> getAllStations(){
        return stationRepository.findAll();
    }

    /**
     * Retrieves a Station by its ID.
     * @param id the ID of the Station to retrieve
     * @return an Optional containing the found Station, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<Station> getStationById(long id){
        return stationRepository.findById(id);
    }

    /**
     * Saves a new Station.
     * @param station the Station to save
     * @return the newly saved Station
     */
    @Transactional
    public Station saveStation(Station station){
        return stationRepository.save(station);
    }

    /**
     * Deletes a Station by its ID.
     * @param id the ID of the Station to delete
     * @return an Optional containing the deleted Station if found, or empty if not found
     */
    @Transactional
    public Optional<Station> deleteStationById(long id){
        Optional<Station> stationOpt = stationRepository.findById(id);
        stationOpt.ifPresent(stationRepository::delete);
        return stationOpt;
    }

    /**
     * Updates an existing Station.
     * @param newStation the Station with updated fields
     * @param id the ID of the Station to update
     * @return an Optional containing the updated Station, or empty if not found
     */
    @Transactional
    public Optional<Station> updateStation(Long id, Station newStation){
        return stationRepository.findById(id)
                .map(existingStation -> {
                    ModelUtil.copyFields(newStation, existingStation);
                    return stationRepository.save(existingStation);
                });
    }

    /**
     * Checks if a Station exists by its ID.
     * @param id the ID of the Station to check
     * @return true if the Station exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(long id) {
        return stationRepository.existsById(id);
    }

}
