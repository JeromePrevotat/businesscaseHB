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

import com.humanbooster.buisinessCase.model.PlugType;
import com.humanbooster.buisinessCase.service.PlugTypeService;

@RestController
public class PlugTypeController {
    private final PlugTypeService plugTypeService;

    @Autowired
    public PlugTypeController(PlugTypeService plugTypeService){
        this.plugTypeService = plugTypeService;
    }

    @GetMapping("/plugTypes")
    public List<PlugType> getAllPlugTypes(){
        return plugTypeService.getAllPlugTypes();
    }

    @GetMapping("/plugTypes/{id}")
    public ResponseEntity<PlugType> getPlugTypeById(@PathVariable Long id){
        return plugTypeService.getPlugTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/plugTypes")
    public void savePlugType(@RequestBody PlugType plugType){
        plugTypeService.savePlugType(plugType);
    }

    @DeleteMapping("/plugTypes/{id}")
    public ResponseEntity<PlugType> deletePlugTypeById(@PathVariable Long id){
        return plugTypeService.deletePlugTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/plugTypes/{id}")
    public ResponseEntity<PlugType> updatePlugType(@RequestBody PlugType newPlugType, @PathVariable Long id){
        return plugTypeService.updatePlugType(newPlugType, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
