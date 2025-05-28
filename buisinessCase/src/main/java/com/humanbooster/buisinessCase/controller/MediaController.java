package com.humanbooster.buisinessCase.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.humanbooster.buisinessCase.model.Media;
import com.humanbooster.buisinessCase.service.MediaService;

@RestController
public class MediaController {
    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService){
        this.mediaService = mediaService;
    }

    @GetMapping("/medias")
    public List<Media> getAllMedias(){
        return mediaService.getAllMedias();
    }

    @GetMapping("/medias/{id}")
    public ResponseEntity<Media> getMediaById(@PathVariable long id){
        return mediaService.getMediaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/medias")
    public void saveMedia(@RequestBody Media media){
        mediaService.saveMedia(media);
    }

    @DeleteMapping("/medias/{id}")
    public ResponseEntity<Media> deleteMediaById(@PathVariable long id){
        return mediaService.deleteMediaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/medias/{id}")
    public ResponseEntity<Media> updateMedia(@RequestBody Media newMedia, @PathVariable long id){
        return mediaService.updateMedia(newMedia, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
