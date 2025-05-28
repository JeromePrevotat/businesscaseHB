package com.humanbooster.buisinessCase.model;

import java.util.List;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="adresses")
public class Adresse {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="street_number")
    private int streeNumber;
    
    @NotBlank
    @Column(name="street_name")
    private String streetName;

    @NotBlank
    @Column(name="zipcode")
    private String zipcode;

    @NotBlank
    @Column(name="city")
    private String city;

    @NotBlank
    @Column(name="country")
    private String country;

    @NotBlank
    @Column(name="region")
    private String region;

    @Column(name="addendum")
    private String addendum;

    @NotBlank
    @Column(name="floor")
    private int floor;

    @NotNull
    @ManyToOne
    @JoinColumn(name="utilisateur_id")
    private Utilisateur utilisateur;

    @OneToMany(targetEntity=Lieu.class, mappedBy="id", fetch = FetchType.LAZY)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name="lieu_id")
    private List<Lieu> lieu;
}
