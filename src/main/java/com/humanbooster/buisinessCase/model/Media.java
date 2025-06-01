package com.humanbooster.buisinessCase.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
    
    @Column(name="description", columnDefinition="TEXT")
    private String description;
    
    @Column(name="size")
    private Long size;

    @ManyToMany(mappedBy="mediaList", fetch=FetchType.LAZY)
    @JsonIgnore
    private List<Station> stationList;
}
