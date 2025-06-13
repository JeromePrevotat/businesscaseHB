package com.humanbooster.buisinessCase.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanbooster.buisinessCase.dto.MediaDTO;
import com.humanbooster.buisinessCase.mapper.MediaMapper;
import com.humanbooster.buisinessCase.model.Media;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.repository.MediaRepository;
import com.humanbooster.buisinessCase.repository.SpotRepository;
import com.humanbooster.buisinessCase.repository.StationRepository;
import com.humanbooster.buisinessCase.repository.UserRepository;
import com.humanbooster.buisinessCase.service.MediaService;

@WebMvcTest(MediaController.class)
@Import(MediaMapper.class)
public class MediaControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MediaService mediaService;
    @MockitoBean
    private MediaRepository mediaRepository;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private SpotRepository spotRepository;
    @MockitoBean
    private StationRepository stationRepository;
    @Autowired
    private MediaMapper mediaMapper;

    private Media mockTemplateMedia;
    private MediaDTO mockTemplateMediaDTO;

    @BeforeEach
    public void setUp() {
        this.mockTemplateMedia = new Media();
        this.mockTemplateMedia.setId(1L);
        this.mockTemplateMedia.setUrl("http://example.com/test.jpg");
        this.mockTemplateMedia.setType("image/jpeg");
        this.mockTemplateMedia.setMediaName("Test Media");
        this.mockTemplateMedia.setSize(1024L);
        this.mockTemplateMedia.setUser(new User());

        this.mockTemplateMediaDTO = new MediaDTO();
        this.mockTemplateMediaDTO.setId(1L);
        this.mockTemplateMediaDTO.setUrl("http://example.com/test.jpg");
        this.mockTemplateMediaDTO.setType("image/jpeg");
        this.mockTemplateMediaDTO.setMediaName("Test Media");
        this.mockTemplateMediaDTO.setSize(1024L);
        this.mockTemplateMediaDTO.setUser_id(1L);
        this.mockTemplateMediaDTO.setSpot_id(null);
        this.mockTemplateMediaDTO.setStation_id(null);
    }

    @Test
    public void test_get_all_media_route() throws Exception {
        // Arrange == set up mock result
        List<Media> mockMediaList = new ArrayList<>();
        Media mockMedia = this.mockTemplateMedia;
        mockMediaList.add(mockMedia);

        // Mock Behaviour
        given(mediaService.getAllMedia()).willReturn(mockMediaList);

        // Act & Assert
        mockMvc.perform(get("/api/medias"))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Status should be 200 OK"))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString(), "Response body should not be null"))
                .andExpect(result -> assertTrue(!result.getResponse().getContentAsString().isEmpty(), "Response body should not be empty"));
    }

    @Test
    public void test_get_media_route_with_id() throws Exception {
        // Arrange
        Long idToGet = 1L;
        Media mockMedia = this.mockTemplateMedia;
        given(mediaService.getMediaById(idToGet)).willReturn(Optional.of(mockMedia));

        // Act & Assert
        MvcResult mvcResult = mockMvc.perform(get("/api/medias/" + idToGet))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        MediaDTO responseMediaDTO = new ObjectMapper().readValue(content, MediaDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

        MediaDTO expectedMediaDTO = mediaMapper.toDTO(mockMedia);

        // Check all Fields match
        Field[] responseFields = responseMediaDTO.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedMediaDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedMediaDTO), responseField.get(responseMediaDTO),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_get_media_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToGet = 999L;
        given(mediaService.getMediaById(idToGet)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/medias/" + idToGet))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_save_media_route() throws Exception   {
        // Arrange
        MediaDTO newMediaDTO = this.mockTemplateMediaDTO;

        Media mockMediaService = new Media();
        mockMediaService.setId(1L);
        mockMediaService.setUrl("http://example.com/test.jpg");
        mockMediaService.setType("image/jpeg");
        mockMediaService.setMediaName("Test Save Media");
        mockMediaService.setSize(2048L);
        mockMediaService.setUser(new User());
        mockMediaService.setSpot(null);
        mockMediaService.setStation(null);
        given(mediaService.saveMedia(any(Media.class))).willReturn(mockMediaService);

        // ACT
        MvcResult mvcResult = mockMvc.perform(post("/api/medias")
                .content(new ObjectMapper().writeValueAsString(newMediaDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String content = mvcResult.getResponse().getContentAsString();
        MediaDTO responseMedia = new ObjectMapper().readValue(content, MediaDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        assertNotNull(content, "Response body should not be null");
        assertEquals("Test Save Media", responseMedia.getMediaName(), "Media name should match");
        assertEquals("http://example.com/test.jpg", responseMedia.getUrl(), "Media URL should match");
    }

    @Test
    public void test_delete_media_route_with_id() throws Exception {
        // Arrange
        Long idToDelete = 1L;
        Media mockMedia = this.mockTemplateMedia;
        mockMedia.setId(idToDelete);
        given(mediaService.deleteMediaById(idToDelete)).willReturn(Optional.of(mockMedia));

        // Act & Assert
        mockMvc.perform(delete("/api/medias/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Status should be 204 No Content"));
    }

    @Test
    public void test_delete_media_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToDelete = 999L;
        given(mediaService.deleteMediaById(idToDelete)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/medias/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_update_media_route() throws Exception {
        // Arrange
        Long idToUpdate = 1L;
        Media mockMedia = this.mockTemplateMedia;

        given(mediaService.updateMedia(any(Long.class), any(Media.class))).willReturn(Optional.of(mockMedia));

        // Create MediaDTO to send in the request
        MediaDTO newMediaDTO = new MediaDTO();
        newMediaDTO.setId(idToUpdate);
        newMediaDTO.setUrl("http://example.com/update-test.jpg");
        newMediaDTO.setType("image/jpeg");
        newMediaDTO.setMediaName("Updated Media");
        newMediaDTO.setUser_id(1L);
        newMediaDTO.setSpot_id(null);
        newMediaDTO.setStation_id(null);

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/api/medias/" + idToUpdate)
                .content(new ObjectMapper().writeValueAsString(newMediaDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        String content = mvcResult.getResponse().getContentAsString();
        MediaDTO responseMediaDTO = new ObjectMapper().readValue(content, MediaDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

        MediaDTO expectedMediaDTO = mediaMapper.toDTO(mockMedia);

        // Check all Fields match
        Field[] responseFields = responseMediaDTO.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedMediaDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedMediaDTO), responseField.get(responseMediaDTO),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_update_media_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToUpdate = 999L;
        given(mediaService.updateMedia(any(Long.class), any(Media.class))).willReturn(Optional.empty());

        // Create MediaDTO to send in the request
        MediaDTO newMediaDTO = this.mockTemplateMediaDTO;
        newMediaDTO.setId(idToUpdate);
        
        // Act & Assert
        mockMvc.perform(put("/api/medias/" + idToUpdate)
                .content(new ObjectMapper().writeValueAsString(newMediaDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }
}