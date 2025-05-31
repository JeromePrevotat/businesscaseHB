package com.humanbooster.buisinessCase.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humanbooster.buisinessCase.model.Media;
import com.humanbooster.buisinessCase.service.MediaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing Medias
 * Provides endpoints to retrieve, create, update, and delete Media records.
 */
@RestController
@RequestMapping("/api/medias")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    /**
     * Retrieves all Medias
     * @return ResponseEntity containing a list of all Medias
     */
    @GetMapping
    public ResponseEntity<List<Media>> getAllMedias(){
        return ResponseEntity.ok(mediaService.getAllMedias());
    }

    /**
     * Retrieves a Media by its ID
     * @param id the ID of the Media to retrieve
     * @return ResponseEntity containing the Media if found, otherwise 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Media> getMediaById(@PathVariable Long id){
        return mediaService.getMediaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Saves a new Media
     * @param media the Media object to save
     * @return ResponseEntity indicating the result of the save operation
     */
    @PostMapping
    public ResponseEntity<Media> saveMedia(@Valid @RequestBody Media media){
        Media savedMedia = mediaService.saveMedia(media);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMedia);
    }

    /**
     * Deletes a Media by its ID
     * @param id the ID of the Media to delete
     * @return ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Media> deleteMediaById(@PathVariable Long id){
        return mediaService.deleteMediaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing Media
     * @param id the ID of the Media to update
     * @param newMedia the Media object containing updated fields
     * @return ResponseEntity containing the updated Media if found, otherwise 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Media> updateMedia(@PathVariable Long id, @Valid @RequestBody Media newMedia){
        return mediaService.updateMedia(id, newMedia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
