package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humanbooster.buisinessCase.model.PlugType;
import com.humanbooster.buisinessCase.repository.PlugTypeRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import jakarta.transaction.Transactional;

@Service
public class PlugTypeService {
    private final PlugTypeRepository plugTypeRepository;

    @Autowired
    public PlugTypeService(PlugTypeRepository plugTypeRepository){
        this.plugTypeRepository = plugTypeRepository;
    }

    @Transactional
    public void saveAdresse(PlugType plugType){
        plugTypeRepository.save(plugType);
    }

    public List<PlugType> getAllPlugType(){
        return plugTypeRepository.findAll();
    }

    public Optional<PlugType> getPlugTypeById(long id){
        return plugTypeRepository.findById(id);
    }

    @Transactional
    public Optional<PlugType> deletePlugTypeById(long id){
        Optional<PlugType> plugTypeOpt = plugTypeRepository.findById(id);
        plugTypeOpt.ifPresent(plugTypeRepository::delete);
        return plugTypeOpt;
    }

    @Transactional
    public Optional<PlugType> updatePlugType(PlugType newPlugType, long id){
        return plugTypeRepository.findById(id)
                .map(existingPlugType -> {
                    ModelUtil.copyFields(newPlugType, existingPlugType);
                    return plugTypeRepository.save(existingPlugType);
                });
    }

}
