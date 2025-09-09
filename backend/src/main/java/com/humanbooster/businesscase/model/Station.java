package com.humanbooster.businesscase.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString(exclude = {"reservationList", "spot", "mediaList", "plugType"})
@Table(name="stations")
public class Station {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @NotBlank(message = "Station name cannot be blank")
    @Column(name="station_name")
    private String stationName;

    @Column(name = "latitude", precision = 10, scale = 8, nullable = false)
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @NotNull(message = "Latitude is required")
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8, nullable = false)
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @NotNull(message = "Longitude is required")
    private BigDecimal longitude;

    @Column(name="price_rate", precision = 10, scale = 2, nullable = false)
    @DecimalMin(value = "0.1", message = "Price rate must be positive")
    @NotNull(message = "Price rate is required")
    private BigDecimal priceRate;

    @Column(name="power_output", precision = 10, scale = 2, nullable = false)
    @DecimalMin(value = "0.1", message = "Power output must be positive")
    @NotNull(message = "Power output is required")
    private BigDecimal powerOutput;
    
    @Column(name="manual", columnDefinition= "TEXT")
    private String manual;
    
    @Enumerated(EnumType.STRING)
    @Column(name="state", length = 20, nullable = false)
    @NotNull(message = "Station state is required")
    private StationState state;

    @Column(name="grounded", nullable = false)
    @NotNull(message = "Grounded status is required")
    private boolean grounded = true;
    
    @NotNull(message = "Busy status is required")
    @Column(name="busy", nullable = false)
    private boolean busy =  false;

    @NotNull(message = "Wired status is required")
    @Column(name="wired")
    private boolean wired = false;

    @NotNull(message = "Spot is required")
    @ManyToOne
    @JoinColumn(name="spot", nullable = false)
    @JsonBackReference("stations-spots")
    private Spot spot;
    
    @NotNull(message = "User is required")
    @ManyToOne
    @JoinColumn(name="owner", nullable = false)
    @JsonBackReference("stations-users")
    private User owner;

    @OneToMany(targetEntity=Reservation.class, mappedBy="station", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("stations-reservations")
    private List<Reservation> reservationList;

    @NotNull
    @OneToMany(targetEntity=Media.class, mappedBy="station", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("medias-stations")
    private List<Media> mediaList;

    @NotNull
    @ManyToMany
    @JoinTable(
        name = "plug_types_stations",
        joinColumns = @JoinColumn(name = "station_id"),
        inverseJoinColumns = @JoinColumn(name = "plug_type_id")
    )
    @JsonManagedReference("plugtypes-stations")
    private List<PlugType> plugType;
}
