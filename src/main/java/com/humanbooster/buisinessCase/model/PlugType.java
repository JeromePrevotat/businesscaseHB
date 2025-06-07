package com.humanbooster.buisinessCase.model;

import java.util.HashSet;
import java.util.Set;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="plug_types")
public class PlugType {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message="Plug type cannot be blank")
    @Column(name="plug_name")
    private String plugname;

    @ManyToMany(mappedBy = "plug_id")
    @JoinTable(
        name="vehicules_plug_types",
        joinColumns= @JoinColumn(name="vehicule_id") ,
        inverseJoinColumns= @JoinColumn(name="plug_type_id")
    )
    private Set<Vehicule> vehiculeList = new HashSet<>();

    @ManyToMany(mappedBy = "plug_id")
    @JoinTable(
        name="stations_plug_types",
        joinColumns= @JoinColumn(name="station_id") ,
        inverseJoinColumns= @JoinColumn(name="plug_type_id")
    )
    private Set<Station> stationList = new HashSet<>();
}
