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

import com.humanbooster.buisinessCase.model.Borne;
import com.humanbooster.buisinessCase.service.BorneService;

@RestController

public class BorneController {
    private final BorneService borneService;

    @Autowired
    public BorneController(BorneService borneService){
        this.borneService = borneService;
    }

    @GetMapping("/bornes")
    public List<Borne> getAllBornes(){
        return borneService.getAllBornes();
    }

    @GetMapping("/bornes/{id}")
    public ResponseEntity<Borne> getBorneById(@PathVariable long id){
        return borneService.getBorneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/bornes")
    public void saveBorne(@RequestBody Borne borne){
        borneService.saveBorne(borne);
    }

    @DeleteMapping("/bornes/{id}")
    public ResponseEntity<Borne> deleteBorneById(@PathVariable long id){
        return borneService.deleteBorneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/bornes/{id}")
    public ResponseEntity<Borne> updateBorne(@RequestBody Borne newBorne, @PathVariable long id){
        return borneService.updateBorne(newBorne, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
