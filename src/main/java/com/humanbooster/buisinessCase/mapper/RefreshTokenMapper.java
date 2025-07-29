package com.humanbooster.buisinessCase.mapper;

import org.springframework.stereotype.Component;

import com.humanbooster.buisinessCase.dto.RefreshTokenDTO;
import com.humanbooster.buisinessCase.model.RefreshToken;
import com.humanbooster.buisinessCase.repository.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RefreshTokenMapper {
    private final UserRepository userRepository;

    public RefreshTokenDTO toDTO(RefreshToken refreshToken){
        if (refreshToken == null) return null;
        RefreshTokenDTO dto = new RefreshTokenDTO();
        dto.setId(refreshToken.getId());
        dto.setUserId(refreshToken.getUser().getId());
        dto.setToken(refreshToken.getToken());
        dto.setIssuedAt(refreshToken.getIssuedAt());
        return dto;
    }

    public RefreshToken toEntity(RefreshTokenDTO dto){
        if (dto == null) return null;
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(dto.getId());
        refreshToken.setUser(userRepository.findById(dto.getUserId()).orElse(null));
        refreshToken.setToken(dto.getToken());
        refreshToken.setIssuedAt(dto.getIssuedAt());
        return refreshToken;
    }
}
