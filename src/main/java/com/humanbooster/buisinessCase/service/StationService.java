package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.repository.StationRepository;

import com.humanbooster.buisinessCase.utils.ModelUtil;
import jakarta.transaction.Transactional;

@Service
public class StationService{
    private final StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository){
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void saveStation(Station station){
        stationRepository.save(station);
    }

    public List<Station> getAllStations(){
        return stationRepository.findAll();
    }

    public Optional<Station> getStationById(long id){
        return stationRepository.findById(id);
    }

    @Transactional
    public Optional<Station> deleteStationById(long id){
        Optional<Station> stationOpt = stationRepository.findById(id);
        stationOpt.ifPresent(stationRepository::delete);
        return stationOpt;
    }

    @Transactional
    public Optional<Station> updateStation(Station newStation, long id){
        return stationRepository.findById(id)
                .map(existingStation -> {
                    ModelUtil.copyFields(newStation, existingStation);
                    return stationRepository.save(existingStation);
                });
    }

}
