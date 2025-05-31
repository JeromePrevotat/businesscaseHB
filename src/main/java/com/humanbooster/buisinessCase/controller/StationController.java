package com.humanbooster.buisinessCase.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.service.StationService;

@RestController

public class StationController {
    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService){
        this.stationService = stationService;
    }

    @GetMapping("/stations")
    public List<Station> getAllStations(){
        return stationService.getAllStations();
    }

    @GetMapping("/stations/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable long id){
        return stationService.getStationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/stations")
    public void saveStation(@RequestBody Station station){
        stationService.saveStation(station);
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Station> deleteStationById(@PathVariable long id){
        return stationService.deleteStationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/stations/{id}")
    public ResponseEntity<Station> updateStation(@RequestBody Station newStation, @PathVariable long id){
        return stationService.updateStation(newStation, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
