package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humanbooster.buisinessCase.model.Media;
import com.humanbooster.buisinessCase.repository.MediaRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import jakarta.transaction.Transactional;

@Service
public class MediaService {
    private final MediaRepository mediaRepository;

    @Autowired
    public MediaService(MediaRepository mediaRepository){
        this.mediaRepository = mediaRepository;
    }

    @Transactional
    public void saveMedia(Media media){
        mediaRepository.save(media);
    }

    public List<Media> getAllMedias(){
        return mediaRepository.findAll();
    }

    public Optional<Media> getMediaById(long id){
        return mediaRepository.findById(id);
    }

    @Transactional
    public Optional<Media> deleteMediaById(long id){
        Optional<Media> mediaOpt = mediaRepository.findById(id);
        mediaOpt.ifPresent(mediaRepository::delete);
        return mediaOpt;
    }

    @Transactional
    public Optional<Media> updateMedia(Media newMedia, long id){
        return mediaRepository.findById(id)
                .map(existingMedia -> {
                    ModelUtil.copyFields(newMedia, existingMedia);
                    return mediaRepository.save(existingMedia);
                });
    }

}