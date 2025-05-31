package com.humanbooster.buisinessCase.dto;

import com.humanbooster.buisinessCase.model.Spot;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Adress entity.
 * Includes a simple reference without circular relations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdressDTO {
    private Long id;
    private String adressName;
    private int streetNumber;
    
    @NotBlank(message="Street name cannot be blank")
    private String streetName;

    @NotBlank(message="Zipcode cannot be blank")
    private String zipcode;

    @NotBlank(message="City cannot be blank")
    private String city;

    @NotBlank(message="Country cannot be blank")
    private String country;

    private String region;
    private String addendum;
    private int floor;

    // private Long userId; // Reference to Utilisateur ID
    // private List<SpotDTO> spotList; // List of LieuDTOs to avoid circular references
}
