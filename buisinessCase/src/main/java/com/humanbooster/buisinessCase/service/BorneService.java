package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humanbooster.buisinessCase.model.Borne;
import com.humanbooster.buisinessCase.repository.BorneRepository;

import com.humanbooster.buisinessCase.utils.ModelUtil;
import jakarta.transaction.Transactional;

@Service
public class BorneService{
    private final BorneRepository borneRepository;

    @Autowired
    public BorneService(BorneRepository borneRepository){
        this.borneRepository = borneRepository;
    }

    @Transactional
    public void saveBorne(Borne borne){
        borneRepository.save(borne);
    }

    public List<Borne> getAllBornes(){
        return borneRepository.findAll();
    }

    public Optional<Borne> getBorneById(long id){
        return borneRepository.findById(id);
    }

    @Transactional
    public Optional<Borne> deleteBorneById(long id){
        Optional<Borne> borneOpt = borneRepository.findById(id);
        borneOpt.ifPresent(borneRepository::delete);
        return borneOpt;
    }

    @Transactional
    public Optional<Borne> updateBorne(Borne newBorne, long id){
        return borneRepository.findById(id)
                .map(existingBorne -> {
                    ModelUtil.copyFields(newBorne, existingBorne);
                    return borneRepository.save(existingBorne);
                });
    }

}
