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

import com.humanbooster.businesscase.model.Adress;
import com.humanbooster.businesscase.model.Spot;
import com.humanbooster.businesscase.repository.SpotRepository;

@ExtendWith(MockitoExtension.class)
public class SpotServiceTests {
    @Mock
    private SpotRepository spotRepository;
    @InjectMocks
    private SpotService spotService;

    private Spot mockTemplateSpot;

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
    }

    @Test
    void test_save_spot_service(){
        // Arrange
        Spot newSpot = new Spot();
        newSpot.setInstruction(mockTemplateSpot.getInstruction());
        newSpot.setStationList(mockTemplateSpot.getStationList());
        newSpot.setAdress(mockTemplateSpot.getAdress());
        newSpot.setMediaList(mockTemplateSpot.getMediaList());

        Spot mockSpot = this.mockTemplateSpot;
        when(spotRepository.save(newSpot)).thenReturn(mockSpot);

        // Act
        Spot savedSpot = spotService.saveSpot(newSpot);

        // Assert
        assertAll(
            () -> assertNotNull(savedSpot, "Saved spot should not be null"),
            () -> assertEquals(mockSpot.getId(), savedSpot.getId(), "Saved spot ID should match"),
            () -> assertEquals(mockSpot.getInstruction(), savedSpot.getInstruction(), "Saved spot instruction should match"),
            () -> assertEquals(mockSpot.getStationList(), savedSpot.getStationList(), "Saved spot station list should match"),
            () -> assertEquals(mockSpot.getAdress(), savedSpot.getAdress(), "Saved spot address should match"),
            () -> assertEquals(mockSpot.getMediaList(), savedSpot.getMediaList(), "Saved spot media list should match")
        );
    }
    
    @Test
    public void test_get_all_spots_service() throws IllegalAccessException {
        // Arrange
        Spot spot1 = new Spot();
        spot1.setInstruction("Test Instruction 1");
        spot1.setStationList(new ArrayList<>());
        Adress adress = new Adress();
        adress.setId(1L);
        spot1.setAdress(adress);
        spot1.setMediaList(new ArrayList<>());

        Spot spot2 = new Spot();
        spot2.setInstruction("Test Instruction 2");
        spot2.setStationList(new ArrayList<>());
        Adress adress2 = new Adress();
        adress2.setId(2L);
        spot2.setAdress(adress2);
        spot2.setMediaList(new ArrayList<>());

        ArrayList<Spot> mockSpots = new ArrayList<>();
        mockSpots.add(spot1);
        mockSpots.add(spot2);

        when(spotRepository.findAll()).thenReturn(mockSpots);

        // Act
        List<Spot> results = spotService.getAllSpots();

        // Assert
        assertNotNull(results, "Spots list should not be null");
        assertEquals(2, results.size(), "Spots list size should match");

        // Check Lists content without any order
        assertThat(results).containsExactlyInAnyOrderElementsOf(mockSpots);
    }

    @Test
    public void test_get_spot_by_id_service() {
        // Arrange
        Long spotId = 1L;
        Spot mockSpot = this.mockTemplateSpot;
        when(spotRepository.findById(spotId)).thenReturn(Optional.of(mockSpot));

        // Act
        Optional<Spot> resultSpot = spotService.getSpotById(spotId);

        // Assert
        assertTrue(resultSpot.isPresent(), "Spot should be found");
        assertEquals(mockSpot, resultSpot.get(), "Result Spot should match the mock");
    }

    @Test
    public void test_get_spot_by_id_service_with_invalid_id() {
        // Arrange
        Long spotId = 1L;
        when(spotRepository.findById(spotId)).thenReturn(Optional.empty());

        // Act
        Optional<Spot> resultSpot = spotService.getSpotById(spotId);

        // Assert
        assertTrue(resultSpot.isEmpty(), "Spot should not be found");
    }

    @Test
    public void test_delete_spot_by_id_service() {
        // Arrange
        Long spotId = 1L;
        Spot mockSpot = this.mockTemplateSpot;
        when(spotRepository.findById(spotId)).thenReturn(Optional.of(mockSpot));

        // Act
        Optional<Spot> resultSpot = spotService.deleteSpotById(spotId);

        // Assert
        assertTrue(resultSpot.isPresent(), "Deleted Spot should be returned");
        assertEquals(mockSpot, resultSpot.get(), "Deleted Spot should match the mock");
    }

    @Test
    public void test_delete_spot_by_id_service_with_invalid_id() {
        // Arrange
        Long spotId = 1L;
        when(spotRepository.findById(spotId)).thenReturn(Optional.empty());

        // Act
        Optional<Spot> resultSpot = spotService.deleteSpotById(spotId);

        // Assert
        assertTrue(resultSpot.isEmpty(), "Deleted Spot should not be found");
    }

    @Test
    public void test_update_spot_service() {
        // Arrange
        Long spotId = 1L;
        Spot existingSpot = new Spot();
        existingSpot.setId(spotId);
        existingSpot.setInstruction("Old Instruction");
        existingSpot.setStationList(new ArrayList<>());
        Adress adress = new Adress();
        adress.setId(1L);
        existingSpot.setAdress(adress);
        existingSpot.setMediaList(new ArrayList<>());

        Spot mockSpot = new Spot();
        mockSpot.setId(spotId);
        mockSpot.setInstruction("Updated Instruction");
        mockSpot.setStationList(new ArrayList<>());
        Adress updatedAdress = new Adress();
        updatedAdress.setId(1L);
        mockSpot.setAdress(updatedAdress);
        mockSpot.setMediaList(new ArrayList<>());

        when(spotRepository.findById(spotId)).thenReturn(Optional.of(existingSpot));
        when(spotRepository.save(any(Spot.class))).thenReturn(mockSpot);

        // Act
        Optional<Spot> result = spotService.updateSpot(spotId, mockSpot);

        // Assert
        assertTrue(result.isPresent(), "Updated Spot should be returned");
        Spot newSpot = result.get();
        assertEquals(mockSpot.getId(), newSpot.getId(), "ID should remain the same after update");
        assertEquals(mockSpot.getInstruction(), newSpot.getInstruction(), "Updated instruction should match");
        assertEquals(mockSpot.getAdress(), newSpot.getAdress(), "Updated adress should match");
    }

    @Test
    public void test_update_spot_service_with_invalid_id() {
        // Arrange
        Long spotId = 1L;
        Spot mockSpot = new Spot();

        when(spotRepository.findById(spotId)).thenReturn(Optional.empty());

        // Act
        Optional<Spot> result = spotService.updateSpot(spotId, mockSpot);

        // Assert
        assertTrue(result.isEmpty(), "Updated Spot should not be returned");
    }
}