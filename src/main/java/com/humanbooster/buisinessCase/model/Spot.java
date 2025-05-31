package com.humanbooster.buisinessCase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="location")
public class Spot {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="instruction")
    private String instruction;

    @NotNull
    @ManyToOne
    @JsonBackReference("adresse-spot")
    @JoinColumn(name="adress_id")
    private Adress adress;

    @NotNull
    @ManyToOne
    @JsonBackReference("spot-user")
    @JoinColumn(name="user_id")
    private User user;
    
    @NotNull
    @ManyToOne
    @JsonBackReference("station-spot")
    @JoinColumn(name="station_id")
    private Station station;
}
