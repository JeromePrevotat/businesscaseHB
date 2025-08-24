package com.humanbooster.buisinessCase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User's DTO
 * Used to change user password
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePwdDTO {
    private Long id;
    @NotBlank(message = "Old password is required")
    private String oldpwd;
    @NotBlank(message = "New password is required")
    private String newpwd;
}
