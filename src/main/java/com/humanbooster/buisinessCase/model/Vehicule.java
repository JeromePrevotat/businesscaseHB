package com.humanbooster.buisinessCase.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="vehicules")
public class Vehicule {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message="Vehicule Plate cannot be blank")
    @Column(name="plate")
    private String plate;

    @Column(name="brand")
    private String brand;

    @Column(name="battery_capacity")
    private int batteryCapacity;

    @NotNull
    @ManyToMany(mappedBy="vehiculeList")
    private Set<User> user = new HashSet<>();

    @NotNull
    @ManyToMany
    @JoinTable(
        name = "plug_types_vehicules",
        joinColumns = @JoinColumn(name = "vehicule_id"),
        inverseJoinColumns = @JoinColumn(name = "plug_type_id")
    )
    private Set<PlugType> plugType = new HashSet<>();
}
