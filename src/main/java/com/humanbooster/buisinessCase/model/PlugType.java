package com.humanbooster.buisinessCase.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString(exclude = {"vehiculeList", "stationList"})
@Table(name="plug_types")
public class PlugType {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message="Plug type cannot be blank")
    @Column(name="plug_name")
    private String plugname;

    @ManyToMany(mappedBy = "plugType")
    private Set<Vehicule> vehiculeList = new HashSet<>();

    @ManyToMany(mappedBy = "plugType")
    @JsonBackReference("plugtypes-stations")
    private Set<Station> stationList = new HashSet<>();
}
