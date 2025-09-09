package com.humanbooster.businesscase.dto;

import java.util.List;

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
    
    @NotBlank(message="Adress name cannot be blank")
    private String adressname;

    @NotBlank(message="Street number cannot be blank")
    private String streetnumber;
    
    @NotBlank(message="Street name cannot be blank")
    private String streetname;

    @NotBlank(message="Zipcode cannot be blank")
    private String zipcode;

    @NotBlank(message="City cannot be blank")
    private String city;

    @NotBlank(message="Country cannot be blank")
    private String country;

    private String region;
    private String addendum;
    private int floor;
    private List<Long> userList;
    private List<Long> spotList;

}
