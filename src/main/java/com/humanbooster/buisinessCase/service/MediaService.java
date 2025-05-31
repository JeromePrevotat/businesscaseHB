package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.buisinessCase.model.Media;
import com.humanbooster.buisinessCase.repository.MediaRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing Media entities.
 * Provides methods to save, retrieve, update, and delete Media records.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MediaService {
    private final MediaRepository mediaRepository;

    /**
     * Retrieves all Medias
     * @return List of all Medias
     */
    @Transactional(readOnly = true)
    public List<Media> getAllMedias(){
        return mediaRepository.findAll();
    }
    
    /**
     * Retrieves a Media by its ID
     * @param id the ID of the Media to retrieve
     * @return Optional containing the Media if found, otherwise empty
     */
    public Optional<Media> getMediaById(long id){
        return mediaRepository.findById(id);
    }

    /**
     * Saves a new Media
     * @param media the Media object to save
     * @return the newly saved Media object
     */
    @Transactional
    public Media saveMedia(Media media){
        return mediaRepository.save(media);
    }

    /**
     * Deletes a Media by its ID
     * @param id the ID of the Media to delete
     * @return Optional containing the deleted Media if found, otherwise empty
     */
    @Transactional
    public Optional<Media> deleteMediaById(Long id){
        Optional<Media> mediaOpt = mediaRepository.findById(id);
        mediaOpt.ifPresent(mediaRepository::delete);
        return mediaOpt;
    }

    /**
     * Updates an existing Media
     * @param id the ID of the Media to update
     * @param newMedia the Media object containing updated fields
     * @return Optional containing the updated Media if found, otherwise empty
     */
    @Transactional
    public Optional<Media> updateMedia(Long id, Media newMedia){
        return mediaRepository.findById(id)
                .map(existingMedia -> {
                    ModelUtil.copyFields(newMedia, existingMedia);
                    return mediaRepository.save(existingMedia);
                });
    }

    /**
     * Checks if a Media exists by its ID
     * @param id the ID of the Media to check
     * @return true if the Media exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return mediaRepository.existsById(id);
    }

}