package com.humanbooster.buisinessCase.service;

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
import com.humanbooster.buisinessCase.repository.AdressRepository;

@ExtendWith(MockitoExtension.class)
public class AdressServiceTests {
    @Mock
    private AdressRepository adressRepository;
    @InjectMocks
    private AdressService adressService;

    private Adress newMockAdress;

    @BeforeEach
    public void setUp() {
        this.newMockAdress = new Adress();
        newMockAdress.setAdressname("Test Adress");
        newMockAdress.setStreetnumber("123");
        newMockAdress.setStreetname("Test Street");
        newMockAdress.setZipcode("12345");
        newMockAdress.setCity("Lyon");
        newMockAdress.setCountry("France");
        newMockAdress.setRegion("Auvergne-Rhône-Alpes");
        newMockAdress.setAddendum("Bâtiment B");
        newMockAdress.setFloor(2);
        newMockAdress.setUserList(new ArrayList<>());
    }

    @Test
    void test_save_adress_service(){
        // Arrange
        Adress newAdress = new Adress();
        newAdress.setAdressname(newMockAdress.getAdressname());
        newAdress.setStreetnumber(newMockAdress.getStreetnumber());
        newAdress.setStreetname(newMockAdress.getStreetname());
        newAdress.setZipcode(newMockAdress.getZipcode());
        newAdress.setCity(newMockAdress.getCity());
        newAdress.setCountry(newMockAdress.getCountry());
        newAdress.setRegion(newMockAdress.getRegion());
        newAdress.setAddendum(newMockAdress.getAddendum());
        newAdress.setFloor(newMockAdress.getFloor());
        newAdress.setUserList(newMockAdress.getUserList());

        Adress mockAdress = this.newMockAdress;
        when(adressRepository.save(newAdress)).thenReturn(mockAdress);
        
        // Act
        Adress savedAdress = adressService.saveAdress(newAdress);

        // Assert
        assertAll(
            () -> assertNotNull(savedAdress, "Saved adress should not be null"),
            () -> assertEquals(mockAdress.getId(), savedAdress.getId(), "Saved adress ID should match"),
            () -> assertEquals(mockAdress.getAdressname(), savedAdress.getAdressname(), "Saved adress name should match"),
            () -> assertEquals(mockAdress.getStreetnumber(), savedAdress.getStreetnumber(), "Saved street number should match"),
            () -> assertEquals(mockAdress.getStreetname(), savedAdress.getStreetname(), "Saved street name should match"),
            () -> assertEquals(mockAdress.getZipcode(), savedAdress.getZipcode(), "Saved zipcode should match"),
            () -> assertEquals(mockAdress.getCity(), savedAdress.getCity(), "Saved city should match"),
            () -> assertEquals(mockAdress.getCountry(), savedAdress.getCountry(), "Saved country should match"),
            () -> assertEquals(mockAdress.getRegion(), savedAdress.getRegion(), "Saved region should match"),
            () -> assertEquals(mockAdress.getAddendum(), savedAdress.getAddendum(), "Saved addendum should match"),
            () -> assertEquals(mockAdress.getFloor(), savedAdress.getFloor(), "Saved floor should match")
        );
    }
    
    @Test
    public void test_get_all_adresses_service() throws IllegalAccessException {
        // Arrange
        Adress adress1 = new Adress();
        adress1.setAdressname("Test Adress");
        adress1.setStreetnumber("123");
        adress1.setStreetname("Test Street");
        adress1.setZipcode("12345");
        adress1.setCity("Lyon");
        adress1.setCountry("France");
        adress1.setRegion("Auvergne-Rhône-Alpes");
        adress1.setAddendum("Bâtiment B");
        adress1.setFloor(1);
        adress1.setUserList(new ArrayList<>());

        Adress adress2 = new Adress();
        adress2.setAdressname("Test Adress");
        adress2.setStreetnumber("456");
        adress2.setStreetname("Test Avenue");
        adress2.setZipcode("67890");
        adress2.setCity("Marseille");
        adress2.setCountry("France");
        adress2.setRegion("Provence-Alpes-Côte d'Azur");
        adress2.setAddendum("Bâtiment A");
        adress2.setFloor(2);
        adress2.setUserList(new ArrayList<>());

        ArrayList<Adress> mockAdresses = new ArrayList<>();
        mockAdresses.add(adress1);
        mockAdresses.add(adress2);
        
        when(adressRepository.findAll()).thenReturn(mockAdresses);
        
        // Act
        List<Adress> results = adressService.getAllAdresses();
        
        // Assert
        assertNotNull(results, "Adresses list should not be null");
        assertEquals(2, results.size(), "Adresses list size should match");
        
        // Check Lists content without any order
        assertThat(results).containsExactlyInAnyOrderElementsOf(mockAdresses);
    }

