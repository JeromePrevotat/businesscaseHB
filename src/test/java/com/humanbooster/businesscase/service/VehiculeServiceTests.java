package com.humanbooster.businesscase.service;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.humanbooster.businesscase.model.PlugType;
import com.humanbooster.businesscase.model.User;
import com.humanbooster.businesscase.model.Vehicule;
import com.humanbooster.businesscase.repository.VehiculeRepository;

@ExtendWith(MockitoExtension.class)
public class VehiculeServiceTests {
    @Mock
    private VehiculeRepository vehiculeRepository;
    @InjectMocks
    private VehiculeService vehiculeService;

    private Vehicule mockTemplateVehicule;

    @BeforeEach
    public void setUp() {
        this.mockTemplateVehicule = new Vehicule();
        this.mockTemplateVehicule.setId(1L);
        this.mockTemplateVehicule.setPlate("WX-098-YZ");
        this.mockTemplateVehicule.setBrand("Tesla");
        this.mockTemplateVehicule.setBatteryCapacity(75);
        User user1 = new User();
        user1.setId(1L);
        this.mockTemplateVehicule.setUser(new HashSet<>(Arrays.asList(user1)));
        PlugType plugType1 = new PlugType();
        plugType1.setId(1L);
        PlugType plugType2 = new PlugType();
        plugType2.setId(2L);
        this.mockTemplateVehicule.setPlugType(new HashSet<>(Arrays.asList(plugType1, plugType2)));

    }

    @Test
    void test_save_vehicule_service(){
        // Arrange
        Vehicule newVehicule = new Vehicule();
        newVehicule.setId(mockTemplateVehicule.getId());
        newVehicule.setPlate(mockTemplateVehicule.getPlate());
        newVehicule.setBrand(mockTemplateVehicule.getBrand());
        newVehicule.setBatteryCapacity(mockTemplateVehicule.getBatteryCapacity());
        newVehicule.setUser(mockTemplateVehicule.getUser());
        newVehicule.setPlugType(mockTemplateVehicule.getPlugType());

        Vehicule mockVehicule = this.mockTemplateVehicule;
        when(vehiculeRepository.save(newVehicule)).thenReturn(mockVehicule);

        // Act
        Vehicule savedVehicule = vehiculeService.saveVehicule(newVehicule);

        // Assert
        assertAll(
            () -> assertNotNull(savedVehicule, "Saved vehicule should not be null"),
            () -> assertEquals(mockVehicule.getId(), savedVehicule.getId(), "Saved vehicule ID should match"),
            () -> assertEquals(mockVehicule.getPlate(), savedVehicule.getPlate(), "Saved vehicule plate should match"),
            () -> assertEquals(mockVehicule.getBrand(), savedVehicule.getBrand(), "Saved vehicule brand should match"),
            () -> assertEquals(mockVehicule.getBatteryCapacity(), savedVehicule.getBatteryCapacity(), "Saved vehicule battery capacity should match"),
            () -> assertEquals(mockVehicule.getUser(), savedVehicule.getUser(), "Saved vehicule user should match"),
            () -> assertEquals(mockVehicule.getPlugType(), savedVehicule.getPlugType(), "Saved vehicule plug type should match")
        );
    }
    
    @Test
    public void test_get_all_vehicules_service() throws IllegalAccessException {
        // Arrange
        Vehicule vehicule1 = new Vehicule();
        vehicule1.setId(1L);
        vehicule1.setPlate("WX-098-YZ");
        vehicule1.setBrand("Tesla");
        vehicule1.setBatteryCapacity(75);
        User user1 = new User();
        user1.setId(1L);
        vehicule1.setUser(new HashSet<>(Arrays.asList(user1)));
        PlugType plugType1 = new PlugType();
        plugType1.setId(1L);
        PlugType plugType2 = new PlugType();
        plugType2.setId(2L);
        vehicule1.setPlugType(new HashSet<>(Arrays.asList(plugType1, plugType2)));

        Vehicule vehicule2 = new Vehicule();
        vehicule2.setId(2L);
        vehicule2.setPlate("AB-123-CD");
        vehicule2.setBrand("Nissan");
        vehicule2.setBatteryCapacity(60);
        User user2 = new User();
        user2.setId(2L);
        vehicule2.setUser(new HashSet<>(Arrays.asList(user2)));
        PlugType plugType3 = new PlugType();
        plugType3.setId(3L);
        PlugType plugType4 = new PlugType();
        plugType4.setId(4L);
        vehicule2.setPlugType(new HashSet<>(Arrays.asList(plugType3, plugType4)));

        ArrayList<Vehicule> mockVehicules = new ArrayList<>();
        mockVehicules.add(vehicule1);
        mockVehicules.add(vehicule2);

        when(vehiculeRepository.findAll()).thenReturn(mockVehicules);

        // Act
        List<Vehicule> results = vehiculeService.getAllVehicules();

        // Assert
        assertNotNull(results, "Vehicules list should not be null");
        assertEquals(2, results.size(), "Vehicules list size should match");

        // Check Lists content without any order
        assertThat(results).containsExactlyInAnyOrderElementsOf(mockVehicules);
    }

