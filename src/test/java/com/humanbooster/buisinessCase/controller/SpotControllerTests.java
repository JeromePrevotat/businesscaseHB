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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import com.humanbooster.buisinessCase.dto.SpotDTO;
import com.humanbooster.buisinessCase.mapper.SpotMapper;
import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.model.Spot;
import com.humanbooster.buisinessCase.repository.AdressRepository;
import com.humanbooster.buisinessCase.repository.MediaRepository;
import com.humanbooster.buisinessCase.service.JwtService;
import com.humanbooster.buisinessCase.service.SpotService;

@WebMvcTest(SpotController.class)
@Import(SpotMapper.class)
// Disable security filters for testing
@AutoConfigureMockMvc(addFilters = false)
public class SpotControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private SpotService spotService;
    @MockitoBean
    private AdressRepository adressRepository;
    @MockitoBean
    private MediaRepository mediaRepository;

    @Autowired
    private SpotMapper spotMapper;
    @Autowired
    private ObjectMapper objectMapper;

    private Spot mockTemplateSpot;
    private SpotDTO mockTemplateSpotDTO;

    @BeforeEach
    public void setUp() {
        this.mockTemplateSpot = new Spot();
        this.mockTemplateSpot.setId(1L);
        this.mockTemplateSpot.setInstruction("Test Instruction");
        this.mockTemplateSpot.setStationList(new ArrayList<>());
        Adress adress = new Adress();
        adress.setId(1L);
        this.mockTemplateSpot.setAdress(adress);
        this.mockTemplateSpot.setMediaList(new ArrayList<>());
        
        this.mockTemplateSpotDTO = new SpotDTO();
        this.mockTemplateSpotDTO.setId(1L);
        this.mockTemplateSpotDTO.setInstruction("Test Instruction");
        this.mockTemplateSpotDTO.setStationList(new ArrayList<>());
        this.mockTemplateSpotDTO.setAddress_id(1L);
        this.mockTemplateSpotDTO.setMediaList(new ArrayList<>());
    }

    @Test
    public void test_get_all_spot_route() throws Exception {
        // Arrange == set up mock result
        List<Spot> mockSpotList = new ArrayList<>();
        Spot mockSpot = this.mockTemplateSpot;
        mockSpotList.add(mockSpot);
        // Mock Behaviour
        given(spotService.getAllSpots()).willReturn(mockSpotList);

        // Act & Assert
        mockMvc.perform(get("/api/spots"))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Status should be 200 OK"))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString(), "Response body should not be null"))
                .andExpect(result -> assertTrue(!result.getResponse().getContentAsString().isEmpty(), "Response body should not be empty"));
    }

    @Test
    public void test_get_reservation_route_with_id() throws Exception {
        // Arrange
        Long idToGet = 1L;
        Spot mockSpot = this.mockTemplateSpot;
        given(spotService.getSpotById(idToGet)).willReturn(Optional.of(mockSpot));

        // Act & Assert
        MvcResult mvcResult = mockMvc.perform(get("/api/spots/" + idToGet))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        SpotDTO responseSpotDTO = objectMapper.readValue(content, SpotDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

        SpotDTO expectedSpotDTO = spotMapper.toDTO(mockSpot);

        // Check all Fields match
        Field[] responseFields = responseSpotDTO.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedSpotDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedSpotDTO), responseField.get(responseSpotDTO),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_get_spot_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToGet = 999L;
        given(spotService.getSpotById(idToGet)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/spots/" + idToGet))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_save_spot_route() throws Exception   {
        // Arrange
        SpotDTO newSpotDTO = this.mockTemplateSpotDTO;

        Spot mockSpotService = new Spot();
        mockSpotService.setId(1L);

        given(spotService.saveSpot(any(Spot.class))).willReturn(mockSpotService);

        // ACT
        MvcResult mvcResult = mockMvc.perform(post("/api/spots")
                .content(objectMapper.writeValueAsString(newSpotDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String content = mvcResult.getResponse().getContentAsString();
        SpotDTO responseSpot = objectMapper.readValue(content, SpotDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        assertNotNull(content, "Response body should not be null");
        assertEquals(newSpotDTO.getId(), responseSpot.getId(), "ID should match the mock value");
    }

    @Test
    public void test_delete_spot_route_with_id() throws Exception {
        // Arrange
        Long idToDelete = 1L;
        Spot mockSpot = this.mockTemplateSpot;
        mockSpot.setId(idToDelete);
        given(spotService.deleteSpotById(idToDelete)).willReturn(Optional.of(mockSpot));

        // Act & Assert
        mockMvc.perform(delete("/api/spots/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Status should be 204 No Content"));
    }

    @Test
    public void test_delete_spot_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToDelete = 999L;
        given(spotService.deleteSpotById(idToDelete)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/spots/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_update_spot_route() throws Exception {
        // Arrange
        Long idToUpdate = 1L;
        Spot mockSpot = this.mockTemplateSpot;

        given(spotService.updateSpot(any(Long.class), any(Spot.class))).willReturn(Optional.of(mockSpot));

        // Create SpotDTO to send in the request
        SpotDTO newSpotDTO = new SpotDTO();
        newSpotDTO.setId(idToUpdate);
        newSpotDTO.setInstruction("Instructions Updated");
        newSpotDTO.setStationList(new ArrayList<>());
        newSpotDTO.setAddress_id(1L);
        newSpotDTO.setMediaList(new ArrayList<>());

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/api/spots/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newSpotDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        SpotDTO expectedSpotDTO = spotMapper.toDTO(mockSpot);
        String content = mvcResult.getResponse().getContentAsString();
        SpotDTO responseSpot = objectMapper.readValue(content, SpotDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");


        // Check all Fields match
        Field[] responseFields = responseSpot.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedSpotDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedSpotDTO), responseField.get(responseSpot),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_update_spot_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToUpdate = 999L;
        given(spotService.updateSpot(any(Long.class), any(Spot.class))).willReturn(Optional.empty());

        // Create SpotDTO to send in the request
        SpotDTO newSpotDTO = this.mockTemplateSpotDTO;
        newSpotDTO.setId(idToUpdate);

        // Act & Assert
        mockMvc.perform(put("/api/spots/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newSpotDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }
}