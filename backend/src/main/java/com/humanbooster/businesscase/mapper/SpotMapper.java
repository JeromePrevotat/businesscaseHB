package com.humanbooster.businesscase.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.humanbooster.businesscase.dto.SpotDTO;
import com.humanbooster.businesscase.model.Adress;
import com.humanbooster.businesscase.model.Media;
import com.humanbooster.businesscase.model.Spot;
import com.humanbooster.businesscase.model.Station;
import com.humanbooster.businesscase.repository.AdressRepository;
import com.humanbooster.businesscase.repository.MediaRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SpotMapper {
    private final AdressRepository adressRepository;
    private final MediaRepository mediaRepository;

    public SpotDTO toDTO(Spot spot) {
        if (spot == null) return null;
        return new SpotDTO(spot.getId(),
                    spot.getInstruction(),
                    spot.getStationList() != null ? spot.getStationList()
                                                    .stream()
                                                    .map(station -> station.getId())
                                                    .toList()
                                            : null,
                    spot.getAdress() != null ? spot.getAdress().getId() : null,
                    spot.getMediaList() != null ? spot.getMediaList()
                                                    .stream()
                                                    .map(media -> media.getId())
                                                    .toList()
                                            : null
        );
    }

    public Spot toEntity(SpotDTO dto) {
        if (dto == null) return null;
        Spot spot = new Spot();
        spot.setId(dto.getId());
        spot.setInstruction(dto.getInstruction());
        if (dto.getStationList() != null && !dto.getStationList().isEmpty()) {
            spot.setStationList(dto.getStationList()
                                    .stream()
                                    .map(stationId -> {
                                        Station station = new Station();
                                        station.setId(stationId);
                                        return station;
                                    })
                                    .toList());
        } else spot.setStationList(new ArrayList<>());
        if (dto.getAddress_id() != null) {
            Adress adress = adressRepository.findById(dto.getAddress_id())
                                            .orElse(null);
            spot.setAdress(adress);
        }
        else spot.setAdress(null);
        if (dto.getMediaList() != null && !dto.getMediaList().isEmpty()) {
            spot.setMediaList(dto.getMediaList()
                                    .stream()
                                    .map(mediaId -> {
                                        Media media = mediaRepository.findById(mediaId)
                                                                    .orElse(null);
                                        return media;
                                    })
                                    .toList());
        } else spot.setMediaList(new ArrayList<>());
        return spot;
    }

}
