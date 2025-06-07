package com.humanbooster.buisinessCase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.humanbooster.buisinessCase.model.UserRole;

/**
 * User's DTO
 * Discards Password
 * Prevents circular references
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "First name is required")
    private String firstname;

    @NotBlank(message = "Last name is required")
    private String lastname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Past(message = "Birth date must be in the past")
    @Column(name="birthdate", nullable = false)
    private LocalDate birthDate;

    private LocalDateTime inscriptionDate;
    private Boolean accountValid;

    @NotNull(message = "Role is required")
    private UserRole role;

    private Boolean banned;
    private Long media_id;
} 