package com.humanbooster.buisinessCase.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    private int id;
    
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
    
    @NotBlank
    @Column(name="state")
    private BorneState state;

    @NotEmpty
    @Column(name="busy")
    private boolean busy;
    
    @NotEmpty
    @Column(name="wired")
    private boolean wired;
}
