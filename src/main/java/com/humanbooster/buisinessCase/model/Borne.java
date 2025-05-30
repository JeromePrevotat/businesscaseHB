package com.humanbooster.buisinessCase.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="bornes")
public class Borne {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
    
    @NotBlank
    @Column(name="borne_name")
    private String borneName;
    
    // @Column(name="gps_coordinates")
    // private float coordGps;

    @NotBlank
    @Column(name="hourly_price")
    private double hourlyPrice;

    @NotBlank
    @Column(name="power_output")
    private double powerOutput;
    
    @Column(name="instruction")
    private String instruction;
    
    @Column(name="grounded")
    private boolean grounded;
    
    @Column(name="state")
    private BorneState state;

    @NotNull
    @Column(name="busy")
    private boolean busy;
    
    @NotEmpty
    @Column(name="wired")
    private boolean wired;

    @OneToMany(targetEntity=Reservation.class, mappedBy="id", fetch = FetchType.LAZY)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference("borne-reservation")
    private List<Reservation> reservationList;
    
    @NotNull
    @OneToMany(targetEntity=Lieu.class, mappedBy="id", fetch = FetchType.LAZY)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference("borne-lieu")
    private List<Lieu> lieuList;

    @NotNull
    @OneToMany(targetEntity=Media.class, mappedBy="id", fetch = FetchType.LAZY)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference("borne-media")
    private List<Media> mediaList;
}
