package com.humanbooster.buisinessCase.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
    private int id;

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
}
