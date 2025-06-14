package com.humanbooster.buisinessCase.service;

import java.util.ArrayList;
import java.util.HashSet;
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
import com.humanbooster.buisinessCase.repository.PlugTypeRepository;

@ExtendWith(MockitoExtension.class)
public class PlugTypeServiceTests {
    @Mock
    private PlugTypeRepository plugTypeRepository;
    @InjectMocks
    private PlugTypeService plugTypeService;

    private PlugType newMockPlugType;

    @BeforeEach
    public void setUp() {
        this.newMockPlugType = new PlugType();
        this.newMockPlugType.setId(1L);
        this.newMockPlugType.setPlugname("Test Plug Type");
        this.newMockPlugType.setVehiculeList(new HashSet<>());
        this.newMockPlugType.setStationList(new HashSet<>());
    }

    @Test
    void test_save_plug_type_service(){
        // Arrange
        PlugType newPlugType = new PlugType();
        newPlugType.setId(newMockPlugType.getId());
        newPlugType.setPlugname(newMockPlugType.getPlugname());
        newPlugType.setVehiculeList(newMockPlugType.getVehiculeList());
        newPlugType.setStationList(newMockPlugType.getStationList());

        PlugType mockPlugType = this.newMockPlugType;
        when(plugTypeRepository.save(newPlugType)).thenReturn(mockPlugType);

        // Act
        PlugType savedPlugType = plugTypeService.savePlugType(newPlugType);

        // Assert
        assertAll(
            () -> assertNotNull(savedPlugType, "Saved plug type should not be null"),
            () -> assertEquals(mockPlugType.getId(), savedPlugType.getId(), "Saved plug type ID should match"),
            () -> assertEquals(mockPlugType.getPlugname(), savedPlugType.getPlugname(), "Saved plug type name should match"),
            () -> assertEquals(mockPlugType.getVehiculeList(), savedPlugType.getVehiculeList(), "Saved plug type vehicule list should match"),
            () -> assertEquals(mockPlugType.getStationList(), savedPlugType.getStationList(), "Saved plug type station list should match")
        );
    }
    
    @Test
    public void test_get_all_plug_types_service() throws IllegalAccessException {
        // Arrange
        PlugType plugType1 = new PlugType();
        plugType1.setId(1L);
        plugType1.setPlugname("Test Plug Type 1");
        plugType1.setVehiculeList(new HashSet<>());
        plugType1.setStationList(new HashSet<>());

        PlugType plugType2 = new PlugType();
        plugType2.setId(2L);
        plugType2.setPlugname("Test Plug Type 2");
        plugType2.setVehiculeList(new HashSet<>());
        plugType2.setStationList(new HashSet<>());

        ArrayList<PlugType> mockPlugTypeList = new ArrayList<>();
        mockPlugTypeList.add(plugType1);
        mockPlugTypeList.add(plugType2);

        when(plugTypeRepository.findAll()).thenReturn(mockPlugTypeList);

        // Act
        List<PlugType> results = plugTypeService.getAllPlugTypes();

        // Assert
        assertNotNull(results, "Plug types list should not be null");
        assertEquals(2, results.size(), "Plug types list size should match");

        // Check Lists content without any order
        assertThat(results).containsExactlyInAnyOrderElementsOf(mockPlugTypeList);
    }

    @Test
    public void test_get_plug_type_by_id_service() {
        // Arrange
        Long plugTypeId = 1L;
        PlugType mockPlugType = this.newMockPlugType;
        when(plugTypeRepository.findById(plugTypeId)).thenReturn(Optional.of(mockPlugType));

        // Act
        Optional<PlugType> resultPlugType = plugTypeService.getPlugTypeById(plugTypeId);

        // Assert
        assertTrue(resultPlugType.isPresent(), "PlugType should be found");
        assertEquals(mockPlugType, resultPlugType.get(), "Result PlugType should match the mock");
    }

    @Test
    public void test_get_plug_type_by_id_service_with_invalid_id() {
        // Arrange
        Long plugTypeId = 1L;
        when(plugTypeRepository.findById(plugTypeId)).thenReturn(Optional.empty());

        // Act
        Optional<PlugType> resultPlugType = plugTypeService.getPlugTypeById(plugTypeId);

        // Assert
        assertTrue(resultPlugType.isEmpty(), "PlugType should not be found");
    }

    @Test
    public void test_delete_plug_type_by_id_service() {
        // Arrange
        Long plugTypeId = 1L;
        PlugType mockPlugType = this.newMockPlugType;
        when(plugTypeRepository.findById(plugTypeId)).thenReturn(Optional.of(mockPlugType));

        // Act
        Optional<PlugType> resultPlugType = plugTypeService.deletePlugTypeById(plugTypeId);

        // Assert
        assertTrue(resultPlugType.isPresent(), "Deleted PlugType should be returned");
        assertEquals(mockPlugType, resultPlugType.get(), "Deleted PlugType should match the mock");
    }

    @Test
    public void test_delete_plug_type_by_id_service_with_invalid_id() {
        // Arrange
        Long plugTypeId = 1L;
        when(plugTypeRepository.findById(plugTypeId)).thenReturn(Optional.empty());

        // Act
        Optional<PlugType> resultPlugType = plugTypeService.deletePlugTypeById(plugTypeId);

        // Assert
        assertTrue(resultPlugType.isEmpty(), "Deleted PlugType should not be found");
    }

    @Test
    public void test_update_plug_type_service() {
        // Arrange
        Long plugTypeId = 1L;
        PlugType existingPlugType = new PlugType();
        existingPlugType.setId(plugTypeId);
        existingPlugType.setPlugname("Old Plug Type");
        existingPlugType.setVehiculeList(new HashSet<>());
        existingPlugType.setStationList(new HashSet<>());

        PlugType mockPlugType = new PlugType();
        mockPlugType.setId(plugTypeId);
        mockPlugType.setPlugname("Updated Plug Type");
        mockPlugType.setVehiculeList(new HashSet<>());
        mockPlugType.setStationList(new HashSet<>());

        when(plugTypeRepository.findById(plugTypeId)).thenReturn(Optional.of(existingPlugType));
        when(plugTypeRepository.save(any(PlugType.class))).thenReturn(mockPlugType);

        // Act
        Optional<PlugType> result = plugTypeService.updatePlugType(plugTypeId, mockPlugType);

        // Assert
        assertTrue(result.isPresent(), "Updated PlugType should be returned");

        PlugType newPlugType = result.get();

        assertEquals(mockPlugType.getId(), newPlugType.getId(), "ID should remain the same after update");
        assertEquals(mockPlugType.getPlugname(), newPlugType.getPlugname(), "Updated plug name should match");
    }

    @Test
    public void test_update_plug_type_service_with_invalid_id() {
        // Arrange
        Long plugTypeId = 1L;
        PlugType mockPlugType = new PlugType();

        when(plugTypeRepository.findById(plugTypeId)).thenReturn(Optional.empty());

        // Act
        Optional<PlugType> result = plugTypeService.updatePlugType(plugTypeId, mockPlugType);

        // Assert
        assertTrue(result.isEmpty(), "Updated PlugType should not be returned");
    }
}