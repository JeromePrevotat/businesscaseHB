package com.humanbooster.buisinessCase.model;

import java.time.LocalDate;

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
    private int id;

    @NotEmpty
    @Column(name="start_date")
    private LocalDate startDate;

    @NotEmpty
    @Column(name="end_date")
    private LocalDate endDate;

    @NotEmpty
    @Column(name="state")
    private ReservationState state;

    @NotEmpty
    @Column(name="hourly_price_log")
    private double hourlyPriceLog;

    @NotNull
    @ManyToOne
    @JoinColumn(name="id")
    private Utilisateur utilisateur;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name="id")
    private Borne borne;
    
}
