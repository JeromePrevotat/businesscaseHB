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

import com.humanbooster.buisinessCase.model.Adresse;
import com.humanbooster.buisinessCase.service.AdresseService;

@RestController
public class AdresseController {
    private final AdresseService adresseService;

    @Autowired
    public AdresseController(AdresseService adresseService){
        this.adresseService = adresseService;
    }

    @GetMapping("/adresses")
    public List<Adresse> getAllAdresses(){
        return adresseService.getAllAdresses();
    }

    @GetMapping("/adresses/{id}")
    public ResponseEntity<Adresse> getAdresseById(@PathVariable long id){
        return adresseService.getAdresseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/adresses")
    public void saveAdresse(@RequestBody Adresse adresse){
        adresseService.saveAdresse(adresse);
    }

    @DeleteMapping("/adresses/{id}")
    public ResponseEntity<Adresse> deleteAdresseById(@PathVariable long id){
        return adresseService.deleteAdresseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/adresses/{id}")
    public ResponseEntity<Adresse> updateAdresse(@RequestBody Adresse newAdresse, @PathVariable long id){
        return adresseService.updateAdresse(newAdresse, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
