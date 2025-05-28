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

import com.humanbooster.buisinessCase.model.Vehicule;
import com.humanbooster.buisinessCase.service.VehiculeService;

@RestController
public class VehiculeController {
    private final VehiculeService vehiculeService;

    @Autowired
    public VehiculeController(VehiculeService vehiculeService){
        this.vehiculeService = vehiculeService;
    }

    @GetMapping("/vehicules")
    public List<Vehicule> getAllVehicules(){
        return vehiculeService.getAllVehicules();
    }

    @GetMapping("/vehicules/{id}")
    public ResponseEntity<Vehicule> getVehiculeById(@PathVariable long id){
        return vehiculeService.getVehiculeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/vehicules")
    public void saveVehicule(@RequestBody Vehicule vehicule){
        vehiculeService.saveVehicule(vehicule);
    }

    @DeleteMapping("/vehicules/{id}")
    public ResponseEntity<Vehicule> deleteVehiculeById(@PathVariable long id){
        return vehiculeService.deleteVehiculeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/vehicules/{id}")
    public ResponseEntity<Vehicule> updateVehicule(@RequestBody Vehicule newVehicule, @PathVariable long id){
        return vehiculeService.updateVehicule(newVehicule, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
