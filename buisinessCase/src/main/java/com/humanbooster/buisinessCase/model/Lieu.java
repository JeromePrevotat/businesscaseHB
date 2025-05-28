package com.humanbooster.buisinessCase.model;

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
    private int id;

    @Column(name="instruction")
    private String instruction;

    @NotNull
    @ManyToOne
    @JoinColumn(name="id")
    private Adresse adresse;

    @NotNull
    @ManyToOne
    @JoinColumn(name="id")
    private Utilisateur utilisateur;
}
