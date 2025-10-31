package com.humanbooster.businesscase.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.humanbooster.businesscase.model.ReservationState;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reservation's DTO
 * Prevents circular references
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long id;

    private LocalDateTime createdAt;
    private LocalDateTime validatedAt;

    @NotNull(message="Start Date is required")
    @Future(message="Start Date must be in the future")
    private LocalDateTime startDate;
    
    @NotNull(message="End Date is required")
    private LocalDateTime endDate;

    @NotNull(message="Hourly Rate Log is required")
    private BigDecimal hourlyRateLog;

    @NotNull(message="State is required")
    private ReservationState state;

    private boolean payed;
    private LocalDateTime datePayed;
    
    private Long user_id;

    @NotNull(message="Station ID is required")
    private Long station_id;
}
