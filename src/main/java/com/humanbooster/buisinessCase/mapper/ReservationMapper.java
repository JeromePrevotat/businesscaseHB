package com.humanbooster.buisinessCase.mapper;

import org.springframework.stereotype.Component;

import com.humanbooster.buisinessCase.dto.ReservationDTO;
import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.repository.StationRepository;
import com.humanbooster.buisinessCase.repository.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ReservationMapper {
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    
    public ReservationDTO toDTO(Reservation reservation) {
    if (reservation == null) return null;
    return new ReservationDTO(
        reservation.getId(),
        reservation.getCreatedAt(),
        reservation.getValidatedAt(),
        reservation.getStartDate(),
        reservation.getEndDate(),
        reservation.getHourlyRateLog(),
        reservation.getState(),
        reservation.isPayed(),
        reservation.getDatePayed(),
        reservation.getUser() != null ? reservation.getUser().getId() : null,
        reservation.getStation() != null ? reservation.getStation().getId() : null
        );
    }

    public Reservation toEntity(ReservationDTO dto) {
        if (dto == null) return null;
        Reservation reservation = new Reservation();
        reservation.setId(dto.getId());
        reservation.setCreatedAt(dto.getCreatedAt());
        reservation.setValidatedAt(dto.getValidatedAt());
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        reservation.setHourlyRateLog(dto.getHourlyRateLog());
        reservation.setState(dto.getState());
        reservation.setPayed(dto.isPayed());
        reservation.setDatePayed(dto.getDatePayed());
        if (dto.getUser_id() != null) {
            User user = userRepository.findById(dto.getUser_id())
                                        .orElse(null);
            reservation.setUser(user);
        }
        if (dto.getStation_id() != null) {
            Station station = stationRepository.findById(dto.getStation_id())
                                                .orElse(null);
            reservation.setStation(station);
        }
        return reservation;
    }

}
