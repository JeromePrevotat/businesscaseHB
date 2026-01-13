package com.humanbooster.businesscase.controller;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanbooster.businesscase.dto.StationDTO;
import com.humanbooster.businesscase.mapper.ReservationMapper;
import com.humanbooster.businesscase.mapper.StationMapper;
import com.humanbooster.businesscase.mapper.UserMapper;
import com.humanbooster.businesscase.model.PlugType;
import com.humanbooster.businesscase.model.Spot;
import com.humanbooster.businesscase.model.Station;
import com.humanbooster.businesscase.model.StationState;
import com.humanbooster.businesscase.model.User;
import com.humanbooster.businesscase.service.RefreshTokenService;
import com.humanbooster.businesscase.service.ReservationService;
import com.humanbooster.businesscase.service.StationService;
import com.humanbooster.businesscase.service.UserService;

@WebMvcTest(controllers = StationController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class,
                                SecurityFilterAutoConfiguration.class},
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.humanbooster\\.businesscase\\.security\\..*"))

// Disable security filters for testing
@AutoConfigureMockMvc(addFilters = false)
public class StationControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RefreshTokenService refreshTokenService;
    @MockitoBean
    private StationService stationService;
    @MockitoBean
    private StationMapper mapper;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserMapper userMapper;
    @MockitoBean
    private ReservationService reservationService;
    @MockitoBean
    private ReservationMapper reservationMapper;

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
        given(mapper.toDTO(any(Station.class))).willReturn(this.mockTemplateStationDTO);


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
        StationDTO expectedStationDTO = this.mockTemplateStationDTO;
        given(stationService.getStationById(idToGet)).willReturn(Optional.of(mockStation));
        given(mapper.toDTO(any(Station.class))).willReturn(expectedStationDTO);
        // Act & Assert
        MvcResult mvcResult = mockMvc.perform(get("/api/stations/" + idToGet))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        StationDTO responseStationDTO = objectMapper.readValue(content, StationDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

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
    @Disabled("Requires @AuthenticationPrincipal which is not supported with @WebMvcTest(addFilters=false). TODO: Use integration tests or refactor controller.")
    @WithMockUser(username = "test@test.com", roles = {"USER"})
    public void test_save_station_route() throws Exception   {
        // Arrange
        StationDTO newStationDTO = this.mockTemplateStationDTO;
        Station savedStation = this.mockTemplateStation;
        StationDTO savedStationDTO = this.mockTemplateStationDTO;
        
        // Create a mock User for @AuthenticationPrincipal
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@test.com");

        given(mapper.toEntity(any(StationDTO.class))).willReturn(savedStation);
        given(stationService.saveStation(any(Station.class))).willReturn(savedStation);
        given(mapper.toDTO(any(Station.class))).willReturn(savedStationDTO);

        // ACT
        MvcResult mvcResult = mockMvc.perform(post("/api/stations")
                .with(user(mockUser))
                .content(objectMapper.writeValueAsString(newStationDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String content = mvcResult.getResponse().getContentAsString();
        StationDTO responseStation = objectMapper.readValue(content, StationDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        assertNotNull(content, "Response body should not be null");
        assertEquals(savedStationDTO.getId(), responseStation.getId(), "ID should match the mock value");
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
        
        // Create DIFFERENT data for the update to verify actual changes
        StationDTO newStationDTO = new StationDTO();
        newStationDTO.setId(1L);
        newStationDTO.setStationName("Station Mise à Jour"); // DIFFERENT from template
        newStationDTO.setLatitude(new BigDecimal("45.7640")); // DIFFERENT from template
        newStationDTO.setLongitude(new BigDecimal("4.8357")); // DIFFERENT from template
        newStationDTO.setPriceRate(new BigDecimal("0.30")); // DIFFERENT from template
        newStationDTO.setPowerOutput(new BigDecimal("50.0")); // DIFFERENT from template
        newStationDTO.setManual("Manuel mis à jour"); // DIFFERENT from template
        newStationDTO.setState(StationState.ACTIVE);
        newStationDTO.setGrounded(true);
        newStationDTO.setBusy(true); // DIFFERENT from template
        newStationDTO.setWired(true); // DIFFERENT from template
        newStationDTO.setSpot_id(1L);
        newStationDTO.setReservationList(new ArrayList<>());
        newStationDTO.setMediaList(new ArrayList<>());
        newStationDTO.setPlugTypeList(new ArrayList<>());

        // Create updated Station with the new values
        Station updatedStation = new Station();
        updatedStation.setId(1L);
        updatedStation.setStationName("Station Mise à Jour");
        updatedStation.setLatitude(new BigDecimal("45.7640"));
        updatedStation.setLongitude(new BigDecimal("4.8357"));
        updatedStation.setPriceRate(new BigDecimal("0.30"));
        updatedStation.setPowerOutput(new BigDecimal("50.0"));
        updatedStation.setManual("Manuel mis à jour");
        updatedStation.setState(StationState.ACTIVE);
        updatedStation.setGrounded(true);
        updatedStation.setBusy(true);
        updatedStation.setWired(true);
        Spot spot = new Spot();
        spot.setId(1L);
        updatedStation.setSpot(spot);
        updatedStation.setReservationList(new ArrayList<>());
        updatedStation.setMediaList(new ArrayList<>());
        PlugType plugType1 = new PlugType();
        plugType1.setId(1L);
        updatedStation.setPlugType(Arrays.asList(plugType1));

        // Create expected DTO with updated values
        StationDTO expectedStationDTO = new StationDTO();
        expectedStationDTO.setId(1L);
        expectedStationDTO.setStationName("Station Mise à Jour");
        expectedStationDTO.setLatitude(new BigDecimal("45.7640"));
        expectedStationDTO.setLongitude(new BigDecimal("4.8357"));
        expectedStationDTO.setPriceRate(new BigDecimal("0.30"));
        expectedStationDTO.setPowerOutput(new BigDecimal("50.0"));
        expectedStationDTO.setManual("Manuel mis à jour");
        expectedStationDTO.setState(StationState.ACTIVE);
        expectedStationDTO.setGrounded(true);
        expectedStationDTO.setBusy(true);
        expectedStationDTO.setWired(true);
        expectedStationDTO.setSpot_id(1L);
        expectedStationDTO.setReservationList(new ArrayList<>());
        expectedStationDTO.setMediaList(new ArrayList<>());
        expectedStationDTO.setPlugTypeList(new ArrayList<>());

        given(mapper.toEntity(any(StationDTO.class))).willReturn(updatedStation);
        given(stationService.updateStation(any(Long.class), any(Station.class))).willReturn(Optional.of(updatedStation));
        given(mapper.toDTO(any(Station.class))).willReturn(expectedStationDTO);


        // Act
        MvcResult mvcResult = mockMvc.perform(put("/api/stations/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newStationDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        String content = mvcResult.getResponse().getContentAsString();
        StationDTO responseStation = objectMapper.readValue(content, StationDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

        // Verify that specific fields have been updated to the new values
        assertEquals("Station Mise à Jour", responseStation.getStationName(), "Station name should be updated");
        assertEquals(new BigDecimal("45.7640"), responseStation.getLatitude(), "Latitude should be updated");
        assertEquals(new BigDecimal("4.8357"), responseStation.getLongitude(), "Longitude should be updated");
        assertEquals(new BigDecimal("0.30"), responseStation.getPriceRate(), "Price rate should be updated");
        assertEquals(new BigDecimal("50.0"), responseStation.getPowerOutput(), "Power output should be updated");
        assertEquals("Manuel mis à jour", responseStation.getManual(), "Manual should be updated");
        assertEquals(true, responseStation.isBusy(), "Busy status should be updated");
        assertEquals(true, responseStation.isWired(), "Wired status should be updated");

        // Verify that values are DIFFERENT from the original template
        assertTrue(!this.mockTemplateStationDTO.getStationName().equals(responseStation.getStationName()), 
                   "Station name should be different from template");
        assertTrue(!this.mockTemplateStationDTO.getPriceRate().equals(responseStation.getPriceRate()), 
                   "Price rate should be different from template");
        assertTrue(this.mockTemplateStationDTO.isBusy() != responseStation.isBusy(), 
                   "Busy status should be different from template");
        assertTrue(this.mockTemplateStationDTO.isWired() != responseStation.isWired(), 
                   "Wired status should be different from template");
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