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

import com.humanbooster.buisinessCase.model.Lieu;
import com.humanbooster.buisinessCase.service.LieuService;

@RestController
public class LieuController {
    private final LieuService lieuService;

    @Autowired
    public LieuController(LieuService lieuService){
        this.lieuService = lieuService;
    }

    @GetMapping("/lieux")
    public List<Lieu> getAllLieus(){
        return lieuService.getAllLieus();
    }

    @GetMapping("/lieux/{id}")
    public ResponseEntity<Lieu> getLieuById(@PathVariable long id){
        return lieuService.getLieuById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/lieux")
    public void saveLieu(@RequestBody Lieu lieu){
        lieuService.saveLieu(lieu);
    }

    @DeleteMapping("/lieux/{id}")
    public ResponseEntity<Lieu> deleteLieuById(@PathVariable long id){
        return lieuService.deleteLieuById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/lieux/{id}")
    public ResponseEntity<Lieu> updateLieu(@RequestBody Lieu newLieu, @PathVariable long id){
        return lieuService.updateLieu(newLieu, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
