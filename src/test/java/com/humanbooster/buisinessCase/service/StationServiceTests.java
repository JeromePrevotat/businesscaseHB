package com.humanbooster.buisinessCase.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.humanbooster.buisinessCase.model.PlugType;
import com.humanbooster.buisinessCase.model.Spot;
import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.model.StationState;
import com.humanbooster.buisinessCase.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class StationServiceTests {
    @Mock
    private StationRepository stationRepository;
    @InjectMocks
    private StationService stationService;

    private Station mockTemplateStation;

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
    }

    @Test
    void test_save_station_service(){
        // Arrange
        Station newStation = new Station();
        newStation.setId(mockTemplateStation.getId());
        newStation.setStationName(mockTemplateStation.getStationName());
        newStation.setLatitude(mockTemplateStation.getLatitude());
        newStation.setLongitude(mockTemplateStation.getLongitude());
        newStation.setPriceRate(mockTemplateStation.getPriceRate());
        newStation.setPowerOutput(mockTemplateStation.getPowerOutput());
        newStation.setManual(mockTemplateStation.getManual());
        newStation.setState(mockTemplateStation.getState());
        newStation.setGrounded(mockTemplateStation.isGrounded());
        newStation.setBusy(mockTemplateStation.isBusy());
        newStation.setWired(mockTemplateStation.isWired());
        newStation.setSpot(mockTemplateStation.getSpot());
        newStation.setReservationList(mockTemplateStation.getReservationList());
        newStation.setMediaList(mockTemplateStation.getMediaList());
        newStation.setPlugType(mockTemplateStation.getPlugType());

        Station mockStation = this.mockTemplateStation;
        when(stationRepository.save(newStation)).thenReturn(mockStation);

        // Act
        Station savedStation = stationService.saveStation(newStation);

        // Assert
        assertAll(
            () -> assertNotNull(savedStation, "Saved station should not be null"),
            () -> assertEquals(mockStation.getId(), savedStation.getId(), "Saved station ID should match"),
            () -> assertEquals(mockStation.getStationName(), savedStation.getStationName(), "Saved station name should match"),
            () -> assertEquals(mockStation.getLatitude(), savedStation.getLatitude(), "Saved station latitude should match"),
            () -> assertEquals(mockStation.getLongitude(), savedStation.getLongitude(), "Saved station longitude should match"),
            () -> assertEquals(mockStation.getPriceRate(), savedStation.getPriceRate(), "Saved station price rate should match"),
            () -> assertEquals(mockStation.getPowerOutput(), savedStation.getPowerOutput(), "Saved station power output should match"),
            () -> assertEquals(mockStation.getManual(), savedStation.getManual(), "Saved station manual should match"),
            () -> assertEquals(mockStation.getState(), savedStation.getState(), "Saved station state should match"),
            () -> assertEquals(mockStation.isGrounded(), savedStation.isGrounded(), "Saved station grounded state should match"),
            () -> assertEquals(mockStation.isBusy(), savedStation.isBusy(), "Saved station busy state should match"),
            () -> assertEquals(mockStation.isWired(), savedStation.isWired(), "Saved station wired state should match"),
            () -> assertEquals(mockStation.getSpot(), savedStation.getSpot(), "Saved station spot should match"),
            () -> assertEquals(mockStation.getReservationList(), savedStation.getReservationList(), "Saved station reservation list should match"),
            () -> assertEquals(mockStation.getMediaList(), savedStation.getMediaList(), "Saved station media list should match"),
            () -> assertEquals(mockStation.getPlugType(), savedStation.getPlugType(), "Saved station plug type should match")
        );
    }
    
    @Test
    public void test_get_all_stations_service() throws IllegalAccessException {
        // Arrange
        Station station1 = new Station();
        station1.setStationName("Test Station 1");
        station1.setLatitude(new BigDecimal("48.8566"));
        station1.setLongitude(new BigDecimal("2.3522"));
        station1.setPriceRate(new BigDecimal("0.25"));
        station1.setPowerOutput(new BigDecimal("22.0"));
        station1.setManual("Test Manual 1");
        station1.setState(StationState.ACTIVE);
        station1.setGrounded(true);
        station1.setBusy(false);
        station1.setWired(false);
        Spot spot1 = new Spot();
        spot1.setId(1L);
        station1.setSpot(spot1);
        station1.setReservationList(new ArrayList<>());
        station1.setMediaList(new ArrayList<>());
        PlugType plugType1 = new PlugType();
        plugType1.setId(1L);
        station1.setPlugType(Arrays.asList(plugType1));

        Station station2 = new Station();
        station2.setStationName("Test Station 2");
        station2.setLatitude(new BigDecimal("48.8566"));
        station2.setLongitude(new BigDecimal("2.3522"));
        station2.setPriceRate(new BigDecimal("0.25"));
        station2.setPowerOutput(new BigDecimal("22.0"));
        station2.setManual("Test Manual 2");
        station2.setState(StationState.ACTIVE);
        station2.setGrounded(true);
        station2.setBusy(false);
        station2.setWired(false);
        Spot spot2 = new Spot();
        spot2.setId(2L);
        station2.setSpot(spot2);
        station2.setReservationList(new ArrayList<>());
        station2.setMediaList(new ArrayList<>());
        PlugType plugType2 = new PlugType();
        plugType2.setId(2L);
        station2.setPlugType(Arrays.asList(plugType2));

        ArrayList<Station> mockStations = new ArrayList<>();
        mockStations.add(station1);
        mockStations.add(station2);

        when(stationRepository.findAll()).thenReturn(mockStations);

        // Act
        List<Station> results = stationService.getAllStations();

        // Assert
        assertNotNull(results, "Stations list should not be null");
        assertEquals(2, results.size(), "Stations list size should match");

        // Check Lists content without any order
        assertThat(results).containsExactlyInAnyOrderElementsOf(mockStations);
    }

    @Test
    public void test_get_station_by_id_service() {
        // Arrange
        Long stationId = 1L;
        Station mockStation = this.mockTemplateStation;
        when(stationRepository.findById(stationId)).thenReturn(Optional.of(mockStation));

        // Act
        Optional<Station> resultStation = stationService.getStationById(stationId);

        // Assert
        assertTrue(resultStation.isPresent(), "Station should be found");
        assertEquals(mockStation, resultStation.get(), "Result Station should match the mock");
    }

    @Test
    public void test_get_station_by_id_service_with_invalid_id() {
        // Arrange
        Long stationId = 1L;
        when(stationRepository.findById(stationId)).thenReturn(Optional.empty());

        // Act
        Optional<Station> resultStation = stationService.getStationById(stationId);

        // Assert
        assertTrue(resultStation.isEmpty(), "Station should not be found");
    }

    @Test
    public void test_delete_station_by_id_service() {
        // Arrange
        Long stationId = 1L;
        Station mockStation = this.mockTemplateStation;
        when(stationRepository.findById(stationId)).thenReturn(Optional.of(mockStation));

        // Act
        Optional<Station> resultStation = stationService.deleteStationById(stationId);

        // Assert
        assertTrue(resultStation.isPresent(), "Deleted Station should be returned");
        assertEquals(mockStation, resultStation.get(), "Deleted Station should match the mock");
    }

    @Test
    public void test_delete_station_by_id_service_with_invalid_id() {
        // Arrange
        Long stationId = 1L;
        when(stationRepository.findById(stationId)).thenReturn(Optional.empty());

        // Act
        Optional<Station> resultStation = stationService.deleteStationById(stationId);

        // Assert
        assertTrue(resultStation.isEmpty(), "Deleted Station should not be found");
    }

    @Test
    public void test_update_station_service() {
        // Arrange
        Long stationId = 1L;
        Station existingStation = new Station();
        existingStation.setId(stationId);
        existingStation.setStationName("Old Station Name");
        existingStation.setLatitude(new BigDecimal("48.8566"));
        existingStation.setLongitude(new BigDecimal("2.3522"));
        existingStation.setPriceRate(new BigDecimal("0.25"));
        existingStation.setPowerOutput(new BigDecimal("22.0"));
        existingStation.setManual("Old Manual");
        existingStation.setState(StationState.ACTIVE);
        existingStation.setGrounded(true);
        existingStation.setBusy(false);
        existingStation.setWired(false);
        Spot existingSpot = new Spot();
        existingSpot.setId(1L);
        existingStation.setSpot(existingSpot);
        existingStation.setReservationList(new ArrayList<>());
        existingStation.setMediaList(new ArrayList<>());
        PlugType existingPlugType = new PlugType();
        existingPlugType.setId(1L);
        existingStation.setPlugType(Arrays.asList(existingPlugType));

        Station mockStation = new Station();
        mockStation.setId(stationId);
        mockStation.setStationName("Updated Station Name");
        mockStation.setLatitude(new BigDecimal("48.8566"));
        mockStation.setLongitude(new BigDecimal("2.3522"));
        mockStation.setPriceRate(new BigDecimal("0.25"));
        mockStation.setPowerOutput(new BigDecimal("22.0"));
        mockStation.setManual("Updated Manual");
        mockStation.setState(StationState.ACTIVE);
        mockStation.setGrounded(true);
        mockStation.setBusy(false);
        mockStation.setWired(false);
        Spot mockSpot = new Spot();
        mockSpot.setId(1L);
        mockStation.setSpot(mockSpot);
        mockStation.setReservationList(new ArrayList<>());
        mockStation.setMediaList(new ArrayList<>());
        PlugType mockPlugType = new PlugType();
        mockPlugType.setId(1L);
        mockStation.setPlugType(Arrays.asList(mockPlugType));

        // stationRepository calls the save method to update the Station
        when(stationRepository.findById(stationId)).thenReturn(Optional.of(existingStation));
        when(stationRepository.save(any(Station.class))).thenReturn(mockStation);
        
        // Act
        Optional<Station> result = stationService.updateStation(stationId, mockStation);

        // Assert
        assertTrue(result.isPresent(), "Updated Station should be returned");

        Station newStation = result.get();

        assertEquals(mockStation.getId(), newStation.getId(), "ID should remain the same after update");
        assertEquals(mockStation.getStationName(), newStation.getStationName(), "Updated station name should match");
        assertEquals(mockStation.getLatitude(), newStation.getLatitude(), "Updated latitude should match");
        assertEquals(mockStation.getLongitude(), newStation.getLongitude(), "Updated longitude should match");
        assertEquals(mockStation.getPriceRate(), newStation.getPriceRate(), "Updated price rate should match");
        assertEquals(mockStation.getPowerOutput(), newStation.getPowerOutput(), "Updated power output should match");
        assertEquals(mockStation.getManual(), newStation.getManual(), "Updated manual should match");
        assertEquals(mockStation.getState(), newStation.getState(), "Updated state should match");
        assertEquals(mockStation.isGrounded(), newStation.isGrounded(), "Updated grounded state should match");
        assertEquals(mockStation.isBusy(), newStation.isBusy(), "Updated busy state should match");
        assertEquals(mockStation.isWired(), newStation.isWired(), "Updated wired state should match");
        assertEquals(mockStation.getSpot(), newStation.getSpot(), "Updated spot should match");
        assertEquals(mockStation.getReservationList(), newStation.getReservationList(), "Updated reservation list should match");
        assertEquals(mockStation.getMediaList(), newStation.getMediaList(), "Updated media list should match");
        assertEquals(mockStation.getPlugType(), newStation.getPlugType(), "Updated plug type should match");
    }

    @Test
    public void test_update_station_service_with_invalid_id() {
        // Arrange
        Long stationId = 1L;
        Station mockStation = new Station();

        when(stationRepository.findById(stationId)).thenReturn(Optional.empty());

        // Act
        Optional<Station> result = stationService.updateStation(stationId, mockStation);

        // Assert
        assertTrue(result.isEmpty(), "Updated Station should not be returned");
    }
}