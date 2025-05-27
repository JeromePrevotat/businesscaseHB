package com.humanbooster.buisinessCase.model;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="medias")
public class Media {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @NotBlank
    @Column(name="url")
    private String url;
    
    @NotBlank
    @Column(name="type")
    private String type;
    
    @NotBlank
    @Column(name="media_name")
    private String mediaName;
    
    @Column(name="description")
    private String description;
    
    @Range(min=1)
    @Column(name="size")
    private int size;

    @NotNull
    @ManyToOne
    @Column(name="id_borne")
    private int id_borne;
}