    @Test
    public void test_get_adress_by_id_service() {
        // Arrange
        Long adressId = 1L;
        Adress mockAdress = this.newMockAdress;
        when(adressRepository.findById(adressId)).thenReturn(Optional.of(mockAdress));

        // Act
        Optional<Adress> resultAdress = adressService.getAdressById(adressId);

        // Assert
        assertTrue(resultAdress.isPresent(), "Adress should be found");
        assertEquals(mockAdress, resultAdress.get(), "Result Adress should match the mock");
    }

    @Test
    public void test_get_adress_by_id_service_with_invalid_id() {
        // Arrange
        Long adressId = 1L;
        when(adressRepository.findById(adressId)).thenReturn(Optional.empty());

        // Act
        Optional<Adress> resultAdress = adressService.getAdressById(adressId);

        // Assert
        assertTrue(resultAdress.isEmpty(), "Adress should not be found");
    }

    @Test
    public void test_delete_adress_by_id_service() {
        // Arrange
        Long adressId = 1L;
        Adress mockAdress = this.newMockAdress;
        when(adressRepository.findById(adressId)).thenReturn(Optional.of(mockAdress));
        
        // Act
        Optional<Adress> resultAdress = adressService.deleteAdressById(adressId);
        
        // Assert
        assertTrue(resultAdress.isPresent(), "Deleted Adress should be returned");
        assertEquals(mockAdress, resultAdress.get(), "Deleted Adress should match the mock");
    }

    @Test
    public void test_delete_adress_by_id_service_with_invalid_id() {
        // Arrange
        Long adressId = 1L;
        when(adressRepository.findById(adressId)).thenReturn(Optional.empty());

        // Act
        Optional<Adress> resultAdress = adressService.deleteAdressById(adressId);

        // Assert
        assertTrue(resultAdress.isEmpty(), "Deleted Adress should not be found");
    }

    @Test
    public void test_update_adress_service() {
        // Arrange
        Long adressId = 1L;
        Adress existingAdress = new Adress();
        existingAdress.setId(adressId);
        existingAdress.setAdressname("Old Adress");
        existingAdress.setStreetnumber("123");
        existingAdress.setStreetname("Old Street");
        existingAdress.setZipcode("12345");
        existingAdress.setCity("Lyon");
        existingAdress.setCountry("France");
        existingAdress.setRegion("Auvergne-Rhône-Alpes");
        existingAdress.setAddendum("Bâtiment B");
        existingAdress.setFloor(2);
        existingAdress.setUserList(new ArrayList<>());

        Adress mockAdress = new Adress();
        mockAdress.setId(adressId);
        mockAdress.setAdressname("Updated Adress");
        mockAdress.setStreetnumber("456");
        mockAdress.setStreetname("Updated Street");
        mockAdress.setZipcode("67890");
        mockAdress.setCity("Marseille");
        mockAdress.setCountry("France");
        mockAdress.setRegion("Provence-Alpes-Côte d'Azur");
        mockAdress.setAddendum("Bâtiment A");
        mockAdress.setFloor(3);
        
        // adressRepository calls the save method to update the Adress
        // save is the method to mock
        when(adressRepository.findById(adressId)).thenReturn(Optional.of(existingAdress));
        when(adressRepository.save(any(Adress.class))).thenReturn(mockAdress);
        
        // Act
        Optional<Adress> result = adressService.updateAdress(adressId, mockAdress);
        
        // Assert
        assertTrue(result.isPresent(), "Updated Adress should be returned");
        
        Adress newAdress = result.get();
        
        assertEquals(mockAdress.getId(), newAdress.getId(), "ID should remain the same after update");
        assertEquals(mockAdress.getAdressname(), newAdress.getAdressname(), "Updated adress name should match");
        assertEquals(mockAdress.getStreetnumber(), newAdress.getStreetnumber(), "Updated street number should match");
        assertEquals(mockAdress.getStreetname(), newAdress.getStreetname(), "Updated street name should match");
        assertEquals(mockAdress.getZipcode(), newAdress.getZipcode(), "Updated zipcode should match");
        assertEquals(mockAdress.getCity(), newAdress.getCity(), "Updated city should match");
        assertEquals(mockAdress.getCountry(), newAdress.getCountry(), "Updated country should match");
        assertEquals(mockAdress.getRegion(), newAdress.getRegion(), "Updated region should match");
        assertEquals(mockAdress.getAddendum(), newAdress.getAddendum(), "Updated addendum should match");
        assertEquals(mockAdress.getFloor(), newAdress.getFloor(), "Updated floor should match");
    }

    @Test
    public void test_update_adress_service_with_invalid_id() {
        // Arrange
        Long adressId = 1L;
        Adress mockAdress = new Adress();
        
        when(adressRepository.findById(adressId)).thenReturn(Optional.empty());
        
        // Act
        Optional<Adress> result = adressService.updateAdress(adressId, mockAdress);
        
        // Assert
        assertTrue(result.isEmpty(), "Updated Adress should not be returned");
    }
}