package com.humanbooster.buisinessCase.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.humanbooster.buisinessCase.dto.UserDTO;
import com.humanbooster.buisinessCase.dto.UserRegisterDTO;
import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.model.Media;
import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.model.Role;
import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.model.UserRole;
import com.humanbooster.buisinessCase.model.Vehicule;
import com.humanbooster.buisinessCase.repository.AdressRepository;
import com.humanbooster.buisinessCase.repository.MediaRepository;
import com.humanbooster.buisinessCase.repository.RoleRepository;
import com.humanbooster.buisinessCase.repository.VehiculeRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserMapper {
    private final VehiculeRepository vehiculeRepository;
    private final MediaRepository mediaRepository;
    private final AdressRepository adressRepository;
    private final RoleRepository roleRepository;

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
            user.getRoleList() != null ? user.getRoleList()
                                        .stream()
                                        .map(Role::getId)
                                        .toList()
                                        : null,
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
                                        : null,
            user.getStationList() != null ? user.getStationList()
                                                .stream()
                                                .map(Station::getId)
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
        if (dto.getRoleList() != null && !dto.getRoleList().isEmpty()){
            user.setRoleList(
                dto.getRoleList()
                .stream()
                    .map(id -> {
                        Role role = roleRepository.findById(id).orElse(null);
                        return role;
                    })
                    .filter(role -> role != null)
                    .collect(Collectors.toList())
            );
        }
        else user.setRoleList(new ArrayList<>());
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
        if (dto.getStationList() != null && !dto.getStationList().isEmpty()) {
            user.setStationList(
                dto.getStationList()
                    .stream()
                    .map(id -> {
                        Station station = new Station();
                        station.setId(id);
                        return station;
                    })
                    .toList());
        } else user.setStationList(new ArrayList<>());
        return user;
    }

    public User toEntity(UserRegisterDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setBirthDate(dto.getBirthdate());
        user.setPassword(dto.getPassword());
        // Inscription date and account validity are not part of UserRegisterDTO
        // as they are typically set during registration and not provided by the user.
        user.setInscriptionDate(LocalDateTime.now());
        user.setAccountValid(false);
        Role roleUser = roleRepository.findByName(UserRole.USER);
        if (roleUser != null) {
            user.setRoleList(List.of(roleUser));
        } else {
            user.setRoleList(new ArrayList<>());
        }
        user.setBanned(false);
        user.setVehiculeList(new HashSet<>());
        user.setMedia(null);
        user.setAdressList(new HashSet<>());
        user.setReservationList(new ArrayList<>());
        return user;
    }

}
