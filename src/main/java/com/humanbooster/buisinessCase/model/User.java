package com.humanbooster.buisinessCase.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * User entity representing a user in the system.
 */
@Entity
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString(exclude = {"reservationList", "adressList", "vehiculeList", "media"})
@Table(name="users")
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(name="username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "First name is required")
    @Column(name="firstname", nullable = false)
    private String firstname;

    @NotBlank(message = "Last name is required")
    @Column(name="lastname", nullable = false)
    private String lastname;

    @NotBlank(message = "Password is required")
    @Column(name="password", length=255, nullable = false)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name="email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @Column(name="birthdate", nullable = false)
    private LocalDate birthDate;
    
    @Column(name = "inscription_date", nullable = false)
    private LocalDateTime inscriptionDate = LocalDateTime.now();

    @Column(name="account_valid", nullable = false)
    private boolean accountValid = false;

    @Column(name = "validation_code", length = 15)
    private String validationCode;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(name="role", length=20, nullable = false)
    private UserRole role;

    @Column(name="iban", length=34)
    private String iban;

    @Column(name="banned", nullable = false)
    private boolean banned = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="users_vehicules",
        joinColumns= @JoinColumn(name="user") ,
        inverseJoinColumns= @JoinColumn(name="vehicule")
    )
    // @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Vehicule> vehiculeList = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Media media;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="adresses_users",
        joinColumns= @JoinColumn(name="user") ,
        inverseJoinColumns= @JoinColumn(name="adress")
    )
    // @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Adress> adressList = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("reservations-users")
    private List<Reservation> reservationList;

    public User(String userName,
            String firstName,
            String lastName,
            String password,
            String email,
            LocalDate birthDate,
            String iban) {
        this.username = userName;
        this.firstname = firstName;
        this.lastname = lastName;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.iban = iban;
        
        this.role = UserRole.REGISTERED;
    }

    /**
     * Returns the authorities granted to the user.
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ADMIN"));
    }

}
