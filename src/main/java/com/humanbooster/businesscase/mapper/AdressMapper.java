package com.humanbooster.businesscase.mapper;

import org.springframework.stereotype.Component;

import com.humanbooster.businesscase.dto.AdressDTO;
import com.humanbooster.businesscase.model.Adress;
import com.humanbooster.businesscase.model.Spot;
import com.humanbooster.businesscase.model.User;

@Component
public class AdressMapper {
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

}
