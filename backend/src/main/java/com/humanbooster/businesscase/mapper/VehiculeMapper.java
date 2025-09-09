package com.humanbooster.businesscase.mapper;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.humanbooster.businesscase.dto.VehiculeDTO;
import com.humanbooster.businesscase.model.PlugType;
import com.humanbooster.businesscase.model.User;
import com.humanbooster.businesscase.model.Vehicule;
import com.humanbooster.businesscase.repository.PlugTypeRepository;
import com.humanbooster.businesscase.repository.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class VehiculeMapper {
    private final UserRepository userRepository;
    private final PlugTypeRepository plugTypeRepository;
    
    public VehiculeDTO toDTO(Vehicule vehicule) {
        if (vehicule == null) return null;
        return new VehiculeDTO(
            vehicule.getId(),
            vehicule.getPlate(),
            vehicule.getBrand(),
            vehicule.getBatteryCapacity(),
            vehicule.getUser() != null ? vehicule.getUser()
                                                    .stream()
                                                    .map(user -> user.getId())
                                                    .toList()
                                            : null,
            vehicule.getPlugType() != null ? vehicule.getPlugType()
                                                    .stream()
                                                    .map(plugType -> plugType.getId())
                                                    .toList()
                                            : null
        );
    }

    public Vehicule toEntity(VehiculeDTO dto) {
        if (dto == null) return null;
        Vehicule vehicule = new Vehicule();
        vehicule.setId(dto.getId());
        vehicule.setPlate(dto.getPlate());
        vehicule.setBrand(dto.getBrand());
        vehicule.setBatteryCapacity(dto.getBatteryCapacity());
        if (dto.getUserList() != null && !dto.getUserList().isEmpty()) {
            vehicule.setUser(new HashSet<>(
                dto.getUserList()
                    .stream()
                    .map(id -> {
                        User user = userRepository.findById(id)
                                                    .orElse(null);
                        return user;
                    })
                    .toList()));
        } else vehicule.setUser(new HashSet<>());
        if (dto.getPlugTypeList() != null && !dto.getPlugTypeList().isEmpty()) {
            vehicule.setPlugType(new HashSet<>(
                dto.getPlugTypeList()
                    .stream()
                    .map(id -> {
                        PlugType plugType = plugTypeRepository.findById(id)
                                                                .orElse(null);
                        return plugType;
                    })
                    .toList()));
        } else vehicule.setPlugType(new HashSet<>());
        return vehicule;
    }

}
