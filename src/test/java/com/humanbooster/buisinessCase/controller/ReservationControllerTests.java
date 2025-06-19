package com.humanbooster.buisinessCase.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import com.humanbooster.buisinessCase.dto.ReservationDTO;
import com.humanbooster.buisinessCase.mapper.ReservationMapper;
import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.model.ReservationState;
import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.repository.StationRepository;
import com.humanbooster.buisinessCase.repository.UserRepository;
import com.humanbooster.buisinessCase.service.JwtService;
import com.humanbooster.buisinessCase.service.ReservationService;

@WebMvcTest(ReservationController.class)
@Import(ReservationMapper.class)
// Disable security filters for testing
@AutoConfigureMockMvc(addFilters = false)
public class ReservationControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private StationRepository stationRepository;

    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private ObjectMapper objectMapper;

    private Reservation mockTemplateReservation;
    private ReservationDTO mockTemplateReservationDTO;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        this.mockTemplateReservation = new Reservation();
        this.mockTemplateReservation.setId(1L);
        this.mockTemplateReservation.setCreatedAt(now);
        this.mockTemplateReservation.setValidatedAt(now.plusDays(1));
        this.mockTemplateReservation.setStartDate(now.plusDays(2));
        this.mockTemplateReservation.setEndDate(now.plusDays(3));
        this.mockTemplateReservation.setHourlyRateLog(BigDecimal.valueOf(10.0));
        this.mockTemplateReservation.setState(ReservationState.PENDING);
        this.mockTemplateReservation.setPayed(false);
        this.mockTemplateReservation.setDatePayed(null);
        User user = new User();
        user.setId(1L);
        this.mockTemplateReservation.setUser(user);
        Station station = new Station();
        station.setId(1L);
        this.mockTemplateReservation.setStation(station);

        this.mockTemplateReservationDTO = new ReservationDTO();
        this.mockTemplateReservationDTO.setId(1L);
        this.mockTemplateReservationDTO.setCreatedAt(now);
        this.mockTemplateReservationDTO.setValidatedAt(now.plusDays(1));
        this.mockTemplateReservationDTO.setStartDate(now.plusDays(2));
        this.mockTemplateReservationDTO.setEndDate(now.plusDays(3));
        this.mockTemplateReservationDTO.setHourlyRateLog(BigDecimal.valueOf(10.0));
        this.mockTemplateReservationDTO.setState(ReservationState.PENDING);
        this.mockTemplateReservationDTO.setPayed(false);
        this.mockTemplateReservationDTO.setDatePayed(null);
        this.mockTemplateReservationDTO.setUser_id(1L);
        this.mockTemplateReservationDTO.setStation_id(1L);
    }

    @Test
    public void test_get_all_reservation_route() throws Exception {
        // Arrange == set up mock result
        List<Reservation> mockReservations = new ArrayList<>();
        Reservation mockReservation = this.mockTemplateReservation;
        mockReservations.add(mockReservation);
        // Mock Behaviour
        given(reservationService.getAllReservations()).willReturn(mockReservations);

        // Act & Assert
        mockMvc.perform(get("/api/reservations"))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Status should be 200 OK"))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString(), "Response body should not be null"))
                .andExpect(result -> assertTrue(!result.getResponse().getContentAsString().isEmpty(), "Response body should not be empty"));
    }

    @Test
    public void test_get_reservation_route_with_id() throws Exception {
        // Arrange
        Long idToGet = 1L;
        Reservation mockReservation = this.mockTemplateReservation;
        given(reservationService.getReservationById(idToGet)).willReturn(Optional.of(mockReservation));

        // Act & Assert
        MvcResult mvcResult = mockMvc.perform(get("/api/reservations/" + idToGet))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ReservationDTO responseReservationDTO = objectMapper.readValue(content, ReservationDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

        ReservationDTO expectedReservationDTO = reservationMapper.toDTO(mockReservation);

        // Check all Fields match
        Field[] responseFields = responseReservationDTO.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedReservationDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedReservationDTO), responseField.get(responseReservationDTO),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_get_reservation_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToGet = 999L;
        given(reservationService.getReservationById(idToGet)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/reservations/" + idToGet))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_save_reservation_route() throws Exception   {
        // Arrange
        ReservationDTO newReservationDTO = this.mockTemplateReservationDTO;

        Reservation mockReservationService = new Reservation();
        mockReservationService.setId(1L);
        mockReservationService.setCreatedAt(newReservationDTO.getCreatedAt());
        mockReservationService.setValidatedAt(newReservationDTO.getValidatedAt());
        mockReservationService.setStartDate(newReservationDTO.getStartDate());
        mockReservationService.setEndDate(newReservationDTO.getEndDate());
        mockReservationService.setHourlyRateLog(newReservationDTO.getHourlyRateLog());
        mockReservationService.setState(newReservationDTO.getState());
        mockReservationService.setPayed(newReservationDTO.isPayed());
        mockReservationService.setDatePayed(newReservationDTO.getDatePayed());
        mockReservationService.setUser(new User());
        mockReservationService.setStation(new Station());
        given(reservationService.saveReservation(any(Reservation.class))).willReturn(mockReservationService);

        // ACT
        MvcResult mvcResult = mockMvc.perform(post("/api/reservations")
                .content(objectMapper.writeValueAsString(newReservationDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String content = mvcResult.getResponse().getContentAsString();
        ReservationDTO responseReservation = objectMapper.readValue(content, ReservationDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        assertNotNull(content, "Response body should not be null");
        assertEquals(newReservationDTO.getId(), responseReservation.getId(), "ID should match the mock value");
        assertEquals(newReservationDTO.getCreatedAt(), responseReservation.getCreatedAt(), "CreatedAt should match the mock value");
        assertEquals(newReservationDTO.getValidatedAt(), responseReservation.getValidatedAt(), "ValidatedAt should match the mock value");
        assertEquals(newReservationDTO.getStartDate(), responseReservation.getStartDate(), "StartDate should match the mock value");
        assertEquals(newReservationDTO.getEndDate(), responseReservation.getEndDate(), "EndDate should match the mock value");
        assertEquals(newReservationDTO.getHourlyRateLog(), responseReservation.getHourlyRateLog(), "HourlyRateLog should match the mock value");
        assertEquals(newReservationDTO.getState(), responseReservation.getState(), "State should match the mock value");
        assertEquals(newReservationDTO.isPayed(), responseReservation.isPayed(), "Payed should match the mock value");
        assertEquals(newReservationDTO.getDatePayed(), responseReservation.getDatePayed(), "DatePayed should match the mock value");
    }

    @Test
    public void test_delete_reservation_route_with_id() throws Exception {
        // Arrange
        Long idToDelete = 1L;
        Reservation mockReservation = this.mockTemplateReservation;
        mockReservation.setId(idToDelete);
        given(reservationService.deleteReservationById(idToDelete)).willReturn(Optional.of(mockReservation));

        // Act & Assert
        mockMvc.perform(delete("/api/reservations/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Status should be 204 No Content"));
    }

    @Test
    public void test_delete_reservation_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToDelete = 999L;
        given(reservationService.deleteReservationById(idToDelete)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/reservations/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_update_reservation_route() throws Exception {
        // Arrange
        Long idToUpdate = 1L;
        Reservation mockReservation = this.mockTemplateReservation;

        given(reservationService.updateReservation(any(Long.class), any(Reservation.class))).willReturn(Optional.of(mockReservation));

        // Create ReservationDTO to send in the request
        ReservationDTO newReservationDTO = new ReservationDTO();
        newReservationDTO.setId(idToUpdate);
        newReservationDTO.setCreatedAt(this.mockTemplateReservation.getCreatedAt());
        newReservationDTO.setValidatedAt(this.mockTemplateReservation.getValidatedAt());
        newReservationDTO.setStartDate(this.mockTemplateReservation.getStartDate());
        newReservationDTO.setEndDate(this.mockTemplateReservation.getEndDate());
        newReservationDTO.setHourlyRateLog(this.mockTemplateReservation.getHourlyRateLog());
        newReservationDTO.setState(ReservationState.PAST);
        newReservationDTO.setPayed(true);
        newReservationDTO.setDatePayed(this.mockTemplateReservation.getDatePayed());
        newReservationDTO.setUser_id(1L);
        newReservationDTO.setStation_id(1L);

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/api/reservations/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newReservationDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        ReservationDTO expectedReservationDTO = reservationMapper.toDTO(mockReservation);
        String content = mvcResult.getResponse().getContentAsString();
        ReservationDTO responseReservation = objectMapper.readValue(content, ReservationDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");


        // Check all Fields match
        Field[] responseFields = responseReservation.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedReservationDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedReservationDTO), responseField.get(responseReservation),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_update_reservation_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToUpdate = 999L;
        given(reservationService.updateReservation(any(Long.class), any(Reservation.class))).willReturn(Optional.empty());

        // Create ReservationDTO to send in the request
        ReservationDTO newReservationDTO = this.mockTemplateReservationDTO;
        newReservationDTO.setId(idToUpdate);

        // Act & Assert
        mockMvc.perform(put("/api/reservations/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newReservationDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }
}