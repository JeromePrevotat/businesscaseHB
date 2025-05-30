package com.humanbooster.buisinessCase.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="utilisateurs")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @NotBlank
    @NotEmpty
    @Size(min = 6, max = 50)
    @Column(name="username")
    private String userName;
    
    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 50)
    @Column(name="firstname")
    private String firstName;
    
    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 50)
    @Column(name="lastname")
    private String lastName;
    
    @NotBlank
    @NotEmpty
    @Size(min = 6)
    @Column(name="password")
    private String password;

    @NotNull
    @Column(name="role")
    private RoleUtilisateur role;

    @NotBlank
    @Email
    @Column(name="email")
    private String email;

    @NotNull
    @Column(name="birthdate")
    private LocalDate birthDate;

    @NotBlank
    @Column(name="iban")
    private String iban;

    @NotNull
    @Column(name="banned")
    private boolean banned;
    
    @OneToMany(targetEntity=Adresse.class, mappedBy="id", fetch = FetchType.LAZY)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference("adresse-utilisateur")
    private List<Adresse> adresseList;

    @OneToMany(targetEntity=Lieu.class, mappedBy="id", fetch = FetchType.LAZY)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference("lieu-utilisateur")
    private List<Lieu> lieuList;

    @OneToMany(targetEntity=Reservation.class, mappedBy="id", fetch = FetchType.LAZY)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference("reservation-utilisateur")
    private List<Reservation> reservationList;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="utilisateur_vehicule",
        joinColumns= @JoinColumn(name="utilisateur_id") ,
        inverseJoinColumns= @JoinColumn(name="vehicule_id")
    )
    // @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Vehicule> vehiculeList = new HashSet<>(); // HashSet ? HashMap ?

    public Utilisateur(String userName,
            String firstName,
            String lastName,
            String password,
            String email,
            LocalDate birthDate,
            String iban) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.iban = iban;
        
        this.role = RoleUtilisateur.REGISTERED;
    }
    
}
