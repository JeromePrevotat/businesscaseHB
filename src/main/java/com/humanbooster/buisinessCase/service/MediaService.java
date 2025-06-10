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
public class MediaService{
    private final MediaRepository mediaRepository;

    /**
     * Saves a new Media.
     * @param media the Media to save
     * @return the newly saved Media
     */
    @Transactional
    public Media saveMedia(Media media){
        return mediaRepository.save(media);
    }

    /**
     * Retrieves all Media.
     * @return a list of all Media
     */
    @Transactional(readOnly = true)
    public List<Media> getAllMedia(){
        return mediaRepository.findAll();
    }

    /**
     * Retrieves a Media by its ID.
     * @param id the ID of the Media to retrieve
     * @return  an Optional containing the Media if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<Media> getMediaById(Long id){
        return mediaRepository.findById(id);
    }

    /**
     * Deletes a Media by its ID.
     * @param id the ID of the Media to delete
     * @return an Optional containing the deleted Media if found, or empty if not found
     */
    @Transactional
    public Optional<Media> deleteMediaById(Long id){
        Optional<Media> mediaOpt = mediaRepository.findById(id);
        mediaOpt.ifPresent(mediaRepository::delete);
        return mediaOpt;
    }

    /**
     * Updates an existing Media.
     * @param id the ID of the Media to update
     * @param newMedia the new Media data to update
     * @return an Optional containing the updated Media if found, or empty if not found
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
     * Checks if a Media exists by its ID.
     * @param id the ID of the Media to check
     * @return true if the Media exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return mediaRepository.existsById(id);
    }
}
