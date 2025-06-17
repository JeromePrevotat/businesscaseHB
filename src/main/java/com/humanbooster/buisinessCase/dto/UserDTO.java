package com.humanbooster.buisinessCase.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private LocalDate birthDate;

    private LocalDateTime inscriptionDate;
    private Boolean accountValid;

    @NotNull(message = "Role is required")
    private List<Long> roleList;

    private Boolean banned;
    private List<Long> vehiculeList;
    private Long media_id;
    private List<Long> adressList;
    private List<Long> reservationList;
} 