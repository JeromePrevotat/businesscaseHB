package com.humanbooster.buisinessCase.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a Spot.
 * A Spot can contain multiple Stations and is located at a specific Adress.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="spots")
public class Spot {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="instruction", columnDefinition="TEXT")
    private String instruction;

    @OneToMany(mappedBy = "spot_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Station> stationList;

    @ManyToOne
    @JoinColumn(name="adress_id", nullable=false)
    private Adress address_id;

    @NotNull
    @OneToMany(targetEntity=Media.class, mappedBy="media_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Media> mediaList;
}
