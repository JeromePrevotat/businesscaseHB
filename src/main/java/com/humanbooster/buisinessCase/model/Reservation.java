package com.humanbooster.buisinessCase.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @NotEmpty
    @Column(name="start_date")
    private LocalDate startDate;

    @NotEmpty
    @Column(name="end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name="state")
    private ReservationState state;

    @NotEmpty
    @Column(name="hourly_rate_log")
    private double hourlyRateLog;

    @NotNull
    @ManyToOne
    @JsonBackReference("reservation-user")
    @JoinColumn(name="user_id")
    private User user;
    
    @NotNull
    @ManyToOne
    @JsonBackReference("station-reservation")
    @JoinColumn(name="station_id")
    private Station station;
    
}
