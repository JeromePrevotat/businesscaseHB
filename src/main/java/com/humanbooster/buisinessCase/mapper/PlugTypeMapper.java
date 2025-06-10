package com.humanbooster.buisinessCase.mapper;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.humanbooster.buisinessCase.dto.PlugTypeDTO;
import com.humanbooster.buisinessCase.model.PlugType;
import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.model.Vehicule;
import com.humanbooster.buisinessCase.repository.VehiculeRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PlugTypeMapper {
    private final VehiculeRepository vehiculeRepository;
    
    public PlugTypeDTO toDTO(PlugType plugType) {
        if (plugType == null) return null;
        return new PlugTypeDTO(
            plugType.getId(),
            plugType.getPlugname(),
            plugType.getVehiculeList() != null ? plugType.getVehiculeList()
                                                    .stream()
                                                    .map(vehicule -> vehicule.getId())
                                                    .toList()
                                            : null,
            plugType.getStationList() != null ? plugType.getStationList()
                                                    .stream()
                                                    .map(station -> station.getId())
                                                    .toList()
                                            : null
        );
    }

    public PlugType toEntity(PlugTypeDTO dto) {
        if (dto == null) return null;
        PlugType plugType = new PlugType();
        plugType.setId(dto.getId());
        plugType.setPlugname(dto.getPlugname());
        if (dto.getVehicule_id() != null && !dto.getVehicule_id().isEmpty()) {
            plugType.setVehiculeList(new HashSet<>(
                dto.getVehicule_id()
                    .stream()
                    .map(id -> {
                        Vehicule vehicule = vehiculeRepository.findById(id)
                                                                .orElse(null);
                        return vehicule;
                    })
                    .toList()));
        } else plugType.setVehiculeList(new HashSet<>());
        if (dto.getStation_id() != null && !dto.getStation_id().isEmpty()) {
            plugType.setStationList(new HashSet<>(
                dto.getStation_id()
                    .stream()
                    .map(id -> {
                        Station station = new Station();
                        station.setId(id);
                        return station;
                    })
                    .toList()));
        } else plugType.setStationList(new HashSet<>());
        return plugType;
    }

}
