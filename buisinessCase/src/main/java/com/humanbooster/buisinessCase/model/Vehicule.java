package com.humanbooster.buisinessCase.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
@Table(name="vehicules")
public class Vehicule {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @NotBlank
    @NotEmpty
    @Column(name="plate")
    private String plate;

    @Column(name="brand")
    private String brand;

    @Column(name="model")
    private String model;

    @Column(name="year")
    private int year;

    @Column(name="battery_capacity")
    private int batteryCapacity;

    @NotNull
    @ManyToMany
    @JoinColumn(name="id")
    private Utilisateur utilisateur;
}
