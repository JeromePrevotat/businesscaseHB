package com.humanbooster.buisinessCase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents the Station's Hourly Rates
 * This entity allows for flexible pricing based on time, day, and periods.
 */
@Entity
@Table(name = "tarif_horaire")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_tarif")
    private Long id;

    @Column(name = "hourly_rate", precision = 10, scale = 4, nullable = false)
    @DecimalMin(value = "0.1", message = "Hourly rate must be positive")
    @NotNull(message = "Hourly rate is required")
    private BigDecimal hourlyRate;

    @Column(name = "start_time", nullable = false)
    @NotNull(message = "Statt time is required")
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @Column(name = "week_day")
    @Min(value = 1, message = "Week day must be between 1 and 7")
    @Max(value = 7, message = "Week day must be between 1 and 7")
    private Integer weekDay; // 1=Monday, 7=Sunday, NULL=All days

    @Column(name = "start_date", nullable = false)
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    @NotNull(message = "Station is required")
    @JsonBackReference("station-rate")
    private Station station;
}