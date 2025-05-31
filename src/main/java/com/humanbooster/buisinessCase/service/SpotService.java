package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humanbooster.buisinessCase.model.Spot;
import com.humanbooster.buisinessCase.repository.SpotRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import jakarta.transaction.Transactional;

@Service
public class SpotService {
    private final SpotRepository spotRepository;

    @Autowired
    public SpotService(SpotRepository spotRepository){
        this.spotRepository = spotRepository;
    }

    @Transactional
    public void saveSpot(Spot spot){
        spotRepository.save(spot);
    }

    public List<Spot> getAllSpots(){
        return spotRepository.findAll();
    }

    public Optional<Spot> getSpotById(long id){
        return spotRepository.findById(id);
    }

    @Transactional
    public Optional<Spot> deleteSpotById(long id){
        Optional<Spot> spotOpt = spotRepository.findById(id);
        spotOpt.ifPresent(spotRepository::delete);
        return spotOpt;
    }

    @Transactional
    public Optional<Spot> updateSpot(Spot newSpot, long id){
        return spotRepository.findById(id)
                .map(existingSpot -> {
                    ModelUtil.copyFields(newSpot, existingSpot);
                    return spotRepository.save(existingSpot);
                });
    }

}