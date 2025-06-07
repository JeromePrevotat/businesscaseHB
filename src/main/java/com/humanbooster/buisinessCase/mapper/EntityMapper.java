package com.humanbooster.buisinessCase.mapper;

import org.springframework.stereotype.Component;

import com.humanbooster.buisinessCase.dto.AdressDTO;
import com.humanbooster.buisinessCase.dto.MediaDTO;
import com.humanbooster.buisinessCase.dto.PlugTypeDTO;
import com.humanbooster.buisinessCase.dto.ReservationDTO;
import com.humanbooster.buisinessCase.dto.SpotDTO;
import com.humanbooster.buisinessCase.dto.StationDTO;
import com.humanbooster.buisinessCase.dto.UserDTO;
import com.humanbooster.buisinessCase.dto.VehiculeDTO;
import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.model.Media;
import com.humanbooster.buisinessCase.model.PlugType;
import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.model.Spot;
import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.model.Vehicule;

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
            adress.getId(),
            adress.getAdressname(),
            adress.getStreetnumber(),
            adress.getStreetname(),
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
        adress.setAdressname(dto.getAdressname());
        adress.setStreetnumber(dto.getStreetnumber());
        adress.setStreetname(dto.getStreetname());
        adress.setZipcode(dto.getZipcode());
        adress.setCity(dto.getCity());
        adress.setCountry(dto.getCountry());
        adress.setRegion(dto.getRegion());
        adress.setAddendum(dto.getAddendum());
        adress.setFloor(dto.getFloor());
        return adress;
    }

    // SPOT
    public SpotDTO toDTO(Spot spot) {
        if (spot == null) return null;
        return new SpotDTO(spot.getId(),
                    spot.getInstruction(),
                    spot.getAddress() != null ? spot.getAddress().getId() : null);
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
            station.getPriceRate(),
            station.getPowerOutput(),
            station.getManual(),
            station.getState(),
            station.isGrounded(),
            station.isBusy(),
            station.isWired(),
            station.getSpot_id() != null ? station.getSpot_id().getId() : null
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
        return station;
    }

    // MEDIA
    public MediaDTO toDTO(Media media) {
        if (media == null) return null;
        return new MediaDTO(
            media.getId(),
            media.getUrl(),
            media.getType(),
            media.getMediaName(),
            media.getSize(),
            media.getUser() != null ? media.getUser().getId() : null
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
        return media;
    }

    // RESERVATION
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
            reservation.getUser_id() != null ? reservation.getUser_id().getId() : null,
            reservation.getStation_id() != null ? reservation.getStation_id().getId() : null
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
        return reservation;
    }

    // USER
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getFirstname(),
            user.getLastname(),
            user.getEmail(),
            user.getBirthDate(),
            user.getInscriptionDate(),
            user.isAccountValid(),
            user.getRole(),
            user.isBanned(),
            user.getMedia() != null ? user.getMedia().getId() : null
        );
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setBirthDate(dto.getBirthDate());
        user.setInscriptionDate(dto.getInscriptionDate());
        user.setAccountValid(dto.getAccountValid());
        user.setRole(dto.getRole());
        user.setBanned(dto.getBanned());
        return user;
    }

    // PLUG TYPE
    public PlugTypeDTO toDTO(PlugType plugType) {
        if (plugType == null) return null;
        return new PlugTypeDTO(
            plugType.getId(),
            plugType.getPlugname()
        );
    }

    public PlugType toEntity(PlugTypeDTO dto) {
        if (dto == null) return null;
        PlugType plugType = new PlugType();
        plugType.setId(dto.getId());
        plugType.setPlugname(dto.getPlugname());
        return plugType;
    }

    // VEHICULE
    public VehiculeDTO toDTO(Vehicule vehicule) {
        if (vehicule == null) return null;
        return new VehiculeDTO(
            vehicule.getId(),
            vehicule.getPlate(),
            vehicule.getBrand(),
            vehicule.getBatteryCapacity()
        );
    }

    public Vehicule toEntity(VehiculeDTO dto) {
        if (dto == null) return null;
        Vehicule vehicule = new Vehicule();
        vehicule.setId(dto.getId());
        vehicule.setPlate(dto.getPlate());
        vehicule.setBrand(dto.getBrand());
        vehicule.setBatteryCapacity(dto.getBatteryCapacity());
        return vehicule;
    }
}
