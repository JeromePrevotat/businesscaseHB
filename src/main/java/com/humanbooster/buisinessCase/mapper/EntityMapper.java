package com.humanbooster.buisinessCase.mapper;

import org.springframework.stereotype.Component;

import com.humanbooster.buisinessCase.dto.AdressDTO;
import com.humanbooster.buisinessCase.dto.HourlyRateDTO;
import com.humanbooster.buisinessCase.dto.MediaDTO;
import com.humanbooster.buisinessCase.dto.ReservationDTO;
import com.humanbooster.buisinessCase.dto.SpotDTO;
import com.humanbooster.buisinessCase.dto.StationDTO;
import com.humanbooster.buisinessCase.dto.UserDTO;
import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.model.Media;
import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.model.Spot;
import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.model.User;

/**
 * Mapper to convert Entities between DTOs and JPA Entities.
 * Prevents circular references in JSON serialization.
 */
@Component
public class EntityMapper {
    // ADRESS
    public AdressDTO toDTO(Adress adress) {
        if (adress == null) return null;
        return new AdressDTO(
            // if (adress.getId().longValue() != null) adress.getId();
            adress.getId(),
            adress.getAdressName(),
            adress.getStreetNumber(),
            adress.getStreetName(),
            adress.getZipcode(),
            adress.getCity(),
            adress.getCountry(),
            adress.getRegion(),
            adress.getAddendum(),
            adress.getFloor()
            
            // adress.getUser() != null ? adress.getUser().getId() : 0, // Reference to User ID
            
        );
    }

    public Adress toEntity(AdressDTO dto) {
        if (dto == null) return null;
        Adress adress = new Adress();
        adress.setId(dto.getId());
        adress.setAdressName(dto.getAdressName());
        adress.setStreetNumber(dto.getStreetNumber());
        adress.setStreetName(dto.getStreetName());
        adress.setZipcode(dto.getZipcode());
        adress.setCity(dto.getCity());
        adress.setCountry(dto.getCountry());
        adress.setRegion(dto.getRegion());
        adress.setAddendum(dto.getAddendum());
        adress.setFloor(dto.getFloor());
        // adress.setUser(toEntity(dto.getUserId()));
        return adress;
    }

    // SPOT
    public SpotDTO toDTO(Spot spot) {
        if (spot == null) return null;
        return new SpotDTO(spot.getId(), spot.getInstruction());
    }

    public Spot toEntity(SpotDTO dto) {
        if (dto == null) return null;
        Spot spot = new Spot();
        spot.setId(dto.getId());
        spot.setInstruction(dto.getInstruction());
        return spot;
    }

    // STATION
    public StationDTO toDTO(Station station) {
        if (station == null) return null;
        return new StationDTO(
            station.getId(),
            station.getStationName(),
            station.getLatitude(),
            station.getLongitude(),
            station.getPowerOutput(),
            station.getInstruction(),
            station.isGrounded(),
            station.getState(),
            station.isBusy(),
            station.getCreationDate(),
            station.getLastMaintenance(),
            station.isWired(),
            toDTO(station.getSpot())
        );
    }

    public Station toEntity(StationDTO dto) {
        if (dto == null) return null;
        Station station = new Station();
        station.setId(dto.getId());
        station.setStationName(dto.getStationName());
        station.setLatitude(dto.getLatitude());
        station.setLongitude(dto.getLongitude());
        station.setPowerOutput(dto.getPowerOutput());
        station.setInstruction(dto.getInstruction());
        station.setGrounded(dto.isGrounded());
        station.setState(dto.getState());
        station.setBusy(dto.isBusy());
        station.setCreationDate(dto.getCreationDate());
        station.setLastMaintenance(dto.getLastMaintenance());
        station.setWired(dto.isWired());
        // station.setAdress(toEntity(dto.getAdress()));
        station.setSpot(toEntity(dto.getSpot()));
        return station;
    }

    // MEDIA
    public MediaDTO toDTO(Media media) {
        if (media == null) return null;
        return new MediaDTO(
            media.getId(),
            media.getMediaName(),
            media.getType(),
            media.getUrl(),
            media.getDescription(),
            null
        );
    }

    public Media toEntity(MediaDTO dto) {
        if (dto == null) return null;
        Media media = new Media();
        media.setId(dto.getId());
        media.setMediaName(dto.getMediaName());
        media.setType(dto.getType());
        media.setUrl(dto.getUrl());
        media.setDescription(dto.getDescription());
        return media;
    }

    // RESERVATION
    public ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) return null;
        return new ReservationDTO(
            reservation.getId(),
            reservation.getStartDate(),
            reservation.getEndDate(),
            reservation.getState(),
            reservation.getPricePayed(),
            reservation.getDatePayed(),
            reservation.getHourlyRateLog(),
            reservation.getUser() != null ? reservation.getUser().getId() : null,
            reservation.getStation() != null ? reservation.getStation().getId() : null
        );
    }

    public Reservation toEntity(ReservationDTO dto) {
        if (dto == null) return null;
        Reservation reservation = new Reservation();
        reservation.setId(dto.getId());
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        reservation.setState(dto.getState());
        reservation.setPricePayed(dto.getPricePayed());
        reservation.setDatePayed(dto.getDatePayed());
        reservation.setHourlyRateLog(dto.getHourlyRateLog());
        reservation.setUser(new User(dto.getUserId()));
        reservation.setStation(new Station(dto.getStationId()));
        return reservation;
    }

    // HOURLY RATE
    public HourlyRateDTO toDTO(HourlyRate rate) {
        if (rate == null) return null;
        return new HourlyRateDTO(
            rate.getId(),
            rate.getHourlyRate(),
            rate.getStartTime(),
            rate.getEndTime(),
            rate.getWeekDay(),
            rate.getStartDate(),
            rate.getEndDate(),
            rate.getActive(),
            rate.getStation() != null ? rate.getStation().getId() : null
        );
    }

    public HourlyRate toEntity(HourlyRateDTO dto) {
        if (dto == null) return null;
        HourlyRate rate = new HourlyRate();
        rate.setId(dto.getId());
        rate.setHourlyRate(dto.getHourlyRate());
        rate.setStartTime(dto.getStartTime());
        rate.setEndTime(dto.getEndTime());
        rate.setWeekDay(dto.getWeekDay());
        rate.setStartDate(dto.getStartDate());
        rate.setEndDate(dto.getEndDate());
        rate.setActive(dto.getActive());
        // Note: La référence Station doit être résolue par le service
        return rate;
    }

    // USER
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
            user.getId(),
            user.getUserName(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole(),
            user.getEmail(),
            user.getBirthDate(),
            user.getIban(),
            // user.getVehicule(),
            user.isBanned(),
            user.isAccountValid(),
            user.getInscriptionDate(),
            toDTO(user.getSpot())
        );
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUserName(dto.getUserName());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());
        user.setEmail(dto.getEmail());
        user.setBirthDate(dto.getBirthDate());
        user.setIban(dto.getIban());
        // user.setVehicule(dto.getVehicule());
        user.setBanned(dto.getBanned());
        user.setAccountValid(dto.getAccountValid());
        user.setInscriptionDate(dto.getInscriptionDate());
        user.setSpot(toEntity(dto.getSpot()));
        return user;
    }
}
