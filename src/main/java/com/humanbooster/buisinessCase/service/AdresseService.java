package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humanbooster.buisinessCase.model.Adresse;
import com.humanbooster.buisinessCase.repository.AdresseRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import jakarta.transaction.Transactional;

@Service
public class AdresseService{
    private final AdresseRepository adresseRepository;

    @Autowired
    public AdresseService(AdresseRepository adresseRepository){
        this.adresseRepository = adresseRepository;
    }

    @Transactional
    public void saveAdresse(Adresse adresse){
        adresseRepository.save(adresse);
    }

    public List<Adresse> getAllAdresses(){
        return adresseRepository.findAll();
    }

    public Optional<Adresse> getAdresseById(long id){
        return adresseRepository.findById(id);
    }

    @Transactional
    public Optional<Adresse> deleteAdresseById(long id){
        Optional<Adresse> adresseOpt = adresseRepository.findById(id);
        adresseOpt.ifPresent(adresseRepository::delete);
        return adresseOpt;
    }

    @Transactional
    public Optional<Adresse> updateAdresse(Adresse newAdresse, long id){
        return adresseRepository.findById(id)
                .map(existingAdresse -> {
                    ModelUtil.copyFields(newAdresse, existingAdresse);
                    return adresseRepository.save(existingAdresse);
                });
    }

}
