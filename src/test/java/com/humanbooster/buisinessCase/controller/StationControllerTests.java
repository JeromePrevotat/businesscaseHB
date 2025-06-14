package com.humanbooster.buisinessCase.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.humanbooster.buisinessCase.dto.StationDTO;
import com.humanbooster.buisinessCase.mapper.StationMapper;
import com.humanbooster.buisinessCase.model.PlugType;
import com.humanbooster.buisinessCase.model.Spot;
import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.model.StationState;
import com.humanbooster.buisinessCase.repository.MediaRepository;
import com.humanbooster.buisinessCase.repository.PlugTypeRepository;
import com.humanbooster.buisinessCase.repository.ReservationRepository;
import com.humanbooster.buisinessCase.repository.SpotRepository;
import com.humanbooster.buisinessCase.repository.StationRepository;
import com.humanbooster.buisinessCase.service.StationService;

@WebMvcTest(StationController.class)
@Import(StationMapper.class)
public class StationControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StationService stationService;
    @MockitoBean
    private SpotRepository spotRepository;
    @MockitoBean
    private StationRepository stationRepository;
    @MockitoBean
    private MediaRepository mediaRepository;
    @MockitoBean
    private ReservationRepository reservationRepository;
    @MockitoBean
    private PlugTypeRepository plugTypeRepository;

    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private ObjectMapper objectMapper;

    private Station mockTemplateStation;
    private StationDTO mockTemplateStationDTO;

    @BeforeEach
    public void setUp() {
        this.mockTemplateStation = new Station();
        this.mockTemplateStation.setId(1L);
        this.mockTemplateStation.setStationName("Test Station");
        this.mockTemplateStation.setLatitude(new BigDecimal("48.8566"));
        this.mockTemplateStation.setLongitude(new BigDecimal("2.3522"));
        this.mockTemplateStation.setPriceRate(new BigDecimal("0.25"));
        this.mockTemplateStation.setPowerOutput(new BigDecimal("22.0"));
        this.mockTemplateStation.setManual("Test");
        this.mockTemplateStation.setState(StationState.ACTIVE);
        this.mockTemplateStation.setGrounded(true);
        this.mockTemplateStation.setBusy(false); 
        this.mockTemplateStation.setWired(false);
        Spot spot = new Spot();
        spot.setId(1L);
        this.mockTemplateStation.setSpot(spot);
        this.mockTemplateStation.setReservationList(new ArrayList<>());
        this.mockTemplateStation.setMediaList(new ArrayList<>());
        PlugType plugType1 = new PlugType();
        plugType1.setId(1L);
        this.mockTemplateStation.setPlugType(Arrays.asList(plugType1));

        this.mockTemplateStationDTO = new StationDTO();
        this.mockTemplateStationDTO.setId(1L);
        this.mockTemplateStationDTO.setStationName("Station de Test");
        this.mockTemplateStationDTO.setLatitude(new BigDecimal("48.8566"));
        this.mockTemplateStationDTO.setLongitude(new BigDecimal("2.3522"));
        this.mockTemplateStationDTO.setPriceRate(new BigDecimal("0.25"));
        this.mockTemplateStationDTO.setPowerOutput(new BigDecimal("22.0"));
        this.mockTemplateStationDTO.setManual("Instructions pour utiliser cette station de recharge");
        this.mockTemplateStationDTO.setState(StationState.ACTIVE);
        this.mockTemplateStationDTO.setGrounded(true);
        this.mockTemplateStationDTO.setBusy(false);
        this.mockTemplateStationDTO.setWired(false);
        this.mockTemplateStationDTO.setSpot_id(1L);
        this.mockTemplateStationDTO.setReservationList(new ArrayList<>());
        this.mockTemplateStationDTO.setMediaList(new ArrayList<>());
        this.mockTemplateStationDTO.setPlugTypeList(new ArrayList<>());
    }

    @Test
    public void test_get_all_station_route() throws Exception {
        // Arrange == set up mock result
        List<Station> mockStationList = new ArrayList<>();
        Station mockStation = this.mockTemplateStation;
        mockStationList.add(mockStation);
        // Mock Behaviour
        given(stationService.getAllStations()).willReturn(mockStationList);

        // Act & Assert
        mockMvc.perform(get("/api/stations"))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Status should be 200 OK"))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString(), "Response body should not be null"))
                .andExpect(result -> assertTrue(!result.getResponse().getContentAsString().isEmpty(), "Response body should not be empty"));
    }

    @Test
    public void test_get_station_route_with_id() throws Exception {
        // Arrange
        Long idToGet = 1L;
        Station mockStation = this.mockTemplateStation;
        given(stationService.getStationById(idToGet)).willReturn(Optional.of(mockStation));

        // Act & Assert
        MvcResult mvcResult = mockMvc.perform(get("/api/stations/" + idToGet))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        StationDTO responseStationDTO = objectMapper.readValue(content, StationDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

        StationDTO expectedStationDTO = stationMapper.toDTO(mockStation);

        // Check all Fields match
        Field[] responseFields = responseStationDTO.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedStationDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedStationDTO), responseField.get(responseStationDTO),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_get_station_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToGet = 999L;
        given(stationService.getStationById(idToGet)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/stations/" + idToGet))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_save_station_route() throws Exception   {
        // Arrange
        StationDTO newStationDTO = this.mockTemplateStationDTO;

        Station mockStationService = new Station();
        mockStationService.setId(1L);

        given(stationService.saveStation(any(Station.class))).willReturn(mockStationService);

        // ACT
        MvcResult mvcResult = mockMvc.perform(post("/api/stations")
                .content(objectMapper.writeValueAsString(newStationDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String content = mvcResult.getResponse().getContentAsString();
        StationDTO responseStation = objectMapper.readValue(content, StationDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        assertNotNull(content, "Response body should not be null");
        assertEquals(newStationDTO.getId(), responseStation.getId(), "ID should match the mock value");
    }

    @Test
    public void test_delete_station_route_with_id() throws Exception {
        // Arrange
        Long idToDelete = 1L;
        Station mockStation = this.mockTemplateStation;
        mockStation.setId(idToDelete);
        given(stationService.deleteStationById(idToDelete)).willReturn(Optional.of(mockStation));

        // Act & Assert
        mockMvc.perform(delete("/api/stations/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Status should be 204 No Content"));
    }

    @Test
    public void test_delete_station_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToDelete = 999L;
        given(stationService.deleteStationById(idToDelete)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/stations/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_update_station_route() throws Exception {
        // Arrange
        Long idToUpdate = 1L;
        Station mockStation = this.mockTemplateStation;

        given(stationService.updateStation(any(Long.class), any(Station.class))).willReturn(Optional.of(mockStation));        // Create StationDTO to send in the request
        StationDTO newStationDTO = new StationDTO();
        newStationDTO.setId(idToUpdate);
        newStationDTO.setStationName(this.mockTemplateStationDTO.getStationName());
        newStationDTO.setLatitude(this.mockTemplateStationDTO.getLatitude());
        newStationDTO.setLongitude(this.mockTemplateStationDTO.getLongitude());
        newStationDTO.setPriceRate(this.mockTemplateStationDTO.getPriceRate());
        newStationDTO.setPowerOutput(this.mockTemplateStationDTO.getPowerOutput());
        newStationDTO.setManual(this.mockTemplateStationDTO.getManual());
        newStationDTO.setState(this.mockTemplateStationDTO.getState());
        newStationDTO.setGrounded(this.mockTemplateStationDTO.isGrounded());
        newStationDTO.setBusy(this.mockTemplateStationDTO.isBusy());
        newStationDTO.setWired(this.mockTemplateStationDTO.isWired());
        newStationDTO.setSpot_id(this.mockTemplateStationDTO.getSpot_id());
        newStationDTO.setReservationList(this.mockTemplateStationDTO.getReservationList());
        newStationDTO.setMediaList(this.mockTemplateStationDTO.getMediaList());
        newStationDTO.setPlugTypeList(this.mockTemplateStationDTO.getPlugTypeList());

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/api/stations/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newStationDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        StationDTO expectedStationDTO = stationMapper.toDTO(mockStation);
        String content = mvcResult.getResponse().getContentAsString();
        StationDTO responseStation = objectMapper.readValue(content, StationDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");


        // Check all Fields match
        Field[] responseFields = responseStation.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedStationDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedStationDTO), responseField.get(responseStation),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_update_station_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToUpdate = 999L;
        given(stationService.updateStation(any(Long.class), any(Station.class))).willReturn(Optional.empty());

        // Create StationDTO to send in the request
        StationDTO newStationDTO = this.mockTemplateStationDTO;
        newStationDTO.setId(idToUpdate);

        // Act & Assert
        mockMvc.perform(put("/api/stations/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newStationDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }
}