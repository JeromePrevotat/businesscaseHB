package com.humanbooster.buisinessCase.dto;

import lombok.Data;

@Data
public class AccountConfirmationDTO {
    private Long user_id;
    private String code;
}