package com.humanbooster.buisinessCase.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.humanbooster.buisinessCase.model.ReservationState;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reservation's DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long id;

    @NotNull(message="Start Date is required")
    @Future(message="Start Date must be in the future")
    private LocalDateTime startDate;
    
    @NotNull(message="End Date is required")
    private LocalDateTime endDate;

    @NotNull(message="State is required")
    private ReservationState state;

    private BigDecimal pricePayed;
    private LocalDateTime datePayed;

    @NotNull(message="Hourly Rate Log is required")
    private BigDecimal hourlyRateLog;
    
    @NotNull(message="User ID is required")
    private Long userId;

    @NotNull(message="Station ID is required")
    private Long stationId;
}
