package com.humanbooster.businesscase.mapper;

import org.springframework.stereotype.Component;

import com.humanbooster.businesscase.dto.MediaDTO;
import com.humanbooster.businesscase.model.Media;
import com.humanbooster.businesscase.model.Spot;
import com.humanbooster.businesscase.model.Station;
import com.humanbooster.businesscase.model.User;
import com.humanbooster.businesscase.repository.SpotRepository;
import com.humanbooster.businesscase.repository.StationRepository;
import com.humanbooster.businesscase.repository.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MediaMapper {
    private final UserRepository userRepository;
    private final SpotRepository spotRepository;
    private final StationRepository stationRepository;

    public MediaDTO toDTO(Media media) {
        if (media == null) return null;
        return new MediaDTO(
            media.getId(),
            media.getUrl(),
            media.getType(),
            media.getMediaName(),
            media.getSize(),
            media.getUser() != null ? media.getUser().getId() : null,
            media.getSpot() != null ? media.getSpot().getId() : null,
            media.getStation() != null ? media.getStation().getId() : null
        );
    }

    public Media toEntity(MediaDTO dto) {
        if (dto == null) return null;
        Media media = new Media();
        media.setId(dto.getId());
        media.setUrl(dto.getUrl());
        media.setType(dto.getType());
        media.setMediaName(dto.getMediaName());
        media.setSize(dto.getSize());
        if (dto.getUser_id() != null) {
            User user = userRepository.findById(dto.getUser_id())
                                        .orElse(null);
            media.setUser(user);
        }
        if (dto.getSpot_id() != null) {
            Spot spot = spotRepository.findById(dto.getSpot_id())
                                        .orElse(null);
            media.setSpot(spot);
        }
        if (dto.getStation_id() != null) {
            Station station = stationRepository.findById(dto.getStation_id())
                                                .orElse(null);
            media.setStation(station);
        }
        return media;
    }

}
