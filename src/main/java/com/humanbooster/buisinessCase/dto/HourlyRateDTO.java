package com.humanbooster.buisinessCase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * HourlyRate's DTO
 * Prevents circular references and simplifies data transfer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyRateDTO {
    private Long id;

    @NotNull(message = "Hourly Rate is required")
    @DecimalMin(value = "0.1", message = "Hourly Rate must be positive")
    private BigDecimal hourlyRate;
    
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    @NotNull(message = "End time is required")
    private LocalTime endTime;
    
    @Min(value = 1, message = "Week day must be between 1 and 7")
    @Max(value = 7, message = "Week day must be between 1 and 7")
    private Integer weekDay; // 1=Monday, 7=Sunday, NULL=All days

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;
    private Boolean active;

    // Simple reference without bidirectional relationship
    @NotNull(message = "Station is required")
    private Long stationId;
} 