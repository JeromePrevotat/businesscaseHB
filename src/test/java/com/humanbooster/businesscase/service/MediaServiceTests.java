package com.humanbooster.businesscase.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.humanbooster.businesscase.model.Media;
import com.humanbooster.businesscase.model.User;
import com.humanbooster.businesscase.repository.MediaRepository;

@ExtendWith(MockitoExtension.class)
public class MediaServiceTests {
    @Mock
    private MediaRepository mediaRepository;
    @InjectMocks
    private MediaService mediaService;

    private Media mockTemplateMedia;

    @BeforeEach
    public void setUp() {
        this.mockTemplateMedia = new Media();
        this.mockTemplateMedia.setId(1L);
        this.mockTemplateMedia.setUrl("http://example.com/test.jpg");
        this.mockTemplateMedia.setType("image/jpeg");
        this.mockTemplateMedia.setMediaName("Test Media");
        this.mockTemplateMedia.setSize(1024L);
        this.mockTemplateMedia.setUser(new User());
    }

    @Test
    void test_save_media_service(){
        // Arrange
        Media newMedia = new Media();
        newMedia.setId(mockTemplateMedia.getId());
        newMedia.setUrl(mockTemplateMedia.getUrl());
        newMedia.setType(mockTemplateMedia.getType());
        newMedia.setMediaName(mockTemplateMedia.getMediaName());
        newMedia.setSize(mockTemplateMedia.getSize());
        newMedia.setUser(mockTemplateMedia.getUser());

        Media mockMedia = this.mockTemplateMedia;
        when(mediaRepository.save(newMedia)).thenReturn(mockMedia);

        // Act
        Media savedMedia = mediaService.saveMedia(newMedia);

        // Assert
        assertAll(
            () -> assertNotNull(savedMedia, "Saved media should not be null"),
            () -> assertEquals(mockMedia.getId(), savedMedia.getId(), "Saved media ID should match"),
            () -> assertEquals(mockMedia.getUrl(), savedMedia.getUrl(), "Saved media URL should match"),
            () -> assertEquals(mockMedia.getType(), savedMedia.getType(), "Saved media type should match"),
            () -> assertEquals(mockMedia.getMediaName(), savedMedia.getMediaName(), "Saved media name should match"),
            () -> assertEquals(mockMedia.getSize(), savedMedia.getSize(), "Saved media size should match"),
            () -> assertEquals(mockMedia.getUser(), savedMedia.getUser(), "Saved media user should match")
        );
    }

    @Test
    public void test_get_all_media_service() throws IllegalAccessException {
        // Arrange
        Media media1 = new Media();
        media1.setId(1L);
        media1.setUrl("http://example.com/test1.jpg");
        media1.setType("image/jpeg");
        media1.setMediaName("Test Media 1");
        media1.setSize(1024L);
        media1.setUser(new User());

        Media media2 = new Media();
        media2.setId(2L);
        media2.setUrl("http://example.com/test2.jpg");
        media2.setType("image/jpeg");
        media2.setMediaName("Test Media 2");
        media2.setSize(2048L);
        media2.setUser(new User());

        ArrayList<Media> mockMediaList = new ArrayList<>();
        mockMediaList.add(media1);
        mockMediaList.add(media2);

        when(mediaRepository.findAll()).thenReturn(mockMediaList);

        // Act
        List<Media> results = mediaService.getAllMedia();

        // Assert
        assertNotNull(results, "Media list should not be null");
        assertEquals(2, results.size(), "Media list size should match");

        // Check Lists content without any order
        assertThat(results).containsExactlyInAnyOrderElementsOf(mockMediaList);
    }

    @Test
    public void test_get_media_by_id_service() {
        // Arrange
        Long mediaId = 1L;
        Media mockMedia = this.mockTemplateMedia;
        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(mockMedia));

        // Act
        Optional<Media> resultMedia = mediaService.getMediaById(mediaId);

        // Assert
        assertTrue(resultMedia.isPresent(), "Media should be found");
        assertEquals(mockMedia, resultMedia.get(), "Result Media should match the mock");
    }

    @Test
    public void test_get_media_by_id_service_with_invalid_id() {
        // Arrange
        Long mediaId = 1L;
        when(mediaRepository.findById(mediaId)).thenReturn(Optional.empty());

        // Act
        Optional<Media> resultMedia = mediaService.getMediaById(mediaId);

        // Assert
        assertTrue(resultMedia.isEmpty(), "Media should not be found");
    }

    @Test
    public void test_delete_media_by_id_service() {
        // Arrange
        Long mediaId = 1L;
        Media mockMedia = this.mockTemplateMedia;
        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(mockMedia));

        // Act
        Optional<Media> resultMedia = mediaService.deleteMediaById(mediaId);

        // Assert
        assertTrue(resultMedia.isPresent(), "Deleted Media should be returned");
        assertEquals(mockMedia, resultMedia.get(), "Deleted Media should match the mock");
    }

    @Test
    public void test_delete_media_by_id_service_with_invalid_id() {
        // Arrange
        Long mediaId = 1L;
        when(mediaRepository.findById(mediaId)).thenReturn(Optional.empty());

        // Act
        Optional<Media> resultMedia = mediaService.deleteMediaById(mediaId);

        // Assert
        assertTrue(resultMedia.isEmpty(), "Deleted Media should not be found");
    }

    @Test
    public void test_update_media_service() {
        // Arrange
        Long mediaId = 1L;
        Media existingMedia = new Media();
        existingMedia.setId(mediaId);
        existingMedia.setUrl("http://example.com/old.jpg");
        existingMedia.setType("image/jpeg");
        existingMedia.setMediaName("Old Media");
        existingMedia.setSize(1024L);
        existingMedia.setUser(new User());

        Media mockMedia = new Media();
        mockMedia.setId(mediaId);
        mockMedia.setUrl("http://example.com/updated.jpg");
        mockMedia.setType("image/jpeg");
        mockMedia.setMediaName("Updated Media");
        mockMedia.setSize(2048L);
        mockMedia.setUser(new User());

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(existingMedia));
        when(mediaRepository.save(any(Media.class))).thenReturn(mockMedia);

        // Act
        Optional<Media> result = mediaService.updateMedia(mediaId, mockMedia);

        // Assert
        assertTrue(result.isPresent(), "Updated Media should be returned");

        Media newMedia = result.get();

        assertEquals(mockMedia.getId(), newMedia.getId(), "ID should remain the same after update");
        assertEquals(mockMedia.getUrl(), newMedia.getUrl(), "Updated media URL should match");
        assertEquals(mockMedia.getType(), newMedia.getType(), "Updated media type should match");
        assertEquals(mockMedia.getMediaName(), newMedia.getMediaName(), "Updated media name should match");
        assertEquals(mockMedia.getSize(), newMedia.getSize(), "Updated media size should match");
    }

    @Test
    public void test_update_media_service_with_invalid_id() {
        // Arrange
        Long mediaId = 1L;
        Media mockMedia = new Media();

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.empty());

        // Act
        Optional<Media> result = mediaService.updateMedia(mediaId, mockMedia);

        // Assert
        assertTrue(result.isEmpty(), "Updated Media should not be returned");
    }
}
        