    @Test
    public void test_get_vehicule_by_id_service() {
        // Arrange
        Long vehiculeId = 1L;
        Vehicule mockVehicule = this.mockTemplateVehicule;
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(mockVehicule));

        // Act
        Optional<Vehicule> resultVehicule = vehiculeService.getVehiculeById(vehiculeId);

        // Assert
        assertTrue(resultVehicule.isPresent(), "Vehicule should be found");
        assertEquals(mockVehicule, resultVehicule.get(), "Result Vehicule should match the mock");
    }

    @Test
    public void test_get_vehicule_by_id_service_with_invalid_id() {
        // Arrange
        Long vehiculeId = 1L;
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.empty());

        // Act
        Optional<Vehicule> resultVehicule = vehiculeService.getVehiculeById(vehiculeId);

        // Assert
        assertTrue(resultVehicule.isEmpty(), "Vehicule should not be found");
    }

    @Test
    public void test_delete_vehicule_by_id_service() {
        // Arrange
        Long vehiculeId = 1L;
        Vehicule mockVehicule = this.mockTemplateVehicule;
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(mockVehicule));

        // Act
        Optional<Vehicule> resultVehicule = vehiculeService.deleteVehiculeById(vehiculeId);

        // Assert
        assertTrue(resultVehicule.isPresent(), "Deleted Vehicule should be returned");
        assertEquals(mockVehicule, resultVehicule.get(), "Deleted Vehicule should match the mock");
    }

    @Test
    public void test_delete_vehicule_by_id_service_with_invalid_id() {
        // Arrange
        Long vehiculeId = 1L;
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.empty());

        // Act
        Optional<Vehicule> resultVehicule = vehiculeService.deleteVehiculeById(vehiculeId);

        // Assert
        assertTrue(resultVehicule.isEmpty(), "Deleted Vehicule should not be found");
    }

    @Test
    public void test_update_vehicule_service() {
        // Arrange
        Long vehiculeId = 1L;
        Vehicule existingVehicule = new Vehicule();
        existingVehicule.setId(vehiculeId);
        existingVehicule.setPlate("WX-098-YZ");
        existingVehicule.setBrand("Tesla");
        existingVehicule.setBatteryCapacity(75);
        User user1 = new User();
        user1.setId(1L);
        existingVehicule.setUser(new HashSet<>(Arrays.asList(user1)));
        PlugType plugType1 = new PlugType();
        plugType1.setId(1L);
        PlugType plugType2 = new PlugType();
        plugType2.setId(2L);
        existingVehicule.setPlugType(new HashSet<>(Arrays.asList(plugType1, plugType2)));

        Vehicule mockVehicule = new Vehicule();
        mockVehicule.setId(vehiculeId);
        mockVehicule.setPlate("AB-123-CD");
        mockVehicule.setBrand("Nissan");
        mockVehicule.setBatteryCapacity(80);
        User user2 = new User();
        user2.setId(2L);
        mockVehicule.setUser(new HashSet<>(Arrays.asList(user2)));
        PlugType plugType3 = new PlugType();
        plugType3.setId(3L);
        PlugType plugType4 = new PlugType();
        plugType4.setId(4L);
        mockVehicule.setPlugType(new HashSet<>(Arrays.asList(plugType3, plugType4)));

        // vehiculeRepository calls the save method to update the Vehicule
        // save is the method to mock
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(existingVehicule));
        when(vehiculeRepository.save(any(Vehicule.class))).thenReturn(mockVehicule);

        // Act
        Optional<Vehicule> result = vehiculeService.updateVehicule(vehiculeId, mockVehicule);

        // Assert
        assertTrue(result.isPresent(), "Updated Vehicule should be returned");

        Vehicule newVehicule = result.get();

        assertEquals(mockVehicule.getId(), newVehicule.getId(), "ID should remain the same after update");
        assertEquals(mockVehicule.getPlate(), newVehicule.getPlate(), "Updated plate should match");
        assertEquals(mockVehicule.getBrand(), newVehicule.getBrand(), "Updated brand should match");
        assertEquals(mockVehicule.getBatteryCapacity(), newVehicule.getBatteryCapacity(), "Updated battery capacity should match");
        assertEquals(mockVehicule.getUser(), newVehicule.getUser(), "Updated user should match");
        assertEquals(mockVehicule.getPlugType(), newVehicule.getPlugType(), "Updated plug type should match");
    }

    @Test
    public void test_update_vehicule_service_with_invalid_id() {
        // Arrange
        Long vehiculeId = 1L;
        Vehicule mockVehicule = new Vehicule();

        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.empty());

        // Act
        Optional<Vehicule> result = vehiculeService.updateVehicule(vehiculeId, mockVehicule);

        // Assert
        assertTrue(result.isEmpty(), "Updated Vehicule should not be returned");
    }
}