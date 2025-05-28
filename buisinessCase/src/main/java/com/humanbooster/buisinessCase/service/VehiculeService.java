package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humanbooster.buisinessCase.model.Vehicule;
import com.humanbooster.buisinessCase.repository.VehiculeRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import jakarta.transaction.Transactional;

@Service
public class VehiculeService {
    private final VehiculeRepository vehiculeRepository;

    @Autowired
    public VehiculeService(VehiculeRepository vehiculeRepository){
        this.vehiculeRepository = vehiculeRepository;
    }

    @Transactional
    public void saveVehicule(Vehicule vehicule){
        vehiculeRepository.save(vehicule);
    }

    public List<Vehicule> getAllVehicules(){
        return vehiculeRepository.findAll();
    }

    public Optional<Vehicule> getVehiculeById(long id){
        return vehiculeRepository.findById(id);
    }

    @Transactional
    public Optional<Vehicule> deleteVehiculeById(long id){
        Optional<Vehicule> vehiculeOpt = vehiculeRepository.findById(id);
        vehiculeOpt.ifPresent(vehiculeRepository::delete);
        return vehiculeOpt;
    }

    @Transactional
    public Optional<Vehicule> updateVehicule(Vehicule newVehicule, long id){
        return vehiculeRepository.findById(id)
                .map(existingVehicule -> {
                    ModelUtil.copyFields(newVehicule, existingVehicule);
                    return vehiculeRepository.save(existingVehicule);
                });
    }

}
