package com.humanbooster.buisinessCase.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

import com.humanbooster.buisinessCase.mapper.EntityMapper;
import com.humanbooster.buisinessCase.dto.MediaDTO;
import com.humanbooster.buisinessCase.model.Media;
import com.humanbooster.buisinessCase.service.MediaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing Media entities.
 * Provides endpoints for creating, retrieving, updating, and deleting media    .
 */
@RestController
@RequestMapping("/api/medias")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;
    private final EntityMapper mapper;


    /**
     * Get all media.
     * GET /api/medias
     * @return ResponseEntity with the list of media
     */
    @GetMapping
    public ResponseEntity<List<MediaDTO>> getAllMedia(){
        List<MediaDTO> mediaDTOs = mediaService.getAllMedia().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(mediaDTOs);
    }

    /**
     * Get a media by ID.
     * GET /api/medias/{id}
     * @param id The ID of the media to retrieve
     * @return ResponseEntity with the media if found, or 404 Not Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<MediaDTO> getMediaById(@PathVariable Long id){
        return mediaService.getMediaById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new media.
     * POST /api/medias
     * @param media The media entity to be saved
     * @return ResponseEntity with the saved media and 201 Created status
     */
    @PostMapping
    public ResponseEntity<MediaDTO> saveMedia(@Valid @RequestBody MediaDTO mediaDTO){
        Media newMedia = mapper.toEntity(mediaDTO);
        Media savedMedia = mediaService.saveMedia(newMedia);
        MediaDTO savedMediaDTO = mapper.toDTO(savedMedia);
        // Conform RESTful practices, we should return a URI to the created resource.
        // URI location = URI.create("/api/medias/" + savedMedia.getId());
        // return ResponseEntity.created(location).body(savedMedia);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMediaDTO);
    }

    /**
     * Delete a media by ID.
     * DELETE /api/medias/{id}
     * @param id The ID of the media to delete
     * @return ResponseEntity with the 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaById(@PathVariable Long id){
        return mediaService.deleteMediaById(id).isPresent() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Update a media by ID.
     * PUT /api/medias/{id}
     * @param id The ID of the media to update
     * @param newMedia The updated media entity
     * @return ResponseEntity with the updated media if found, or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<MediaDTO> updateMedia(@PathVariable Long id, @Valid @RequestBody Media newMedia){
        return mediaService.updateMedia(id, newMedia)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

