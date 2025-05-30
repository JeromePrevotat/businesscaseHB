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
public class Lieu {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="instruction")
    private String instruction;

    @NotNull
    @ManyToOne
    @JsonBackReference("adresse-lieu")
    @JoinColumn(name="adresse_id")
    private Adresse adresse;

    @NotNull
    @ManyToOne
    @JsonBackReference("lieu-utilisateur")
    @JoinColumn(name="utilisateur_id")
    private Utilisateur utilisateur;
    
    @NotNull
    @ManyToOne
    @JsonBackReference("borne-lieu")
    @JoinColumn(name="borne_id")
    private Borne borne;
}
