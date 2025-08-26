package com.humanbooster.businesscase.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.humanbooster.businesscase.dto.StationDTO;
import com.humanbooster.businesscase.model.Media;
import com.humanbooster.businesscase.model.PlugType;
import com.humanbooster.businesscase.model.Reservation;
import com.humanbooster.businesscase.model.Spot;
import com.humanbooster.businesscase.model.Station;
import com.humanbooster.businesscase.model.User;
import com.humanbooster.businesscase.repository.MediaRepository;
import com.humanbooster.businesscase.repository.PlugTypeRepository;
import com.humanbooster.businesscase.repository.SpotRepository;
import com.humanbooster.businesscase.repository.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class StationMapper {
    private final SpotRepository spotRepository;
    private final MediaRepository mediaRepository;
    private final PlugTypeRepository plugTypeRepository;
    private final UserRepository userRepository;

    
    public StationDTO toDTO(Station station) {
        if (station == null) return null;
        return new StationDTO(
            station.getId(),
            station.getStationName(),
            station.getLatitude(),
            station.getLongitude(),
            station.getPriceRate(),
            station.getPowerOutput(),
            station.getManual(),
            station.getState(),
            station.isGrounded(),
            station.isBusy(),
            station.isWired(),
            station.getSpot() != null ? station.getSpot().getId() : null,
            station.getOwner() != null ? station.getOwner().getId() : null,
            station.getReservationList() != null ? station.getReservationList()
                                                    .stream()
                                                    .map(reservation -> reservation.getId())
                                                    .toList()
                                            : null,
            station.getMediaList() != null ? station.getMediaList()
                                                    .stream()
                                                    .map(media -> media.getId())
                                                    .toList()
                                            : null,
            station.getPlugType() != null ? station.getPlugType()
                                                    .stream()
                                                    .map(plugType -> plugType.getId())
                                                    .toList()
                                            : null
        );
    }

    public Station toEntity(StationDTO dto) {
        if (dto == null) return null;
        Station station = new Station();
        station.setId(dto.getId());
        station.setStationName(dto.getStationName());
        station.setLatitude(dto.getLatitude());
        station.setLongitude(dto.getLongitude());
        station.setPriceRate(dto.getPriceRate());
        station.setPowerOutput(dto.getPowerOutput());
        station.setManual(dto.getManual());
        station.setState(dto.getState());
        station.setGrounded(dto.isGrounded());
        station.setBusy(dto.isBusy());
        station.setWired(dto.isWired());
        if (dto.getSpot_id() != null) {
            Spot spot = spotRepository.findById(dto.getSpot_id())
                                        .orElse(null);
            station.setSpot(spot);
        } else station.setSpot(null);
        if (dto.getOwner_id() != null) {
            User owner = userRepository.findById(dto.getOwner_id())
                                        .orElse(null);
            station.setOwner(owner);
        } else station.setOwner(null);
        if (dto.getReservationList() != null && !dto.getReservationList().isEmpty()) {
            station.setReservationList(dto.getReservationList()
                                            .stream()
                                            .map(reservationId -> {
                                                Reservation reservation = new Reservation();
                                                reservation.setId(reservationId);
                                                return reservation;
                                            })
                                            .toList());
        } else station.setReservationList(new ArrayList<>());
        if (dto.getMediaList() != null && !dto.getMediaList().isEmpty()) {
            station.setMediaList(dto.getMediaList()
                                        .stream()
                                        .map(mediaId -> {
                                            Media media = mediaRepository.findById(mediaId)
                                                                        .orElse(null);
                                            return media;
                                        })
                                        .toList());
        } else station.setMediaList(new ArrayList<>());
        if (dto.getPlugTypeList() != null && !dto.getPlugTypeList().isEmpty()) {
            station.setPlugType(dto.getPlugTypeList()
                                    .stream()
                                    .map(plugTypeId -> {
                                        PlugType plugType = plugTypeRepository.findById(plugTypeId)
                                                                                .orElse(null);
                                        return plugType;
                                    })
                                    .toList());
        } else station.setPlugType(new ArrayList<>());
        return station;
    }

}
