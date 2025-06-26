package com.humanbooster.buisinessCase.dto;

import lombok.Data;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRefreshDTO {
    private Long id;
    private String refreshToken;
    private Long userId;
    private Date issuedAt;
}
