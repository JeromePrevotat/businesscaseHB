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

import com.humanbooster.buisinessCase.model.Utilisateur;
import com.humanbooster.buisinessCase.service.UtilisateurService;

@RestController
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService){
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/utilisateurs")
    public List<Utilisateur> getAllUtilisateurs(){
        return utilisateurService.getAllUtilisateurs();
    }

    @GetMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable long id){
        return utilisateurService.getUtilisateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/utilisateurs")
    public void saveUtilisateur(@RequestBody Utilisateur utilisateur){
        utilisateurService.saveUtilisateur(utilisateur);
    }

    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> deleteUtilisateurById(@PathVariable long id){
        return utilisateurService.deleteUtilisateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@RequestBody Utilisateur newUtilisateur, @PathVariable long id){
        return utilisateurService.updateUtilisateur(newUtilisateur, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
