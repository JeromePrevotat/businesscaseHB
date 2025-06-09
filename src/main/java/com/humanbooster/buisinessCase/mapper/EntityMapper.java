package com.humanbooster.buisinessCase.mapper;

import java.util.ArrayList;
import java.util.HashSet;

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
import com.humanbooster.buisinessCase.repository.AdressRepository;
import com.humanbooster.buisinessCase.repository.MediaRepository;
import com.humanbooster.buisinessCase.repository.PlugTypeRepository;
import com.humanbooster.buisinessCase.repository.SpotRepository;
import com.humanbooster.buisinessCase.repository.StationRepository;
import com.humanbooster.buisinessCase.repository.UserRepository;
import com.humanbooster.buisinessCase.repository.VehiculeRepository;

import lombok.AllArgsConstructor;

/**
 * Mapper to convert Entities between DTOs and JPA Entities.
 * Prevents circular references in JSON serialization.
 */
@Component
@AllArgsConstructor
public class EntityMapper {
    private final VehiculeRepository vehiculeRepository;
    private final MediaRepository mediaRepository;
    private final AdressRepository adressRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final SpotRepository spotRepository;
    private final PlugTypeRepository plugTypeRepository;

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
            adress.getFloor(),
            adress.getUserList() != null ? adress.getUserList()
                                                    .stream()
                                                    .map(user -> user.getId())
                                                    .toList()
                                            : null,
            adress.getSpotList() != null ? adress.getSpotList()
                                                    .stream()
                                                    .map(spot -> spot.getId())
                                                    .toList()
                                            : null
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
        adress.setUserList(dto.getUserList() != null ? dto.getUserList()
                                                    .stream()
                                                    .map(userId -> {
                                                        User user = new User();
                                                        user.setId(userId);
                                                        return user;
                                                    })
                                                    .toList()
                                            : null);
        adress.setSpotList(dto.getSpotList() != null ? dto.getSpotList()
                                                    .stream()
                                                    .map(spotId -> {
                                                        Spot spot = new Spot();
                                                        spot.setId(spotId);
                                                        return spot;
                                                    })
                                                    .toList()
                                            : null);
        return adress;
    }

    // SPOT
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
            station.getSpot() != null ? station.getSpot().getId() : null,
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

    // MEDIA
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
            user.getVehiculeList() != null ? user.getVehiculeList()
                                                .stream()
                                                .map(Vehicule::getId)
                                                .toList()
                                        : null,
            user.getMedia() != null ? user.getMedia().getId() : null,
            user.getAdressList() != null ? user.getAdressList()
                                                .stream()
                                                .map(Adress::getId)
                                                .toList()
                                        : null,
            user.getReservationList() != null ? user.getReservationList()
                                                .stream()
                                                .map(Reservation::getId)
                                                .toList()
                                        : null
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
        if (dto.getVehiculeList() != null && !dto.getVehiculeList().isEmpty()) {
            user.setVehiculeList(new HashSet<>(
                dto.getVehiculeList()
                    .stream()
                    .map(id -> {
                        Vehicule vehicule = vehiculeRepository.findById(id)
                                                                .orElse(null);
                        return vehicule;
                    })
                    .toList()));
        } else user.setVehiculeList(new HashSet<>());
        if (dto.getMedia_id() != null) {
            Media media = mediaRepository.findById(dto.getMedia_id()).orElse(null);
            user.setMedia(media);
        }
        if (dto.getAdressList() != null && !dto.getAdressList().isEmpty()) {
            user.setAdressList(new HashSet<>(
                dto.getAdressList()
                    .stream()
                    .map(id -> {
                        Adress adress = adressRepository.findById(id)
                                                        .orElse(null);
                        return adress;
                    })
                    .toList()));
        } else user.setAdressList(new HashSet<>());
        if (dto.getReservationList() != null && !dto.getReservationList().isEmpty()) {
            user.setReservationList(
                dto.getReservationList()
                    .stream()
                    .map(id -> {
                        Reservation reservation = new Reservation();
                        reservation.setId(id);
                        return reservation;
                    })
                    .toList());
        } else user.setReservationList(new ArrayList<>());
        return user;
    }

    // PLUG TYPE
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

    // VEHICULE
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
