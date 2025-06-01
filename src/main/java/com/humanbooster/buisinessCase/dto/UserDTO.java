package com.humanbooster.buisinessCase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.humanbooster.buisinessCase.model.UserRole;

/**
 * User's DTO
 * Discards Password
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Role is required")
    private UserRole role;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private String iban;
    // private String vehicule;
    private Boolean banned;
    private Boolean accountValid;
    private LocalDateTime inscriptionDate;
    private SpotDTO spot;
} 