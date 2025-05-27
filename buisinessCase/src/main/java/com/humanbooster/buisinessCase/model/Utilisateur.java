package com.humanbooster.buisinessCase.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Table(name="utilisateurs")
@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @NotBlank
    @NotEmpty
    @Size(min = 6, max = 50)
    @Column(name="username")
    private String username;
    
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

    @NotBlank
    @Column(name="role")
    private RoleUtilisateur role;

    @NotBlank
    @Email
    @Column(name="email")
    private String email;

    @NotBlank
    @Column(name="birthdate")
    private LocalDate birthDate;

    @NotBlank
    @Column(name="iban")
    private String iban;

    // @Column(name="owned_vehicules")
    // private List<Vehicule> ownedVehicules;

    @NotNull
    private boolean banned;

    public Utilisateur(){}

    public Utilisateur(String username,
            String firstName,
            String lastName,
            String password,
            String email,
            LocalDate birthDate,
            String iban) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.iban = iban;
        
        this.role = RoleUtilisateur.REGISTERED;
        // this.ownedVehicules = null;
    }

    // GETTER
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public RoleUtilisateur getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getIban() {
        return iban;
    }

    public boolean isBanned() {
        return banned;
    }

    // SETTER
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(RoleUtilisateur role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
    
    
    
}
