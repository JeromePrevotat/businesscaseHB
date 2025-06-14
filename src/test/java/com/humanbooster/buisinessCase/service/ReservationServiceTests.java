package com.humanbooster.buisinessCase.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.model.ReservationState;
import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTests {
    @Mock
    private ReservationRepository reservationRepository;
    @InjectMocks
    private ReservationService reservationService;

    private Reservation mockTemplateReservation;

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
    }

    @Test
    void test_save_reservation_service(){
        // Arrange
        Reservation newReservation = new Reservation();
        newReservation.setCreatedAt(mockTemplateReservation.getCreatedAt());
        newReservation.setValidatedAt(mockTemplateReservation.getValidatedAt());
        newReservation.setStartDate(mockTemplateReservation.getStartDate());
        newReservation.setEndDate(mockTemplateReservation.getEndDate());
        newReservation.setHourlyRateLog(mockTemplateReservation.getHourlyRateLog());
        newReservation.setState(mockTemplateReservation.getState());
        newReservation.setPayed(mockTemplateReservation.isPayed());
        newReservation.setDatePayed(mockTemplateReservation.getDatePayed());
        newReservation.setUser(mockTemplateReservation.getUser());
        newReservation.setStation(mockTemplateReservation.getStation());

        Reservation mockReservation = this.mockTemplateReservation;
        when(reservationRepository.save(newReservation)).thenReturn(mockReservation);
        
        // Act
        Reservation savedReservation = reservationService.saveReservation(newReservation);

        // Assert
        assertAll(
            () -> assertNotNull(savedReservation, "Saved reservation should not be null"),
            () -> assertEquals(mockReservation.getId(), savedReservation.getId(), "Saved reservation ID should match"),
            () -> assertEquals(mockReservation.getCreatedAt(), savedReservation.getCreatedAt(), "Saved reservation createdAt should match"),
            () -> assertEquals(mockReservation.getValidatedAt(), savedReservation.getValidatedAt(), "Saved reservation validatedAt should match"),
            () -> assertEquals(mockReservation.getStartDate(), savedReservation.getStartDate(), "Saved reservation startDate should match"),
            () -> assertEquals(mockReservation.getEndDate(), savedReservation.getEndDate(), "Saved reservation endDate should match"),
            () -> assertEquals(mockReservation.getHourlyRateLog(), savedReservation.getHourlyRateLog(), "Saved reservation hourlyRateLog should match"),
            () -> assertEquals(mockReservation.getState(), savedReservation.getState(), "Saved reservation state should match"),
            () -> assertEquals(mockReservation.isPayed(), savedReservation.isPayed(), "Saved reservation payed status should match"),
            () -> assertEquals(mockReservation.getDatePayed(), savedReservation.getDatePayed(), "Saved reservation datePayed should match"),
            () -> assertEquals(mockReservation.getUser(), savedReservation.getUser(), "Saved reservation user should match"),
            () -> assertEquals(mockReservation.getStation(), savedReservation.getStation(), "Saved reservation station should match")
        );
    }
    
    @Test
    public void test_get_all_reservations_service() throws IllegalAccessException {
        // Arrange
        Reservation reservation1 = new Reservation();
        reservation1.setCreatedAt(mockTemplateReservation.getCreatedAt());
        reservation1.setValidatedAt(mockTemplateReservation.getValidatedAt());
        reservation1.setStartDate(mockTemplateReservation.getStartDate());
        reservation1.setEndDate(mockTemplateReservation.getEndDate());
        reservation1.setHourlyRateLog(mockTemplateReservation.getHourlyRateLog());
        reservation1.setState(mockTemplateReservation.getState());
        reservation1.setPayed(mockTemplateReservation.isPayed());
        reservation1.setDatePayed(mockTemplateReservation.getDatePayed());
        reservation1.setUser(mockTemplateReservation.getUser());
        reservation1.setStation(mockTemplateReservation.getStation());

        Reservation reservation2 = new Reservation();
        reservation2.setCreatedAt(mockTemplateReservation.getCreatedAt());
        reservation2.setValidatedAt(mockTemplateReservation.getValidatedAt());
        reservation2.setStartDate(mockTemplateReservation.getStartDate());
        reservation2.setEndDate(mockTemplateReservation.getEndDate());
        reservation2.setHourlyRateLog(mockTemplateReservation.getHourlyRateLog());
        reservation2.setState(mockTemplateReservation.getState());
        reservation2.setPayed(mockTemplateReservation.isPayed());
        reservation2.setDatePayed(mockTemplateReservation.getDatePayed());
        reservation2.setUser(mockTemplateReservation.getUser());
        reservation2.setStation(mockTemplateReservation.getStation());

        ArrayList<Reservation> mockReservationList = new ArrayList<>();
        mockReservationList.add(reservation1);
        mockReservationList.add(reservation2);

        when(reservationRepository.findAll()).thenReturn(mockReservationList);

        // Act
        List<Reservation> results = reservationService.getAllReservations();

        // Assert
        assertNotNull(results, "Reservations list should not be null");
        assertEquals(2, results.size(), "Reservations list size should match");

        // Check Lists content without any order
        assertThat(results).containsExactlyInAnyOrderElementsOf(mockReservationList);
    }

    @Test
    public void test_get_reservation_by_id_service() {
        // Arrange
        Long reservationId = 1L;
        Reservation mockReservation = this.mockTemplateReservation;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));

        // Act
        Optional<Reservation> resultReservation = reservationService.getReservationById(reservationId);

        // Assert
        assertTrue(resultReservation.isPresent(), "Reservation should be found");
        assertEquals(mockReservation, resultReservation.get(), "Result Reservation should match the mock");
    }

    @Test
    public void test_get_reservation_by_id_service_with_invalid_id() {
        // Arrange
        Long reservationId = 1L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act
        Optional<Reservation> resultReservation = reservationService.getReservationById(reservationId);

        // Assert
        assertTrue(resultReservation.isEmpty(), "Reservation should not be found");
    }

    @Test
    public void test_delete_reservation_by_id_service() {
        // Arrange
        Long reservationId = 1L;
        Reservation mockReservation = this.mockTemplateReservation;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));

        // Act
        Optional<Reservation> resultReservation = reservationService.deleteReservationById(reservationId);

        // Assert
        assertTrue(resultReservation.isPresent(), "Deleted Reservation should be returned");
        assertEquals(mockReservation, resultReservation.get(), "Deleted Reservation should match the mock");
    }

    @Test
    public void test_delete_reservation_by_id_service_with_invalid_id() {
        // Arrange
        Long reservationId = 1L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act
        Optional<Reservation> resultReservation = reservationService.deleteReservationById(reservationId);

        // Assert
        assertTrue(resultReservation.isEmpty(), "Deleted Reservation should not be found");
    }

    @Test
    public void test_update_reservation_service() {
        // Arrange
        Long reservationId = 1L;
        Reservation existingReservation = new Reservation();
        existingReservation.setId(reservationId);
        existingReservation.setCreatedAt(mockTemplateReservation.getCreatedAt());
        existingReservation.setValidatedAt(mockTemplateReservation.getValidatedAt());
        existingReservation.setStartDate(mockTemplateReservation.getStartDate());
        existingReservation.setEndDate(mockTemplateReservation.getEndDate());
        existingReservation.setHourlyRateLog(mockTemplateReservation.getHourlyRateLog());
        existingReservation.setState(mockTemplateReservation.getState());
        existingReservation.setPayed(mockTemplateReservation.isPayed());
        existingReservation.setDatePayed(mockTemplateReservation.getDatePayed());
        existingReservation.setUser(mockTemplateReservation.getUser()); 
        existingReservation.setStation(mockTemplateReservation.getStation());


        Reservation mockReservation = new Reservation();
        mockReservation.setId(reservationId);
        mockReservation.setCreatedAt(existingReservation.getCreatedAt());
        mockReservation.setValidatedAt(existingReservation.getValidatedAt());
        mockReservation.setStartDate(existingReservation.getStartDate());
        mockReservation.setEndDate(existingReservation.getEndDate());
        mockReservation.setHourlyRateLog(BigDecimal.valueOf(15.0));
        mockReservation.setState(ReservationState.PAST);
        mockReservation.setPayed(true);
        mockReservation.setDatePayed(LocalDateTime.now().plusDays(1));
        mockReservation.setUser(existingReservation.getUser());
        mockReservation.setStation(existingReservation.getStation());

        // reservationRepository calls the save method to update the Reservation
        // save is the method to mock
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);

        // Act
        Optional<Reservation> result = reservationService.updateReservation(reservationId, mockReservation);

        // Assert
        assertTrue(result.isPresent(), "Updated Reservation should be returned");

        Reservation newReservation = result.get();

        assertEquals(mockReservation.getId(), newReservation.getId(), "ID should remain the same after update");
        assertEquals(mockReservation.getCreatedAt(), newReservation.getCreatedAt(), "Updated created at should match");
        assertEquals(mockReservation.getValidatedAt(), newReservation.getValidatedAt(), "Updated validated at should match");
        assertEquals(mockReservation.getStartDate(), newReservation.getStartDate(), "Updated start date should match");
        assertEquals(mockReservation.getEndDate(), newReservation.getEndDate(), "Updated end date should match");
        assertEquals(mockReservation.getHourlyRateLog(), newReservation.getHourlyRateLog(), "Updated hourly rate should match");
        assertEquals(mockReservation.getState(), newReservation.getState(), "Updated state should match");
        assertEquals(mockReservation.isPayed(), newReservation.isPayed(), "Updated payed status should match");
        assertEquals(mockReservation.getDatePayed(), newReservation.getDatePayed(), "Updated date payed should match");
        assertEquals(mockReservation.getUser(), newReservation.getUser(), "Updated user should match");
        assertEquals(mockReservation.getStation(), newReservation.getStation(), "Updated station should match");
    }

    @Test
    public void test_update_reservation_service_with_invalid_id() {
        // Arrange
        Long reservationId = 1L;
        Reservation mockReservation = new Reservation();

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act
        Optional<Reservation> result = reservationService.updateReservation(reservationId, mockReservation);

        // Assert
        assertTrue(result.isEmpty(), "Updated Reservation should not be returned");
    }
}