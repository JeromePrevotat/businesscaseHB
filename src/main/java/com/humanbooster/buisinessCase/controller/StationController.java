package com.humanbooster.buisinessCase.controller;

import java.util.List;

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

import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.service.StationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing Stations.
 * Provides endpoints to retrieve, create, update, and delete Stations.
 */
@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;

    /**
     * Retrieves all Stations.
     * GET /api/stations
     * @return a list of all Stations
     */
    @GetMapping
    public ResponseEntity<List<Station>> getAllStations(){
        return ResponseEntity.ok(stationService.getAllStations());
    }

/**
     * Retrieves a Station by its ID.
     * GET /api/stations/{id}
     * @param id the ID of the Station to retrieve
     * @return the Station with the specified ID, or 404 Not Found if it does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable Long id){
        return stationService.getStationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Saves a new Station.
     * POST /api/stations
     * @param station the Station to save
     * @return the saved Station with HTTP status 201 Created
     */
    @PostMapping
    public ResponseEntity<Station> saveStation(@Valid @RequestBody Station station){
        return ResponseEntity.status(HttpStatus.CREATED).body(stationService.saveStation(station));
    }

    /**
     * Deletes a Station by its ID.
     * DELETE /api/stations/{id}
     * @param id the ID of the Station to delete
     * @return 200 OK if the deletion was successful, or 404 Not Found if the Station does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStationById(@PathVariable Long id){
        if (!stationService.existsById(id)) return ResponseEntity.notFound().build();
        
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     *  Updates an existing Station.
     *  PUT /api/stations/{id}
     * @param id the ID of the Station to update
     * @param newStation the updated Station data
     * @return the updated Station with HTTP status 200 OK, or 404 Not Found if the Station does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Station> updateStation(@PathVariable Long id, @Valid @RequestBody Station newStation){
        return stationService.updateStation(id, newStation)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
