package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humanbooster.buisinessCase.model.Utilisateur;
import com.humanbooster.buisinessCase.repository.UtilisateurRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import jakarta.transaction.Transactional;

@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository){
        this.utilisateurRepository = utilisateurRepository;
    }

    @Transactional
    public void saveUtilisateur(Utilisateur utilisateur){
        utilisateurRepository.save(utilisateur);
    }

    public List<Utilisateur> getAllUtilisateur(){
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUtilisateurById(long id){
        return utilisateurRepository.findById(id);
    }

    @Transactional
    public Optional<Utilisateur> deleteUtilisateurById(long id){
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(id);
        utilisateurOpt.ifPresent(utilisateurRepository::delete);
        return utilisateurOpt;
    }

    @Transactional
    public Optional<Utilisateur> updateUtilisateur(Utilisateur newUtilisateur, long id){
        return utilisateurRepository.findById(id)
                .map(existingUtilisateur -> {
                    ModelUtil.copyFields(newUtilisateur, existingUtilisateur);
                    return utilisateurRepository.save(existingUtilisateur);
                });
    }

}
