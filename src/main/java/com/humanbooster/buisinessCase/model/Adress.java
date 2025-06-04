package com.humanbooster.buisinessCase.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

/**
 * Represents an Adress in the system.
 * Users can have multiple Adresses (home, billing, etc...)
 * A Location is linked to an Adress.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="adresses")
public class Adress {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="adress_name")
    private String adressName;

    @Column(name="street_number")
    private int streetNumber;
    
    @NotBlank(message="Street name cannot be blank")
    @Column(name="street_name")
    private String streetName;

    @NotBlank(message="Zipcode cannot be blank")
    @Column(name="zipcode")
    private String zipcode;

    @NotBlank(message="City cannot be blank")
    @Column(name="city")
    private String city;

    @NotBlank(message="Country cannot be blank")
    @Column(name="country")
    private String country;

    @Column(name="region")
    private String region;

    @Column(name="addendum")
    private String addendum;

    @Column(name="floor")
    private int floor;

    @NotNull
    @ManyToOne
    @JsonBackReference("adress-user")
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(targetEntity=Spot.class, mappedBy="id", fetch = FetchType.LAZY)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference("adress-spot")
    private List<Spot> spotList;
}
