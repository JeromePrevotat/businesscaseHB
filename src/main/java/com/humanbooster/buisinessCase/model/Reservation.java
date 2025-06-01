package com.humanbooster.buisinessCase.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Reservation made by a User for a Station.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull(message="Start Date is required")
    @Future(message="Start Date must be in the future")
    @Column(name="start_date", nullable=false)
    private LocalDateTime startDate;

    @NotNull(message="End Date is required")
    @Column(name="end_date", nullable=false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message="State is required")
    @Column(name="state", length=20, nullable=false)
    private ReservationState state;

    @NotEmpty(message="Hourly Rate Log is required")
    @DecimalMin(value="0.1", message="Hourly Rate Log must be a positive number")
    @Column(name="hourly_rate_log", precision=10, scale=4, nullable=false)
    private BigDecimal hourlyRateLog;

    @Column(name="created_at", nullable=false)
    private LocalDate createdAt = LocalDate.now();

    @Column(name="validated_at")
    private LocalDate validatedAt;

    @Column(name="total_price", precision=10, scale=2)
    private BigDecimal totalPrice;

    private BigDecimal pricePayed;
    private LocalDateTime datePayed;

    @Column(name="receipt_generated", nullable=false)
    private boolean receiptGenerated = false;

    @NotNull(message="User is required")
    @ManyToOne
    @JsonBackReference("reservation-user")
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    
    @NotNull(message="Station is required")
    @ManyToOne
    @JsonBackReference("station-reservation")
    @JoinColumn(name="station_id", nullable=false)
    private Station station;
    
}
