package com.humanbooster.businesscase.dto;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Vehicule's DTO
 * Prevents circular references
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeDTO {
    private Long id;

    @NotBlank(message="Vehicule Plate cannot be blank")
    private String plate;

    @Column(name="brand")
    private String brand;

    @Column(name="battery_capacity")
    private int batteryCapacity;
    private List<Long> userList;
    private List<Long> plugTypeList;
}
