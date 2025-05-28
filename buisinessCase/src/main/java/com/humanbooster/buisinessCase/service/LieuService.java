package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humanbooster.buisinessCase.model.Lieu;
import com.humanbooster.buisinessCase.repository.LieuRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import jakarta.transaction.Transactional;

@Service
public class LieuService {
    private final LieuRepository lieuRepository;

    @Autowired
    public LieuService(LieuRepository lieuRepository){
        this.lieuRepository = lieuRepository;
    }

    @Transactional
    public void saveLieu(Lieu lieu){
        lieuRepository.save(lieu);
    }

    public List<Lieu> getAllLieus(){
        return lieuRepository.findAll();
    }

    public Optional<Lieu> getLieuById(long id){
        return lieuRepository.findById(id);
    }

    @Transactional
    public Optional<Lieu> deleteLieuById(long id){
        Optional<Lieu> lieuOpt = lieuRepository.findById(id);
        lieuOpt.ifPresent(lieuRepository::delete);
        return lieuOpt;
    }

    @Transactional
    public Optional<Lieu> updateLieu(Lieu newLieu, long id){
        return lieuRepository.findById(id)
                .map(existingLieu -> {
                    ModelUtil.copyFields(newLieu, existingLieu);
                    return lieuRepository.save(existingLieu);
                });
    }

}