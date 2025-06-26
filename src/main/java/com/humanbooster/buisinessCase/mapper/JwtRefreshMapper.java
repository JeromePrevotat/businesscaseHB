package com.humanbooster.buisinessCase.mapper;

import org.springframework.stereotype.Component;

import com.humanbooster.buisinessCase.dto.JwtRefreshDTO;
import com.humanbooster.buisinessCase.model.JwtRefresh;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class JwtRefreshMapper {
    public JwtRefreshDTO toDTO(JwtRefresh refreshToken){
        if (refreshToken == null) return null;
        JwtRefreshDTO dto = new JwtRefreshDTO();
        dto.setId(refreshToken.getId());
        dto.setUserId(refreshToken.getUserId());
        dto.setRefreshToken(refreshToken.getRefreshToken());
        dto.setIssuedAt(refreshToken.getIssuedAt());
        return dto;
    }

    public JwtRefresh toEntity(JwtRefreshDTO dto){
        if (dto == null) return null;
        JwtRefresh refreshToken = new JwtRefresh();
        refreshToken.setId(dto.getId());
        refreshToken.setUserId(dto.getUserId());
        refreshToken.setRefreshToken(dto.getRefreshToken());
        refreshToken.setIssuedAt(dto.getIssuedAt());
        return refreshToken;
    }
}
