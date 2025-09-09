package com.humanbooster.businesscase.dto;

import lombok.Data;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDTO {
    private Long id;
    private String token;
    private Long userId;
    private Date issuedAt;
}
