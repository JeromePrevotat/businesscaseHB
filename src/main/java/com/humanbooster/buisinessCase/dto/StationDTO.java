package com.humanbooster.buisinessCase.dto;

import java.math.BigDecimal;
import java.util.List;

import com.humanbooster.buisinessCase.model.StationState;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Station's DTO
 * Prevents circular references
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationDTO {
    private Long id;
    
    @NotBlank(message = "Station name cannot be blank")
    private String stationName;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @NotNull(message = "Latitude is required")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @NotNull(message = "Longitude is required")
    private BigDecimal longitude;

    @DecimalMin(value = "0.1", message = "Price rate must be positive")
    @NotNull(message = "Price rate is required")
    private BigDecimal priceRate;

    @DecimalMin(value = "0.1", message = "Power output must be positive")
    @NotNull(message = "Power output is required")
    private BigDecimal powerOutput;
    
    private String manual;
    
    @NotNull(message = "Station state is required")
    private StationState state;

    @NotNull(message = "Grounded status is required")
    private boolean grounded = true;
    
    @NotNull(message = "Busy status is required")
    private boolean busy =  false;
    
    @NotNull(message = "Wired status is required")
    private boolean wired = false;

    private Long spot_id;
    private List<Long> reservationList;
    private List<Long> mediaList;

}
