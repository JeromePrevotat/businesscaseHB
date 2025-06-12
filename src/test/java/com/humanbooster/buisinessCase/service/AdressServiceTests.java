package com.humanbooster.buisinessCase.service;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Test
    void test_save_adress_service(){
        // Arrange
        Adress newAdress = new Adress();
        newAdress.setAdressname("Test Adress");
        newAdress.setStreetnumber("123");
        newAdress.setStreetname("Test Street");
        newAdress.setZipcode("12345");
        newAdress.setCity("Lyon");
        newAdress.setCountry("France");
        newAdress.setRegion("Auvergne-Rhône-Alpes");
        newAdress.setAddendum("Bâtiment B");
        newAdress.setFloor(2);
        newAdress.setUserList(new ArrayList<>());

        Adress mockAdress = new Adress();
        mockAdress.setId(1L);
        mockAdress.setAdressname("Test Adress");
        mockAdress.setStreetnumber("123");
        mockAdress.setStreetname("Test Street");
        mockAdress.setZipcode("12345");
        mockAdress.setCity("Lyon");
        mockAdress.setCountry("France");
        mockAdress.setRegion("Auvergne-Rhône-Alpes");
        mockAdress.setAddendum("Bâtiment B");
        mockAdress.setFloor(2);
        mockAdress.setUserList(new ArrayList<>());
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
}