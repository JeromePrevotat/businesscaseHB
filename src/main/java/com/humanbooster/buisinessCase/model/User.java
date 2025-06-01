package com.humanbooster.buisinessCase.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * User entity representing a user in the system.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {

    public User(Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(name="username", length=100, nullable = false, unique = true)
    private String userName;

    @NotBlank(message = "First name is required")
    @Column(name="firstname", length=100, nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name="lastname", length=100, nullable = false)
    private String lastName;
    
    @NotBlank(message = "Password is required")
    @Column(name="password", length=255, nullable = false)
    private String password;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(name="role", length=20, nullable = false)
    private UserRole role;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name="email", length=150, nullable = false, unique = true)
    private String email;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @Column(name="birthdate", nullable = false)
    private LocalDate birthDate;

    @Column(name="iban", length=34)
    private String iban;

    @Column(name="banned", nullable = false)
    private boolean banned = false;

    @Column(name="account_valid", nullable = false)
    private boolean accountValid = false;

    @Column(name = "validation_code", length = 100)
    private String validationCode;

    @Column(name = "inscription_date", nullable = false)
    private LocalDateTime inscriptionDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "spot_id", nullable = false)
    @JsonManagedReference("spot-user")
    private Spot spot;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("reservation-user")
    private List<Reservation> reservationList;

    @OneToMany(targetEntity=Adress.class, mappedBy="id", fetch = FetchType.LAZY)
    // @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference("adress-user")
    private List<Adress> adressList;
    
    // @Column(name = "vehicule", length = 100)
    // private String vehicule;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="user_vehicule",
        joinColumns= @JoinColumn(name="user_id") ,
        inverseJoinColumns= @JoinColumn(name="vehicule_id")
    )
    // @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Vehicule> vehiculeList = new HashSet<>();

    public User(String userName,
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
        
        this.role = UserRole.REGISTERED;
    }
    
}
