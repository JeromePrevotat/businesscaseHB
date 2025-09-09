package com.humanbooster.businesscase.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents an Adress in the system.
 * Users can have multiple Adresses (home, billing, etc...)
 * A Location is linked to an Adress.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString(exclude = {"userList", "spotList"})
@Table(name="adresses")
public class Adress {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message="Adress name cannot be blank")
    @Column(name="adress_name")
    private String adressname;

    @Column(name="street_number", length=15)
    private String streetnumber;
    
    @NotBlank(message="Street name cannot be blank")
    @Column(name="street_name")
    private String streetname;

    @NotBlank(message="Zipcode cannot be blank")
    @Column(name="zipcode", length=15)
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
    @ManyToMany(mappedBy = "adressList", fetch = FetchType.LAZY)
    @JsonBackReference("adresses-users")
    private List<User> userList;

    @OneToMany(targetEntity=Spot.class, mappedBy="adress", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Spot> spotList;
}
