package com.humanbooster.buisinessCase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Media entity representing a media file
 * A Media can illustsrate multiple Stations
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="medias")
public class Media {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message="URL cannot be blank")
    @Column(name="url", nullable=false, length=500)
    private String url;

    @NotBlank(message="Type cannot be blank")
    @Column(name="type", nullable=false, length=50)
    private String type;
    
    @NotBlank(message="Media name cannot be blank")
    @Column(name="media_name", nullable=false, length=200)
    private String mediaName;
    
    @Column(name="size")
    private Long size;

    @OneToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JsonBackReference("medias-spots")
    private Spot spot;

    @ManyToOne(fetch=FetchType.LAZY)
    @JsonBackReference("medias-stations")
    private Station station;
}
