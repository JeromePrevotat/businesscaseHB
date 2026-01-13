package com.humanbooster.businesscase.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.businesscase.model.Station;
import com.humanbooster.businesscase.model.User;
import com.humanbooster.businesscase.repository.StationRepository;
import com.humanbooster.businesscase.utils.ModelUtil;
import com.humanbooster.businesscase.utils.StationsUtils;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing Stations.
 * Provides methods to save, retrieve, update, and delete Stations  .
 */

@Service
@RequiredArgsConstructor
@Transactional
public class StationService{
    private final StationRepository stationRepository;

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
     * Retrieves all Stations.
     * @return a list of all Stations
     */
    @Transactional(readOnly = true)
    public List<Station> getAllStations(){
        return stationRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Station> searchStations(
            Double radius,
            Double centerLat,
            Double centerLon,
            Double maxPrice,
            LocalDate startDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
        LocalDateTime searchStart = null;
        LocalDateTime searchEnd = null;
        
        // Filter by Date only
        if (startDate != null && startTime == null && endTime == null) {
            searchStart = startDate.atStartOfDay();
            searchEnd = startDate.atTime(23, 59, 59);
        }

        // Filter by Date and Time
        else if (startDate != null && startTime != null && endTime != null) {
            searchStart = LocalDateTime.of(startDate, startTime);
            searchEnd = LocalDateTime.of(startDate, endTime);
        }

        // HQL query to search stations
        List<Station> stations = stationRepository.searchStations(
                maxPrice != null ? BigDecimal.valueOf(maxPrice) : null,
                searchStart,
                searchEnd,
                startTime != null ? startTime.toString() : null,
                endTime != null ? endTime.toString() : null
        );

        if (radius != null && centerLat != null && centerLon != null) {
            stations = stations.stream()
                    .filter(station -> {
                        double distance = StationsUtils.distanceMeters(
                                centerLat,
                                centerLon,
                                station.getLatitude().doubleValue(),
                                station.getLongitude().doubleValue()
                        );
                        return distance <= radius;
                    })
                    .toList();
        }

        return stations;
    }


    /**
     * Retrieves a Station by its ID.
     * @param id the ID of the Station to retrieve
     * @return  an Optional containing the Station if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<Station> getStationById(Long id){
        return stationRepository.findById(id);
    }

    /**
     * Deletes a Station by its ID.
     * @param id the ID of the Station to delete
     * @return an Optional containing the deleted Station if found, or empty if not found
     */
    @Transactional
    public Optional<Station> deleteStationById(Long id){
        Optional<Station> stationOpt = stationRepository.findById(id);
        stationOpt.ifPresent(stationRepository::delete);
        return stationOpt;
    }

    /**
     * Updates an existing Station.
     * @param id the ID of the Station to update
     * @param newStation the new Station data to update
     * @return an Optional containing the updated Station if found, or empty if not found
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
    public boolean existsById(Long id) {
        return stationRepository.existsById(id);
    }

    /**
     * Retrieves all Stations owned by a specific user.
     * @param owner the User who owns the stations
     * @return a list of Stations owned by the user
     */
    @Transactional(readOnly = true)
    public List<Station> getStationsByOwner(User owner) {
        return stationRepository.findByOwner(owner);
    }
}
