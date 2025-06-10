package com.humanbooster.buisinessCase.mapper;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.humanbooster.buisinessCase.dto.UserDTO;
import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.model.Media;
import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.model.Vehicule;
import com.humanbooster.buisinessCase.repository.AdressRepository;
import com.humanbooster.buisinessCase.repository.MediaRepository;
import com.humanbooster.buisinessCase.repository.VehiculeRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserMapper {
    private final VehiculeRepository vehiculeRepository;
    private final MediaRepository mediaRepository;
    private final AdressRepository adressRepository;

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

}
