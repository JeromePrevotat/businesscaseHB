package com.humanbooster.businesscase.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humanbooster.businesscase.dto.StationDTO;
import com.humanbooster.businesscase.mapper.StationMapper;
import com.humanbooster.businesscase.model.Station;
import com.humanbooster.businesscase.model.User;
import com.humanbooster.businesscase.service.StationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing Station entities.
 * Provides endpoints for creating, retrieving, updating, and deleting stations.
 */
@RestController
@RestControllerAdvice
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;
    private final StationMapper mapper;


    /**
     * Get all stations.
     * GET /api/stations
     * @return ResponseEntity with the list of stations
     */
    @GetMapping
    public ResponseEntity<List<StationDTO>> getAllStations(){
        List<StationDTO> stationDTOs = stationService.getAllStations().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stationDTOs);
    }

    @GetMapping("/search")
    public List<Station> searchStations(
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime
    ) {
        return stationService.searchStations(radius, lat, lon, maxPrice, startDate, startTime, endTime);
    }

    /**
     * Get a station by ID.
     * GET /api/stations/{id}
     * @param id The ID of the station to retrieve
     * @return ResponseEntity with the station if found, or 404 Not Found if not found
     */
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<StationDTO> getStationById(@PathVariable Long id){
        return stationService.getStationById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new station.
     * POST /api/stations
     * @param station The station entity to be saved
     * @return ResponseEntity with the saved station and 201 Created status
     */
    @PostMapping
    public ResponseEntity<StationDTO> saveStation(
        @Valid @RequestBody StationDTO stationDTO,
        @AuthenticationPrincipal UserDetails userDetails){
        User user = (User) userDetails;
        Station newStation = mapper.toEntity(stationDTO);
        newStation.setOwner(user);
        Station savedStation = stationService.saveStation(newStation);
        StationDTO savedStationDTO = mapper.toDTO(savedStation);
        // Conform RESTful practices, we should return a URI to the created resource.
        // URI location = URI.create("/api/stations/" + savedStation.getId());
        // return ResponseEntity.created(location).body(savedStation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStationDTO);
    }

    /**
     * Delete a station by ID.
     * DELETE /api/stations/{id}
     * @param id The ID of the station to delete
     * @return ResponseEntity with the 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStationById(@PathVariable Long id){
        return stationService.deleteStationById(id).isPresent() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Update a station by ID.
     * PUT /api/stations/{id}
     * @param id The ID of the station to update
     * @param newStation The updated station entity
     * @return ResponseEntity with the updated station if found, or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<StationDTO> updateStation(@PathVariable Long id, @Valid @RequestBody StationDTO newStationDTO){
        return stationService.updateStation(id, mapper.toEntity(newStationDTO))
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

