package com.humanbooster.buisinessCase.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.humanbooster.buisinessCase.dto.AdressDTO;
import com.humanbooster.buisinessCase.dto.PlugTypeDTO;
import com.humanbooster.buisinessCase.mapper.PlugTypeMapper;
import com.humanbooster.buisinessCase.model.PlugType;
import com.humanbooster.buisinessCase.repository.PlugTypeRepository;
import com.humanbooster.buisinessCase.repository.StationRepository;
import com.humanbooster.buisinessCase.repository.VehiculeRepository;
import com.humanbooster.buisinessCase.service.PlugTypeService;


@WebMvcTest(PlugTypeController.class)
@Import(PlugTypeMapper.class)
public class PlugTypeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlugTypeService plugTypeService;
    @MockitoBean
    private PlugTypeRepository plugTypeRepository;
    @MockitoBean
    private VehiculeRepository vehiculeRepository;
    @MockitoBean
    private StationRepository stationRepository;



    @Autowired
    private PlugTypeMapper plugTypeMapper;

    private PlugType mockTemplatePlugType;
    private PlugTypeDTO mockTemplatePlugTypeDTO;

    @BeforeEach
    public void setUp() {
        this.mockTemplatePlugType = new PlugType();
        this.mockTemplatePlugType.setId(1L);
        this.mockTemplatePlugType.setPlugname("Test Plug Type");
        this.mockTemplatePlugType.setVehiculeList(new HashSet<>());
        this.mockTemplatePlugType.setStationList(new HashSet<>());


        this.mockTemplatePlugTypeDTO = new PlugTypeDTO();
        this.mockTemplatePlugTypeDTO.setId(1L);
        this.mockTemplatePlugTypeDTO.setPlugname("Test Plug Type");
        this.mockTemplatePlugTypeDTO.setVehicule_id(new ArrayList<>());
        this.mockTemplatePlugTypeDTO.setStation_id(new ArrayList<>());
    }

    @Test
    public void test_get_all_plugtype_route() throws Exception {
        // Arrange == set up mock result
        List<PlugType> mockPlugTypes = new ArrayList<>();
        PlugType mockPlugType = this.mockTemplatePlugType;
        mockPlugTypes.add(mockPlugType);
        // Mock Behaviour
        given(plugTypeService.getAllPlugTypes()).willReturn(mockPlugTypes);

        // Act & Assert
        mockMvc.perform(get("/api/plugtypes"))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Status should be 200 OK"))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString(), "Response body should not be null"))
                .andExpect(result -> assertTrue(!result.getResponse().getContentAsString().isEmpty(), "Response body should not be empty"));
    }

    @Test
    public void test_get_plugtype_route_with_id() throws Exception {
        // Arrange
        Long idToGet = 1L;
        PlugType mockPlugType = this.mockTemplatePlugType;
        given(plugTypeService.getPlugTypeById(idToGet)).willReturn(Optional.of(mockPlugType));

        // Act & Assert
        MvcResult mvcResult = mockMvc.perform(get("/api/plugtypes/" + idToGet))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        PlugTypeDTO responsePlugTypeDTO = new ObjectMapper().readValue(content, PlugTypeDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

        PlugTypeDTO expectedPlugTypeDTO = plugTypeMapper.toDTO(mockPlugType);

        // Check all Fields match
        Field[] responseFields = responsePlugTypeDTO.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedPlugTypeDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedPlugTypeDTO), responseField.get(responsePlugTypeDTO),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_get_plugtype_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToGet = 999L;
        given(plugTypeService.getPlugTypeById(idToGet)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/plugtypes/" + idToGet))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_save_plugtype_route() throws Exception   {
        // Arrange
        PlugTypeDTO newPlugTypeDTO = this.mockTemplatePlugTypeDTO;

        PlugType mockPlugTypeService = new PlugType();
        mockPlugTypeService.setId(1L);
        mockPlugTypeService.setPlugname("Test Save PlugType");
        mockPlugTypeService.setVehiculeList(new HashSet<>());
        mockPlugTypeService.setStationList(new HashSet<>());
        given(plugTypeService.savePlugType(any(PlugType.class))).willReturn(mockPlugTypeService);

        // ACT
        MvcResult mvcResult = mockMvc.perform(post("/api/plugtypes")
                .content(new ObjectMapper().writeValueAsString(newPlugTypeDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        PlugTypeDTO responsePlugType = new ObjectMapper().readValue(content, PlugTypeDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        assertNotNull(content, "Response body should not be null");
        assertEquals("Test Save PlugType", responsePlugType.getPlugname(), "PlugType name should match");
    }

    @Test
    public void test_delete_plugtype_route_with_id() throws Exception {
        // Arrange
        Long idToDelete = 1L;
        PlugType mockPlugType = this.mockTemplatePlugType;
        mockPlugType.setId(idToDelete);
        given(plugTypeService.deletePlugTypeById(idToDelete)).willReturn(Optional.of(mockPlugType));

        // Act & Assert
        mockMvc.perform(delete("/api/plugtypes/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Status should be 204 No Content"));
    }

    @Test
    public void test_delete_plugtype_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToDelete = 999L;
        given(plugTypeService.deletePlugTypeById(idToDelete)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/plugtypes/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_update_plugtype_route() throws Exception {
        // Arrange
        Long idToUpdate = 1L;
        PlugType mockPlugType = this.mockTemplatePlugType;
        given(plugTypeService.savePlugType(any(PlugType.class))).willReturn(mockPlugType);

        // Create PlugTypeDTO to send in the request
        PlugTypeDTO newPlugTypeDTO = new PlugTypeDTO();
        newPlugTypeDTO.setId(idToUpdate);
        newPlugTypeDTO.setPlugname("Updated Plug Type");
        newPlugTypeDTO.setVehicule_id(new ArrayList<>());
        newPlugTypeDTO.setStation_id(new ArrayList<>());

        // ACT
        PlugTypeDTO expectedPlugTypeDTO = plugTypeMapper.toDTO(mockPlugType);
        MvcResult mvcResult = mockMvc.perform(post("/api/plugtypes")
                .content(new ObjectMapper().writeValueAsString(newPlugTypeDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String content = mvcResult.getResponse().getContentAsString();
        PlugTypeDTO responsePlugType = new ObjectMapper().readValue(content, PlugTypeDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        assertNotNull(content, "Response body should not be null");

        // Check all Fields match
        Field[] responseFields = responsePlugType.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedPlugTypeDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedPlugTypeDTO), responseField.get(responsePlugType),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_update_plugtype_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToUpdate = 999L;
        given(plugTypeService.updatePlugType(any(Long.class), any(PlugType.class))).willReturn(Optional.empty());

        // Create PlugTypeDTO to send in the request
        PlugTypeDTO newPlugTypeDTO = this.mockTemplatePlugTypeDTO;
        newPlugTypeDTO.setId(idToUpdate);

        // Act & Assert
        mockMvc.perform(put("/api/plugtypes/" + idToUpdate)
                .content(new ObjectMapper().writeValueAsString(newPlugTypeDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

}