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

import com.humanbooster.buisinessCase.model.Spot;
import com.humanbooster.buisinessCase.service.SpotService;

@RestController
public class SpotController {
    private final SpotService spotService;

    @Autowired
    public SpotController(SpotService spotService){
        this.spotService = spotService;
    }

    @GetMapping("/spotx")
    public List<Spot> getAllSpots(){
        return spotService.getAllSpots();
    }

    @GetMapping("/spotx/{id}")
    public ResponseEntity<Spot> getSpotById(@PathVariable long id){
        return spotService.getSpotById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/spotx")
    public void saveSpot(@RequestBody Spot spot){
        spotService.saveSpot(spot);
    }

    @DeleteMapping("/spotx/{id}")
    public ResponseEntity<Spot> deleteSpotById(@PathVariable long id){
        return spotService.deleteSpotById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/spotx/{id}")
    public ResponseEntity<Spot> updateSpot(@RequestBody Spot newSpot, @PathVariable long id){
        return spotService.updateSpot(newSpot, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
