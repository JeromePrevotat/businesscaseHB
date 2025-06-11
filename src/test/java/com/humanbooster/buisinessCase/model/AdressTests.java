package com.humanbooster.buisinessCase.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.humanbooster.buisinessCase.dto.AdressDTO;

@SpringBootTest(classes = com.humanbooster.buisinessCase.BuisinessCaseApplication.class,  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class AdressTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test_adress_route() {
        // Arrange & Act
        ResponseEntity<String> response = restTemplate.getForEntity("/api/adresses", String.class);

        // Assert
        assertAll("Address API Response Validation",
            () -> assertNotNull(response, "Response should not be null"),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be 200 OK"),
            () -> assertNotNull(response.getBody(), "Response body should not be null")
        );
    }

    @Test
    public void test_get_adress_route_with_id() {
        // Arrange & Act
        Long idToGet = 1L; // Assuming this ID exists in the database
        ResponseEntity<String> response = restTemplate.getForEntity("/api/adresses/" + idToGet, String.class);

        // Assert
        assertAll("Address API Response Validation",
            () -> assertNotNull(response, "Response should not be null"),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be 200 OK"),
            () -> assertNotNull(response.getBody(), "Response body should not be null")
        );
    }

    @Test
    public void test_get_adress_route_with_invalid_id() {
        // Arrange & Act
        ResponseEntity<AdressDTO> response = restTemplate.getForEntity("/api/adresses/999", AdressDTO.class);

        // Assert
        assertAll("Address API Response Validation for Invalid ID",
            () -> assertNotNull(response, "Response should not be null"),
            () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be 404 Not Found"),
            () -> assertTrue(response.getBody() == null, "Response body should indicate not found")
        );

    }

    @Test
    public void test_save_adress_route(){
        // Arrange
        AdressDTO newAdressDTO = new AdressDTO();
        newAdressDTO.setAdressname("Test Save Adress");
        newAdressDTO.setStreetnumber("123");
        newAdressDTO.setStreetname("Test Save Street");
        newAdressDTO.setZipcode("75000");
        newAdressDTO.setCity("Paris");
        newAdressDTO.setCountry("France");
        newAdressDTO.setRegion("Île-de-France");
        newAdressDTO.setAddendum("Bâtiment A");
        newAdressDTO.setFloor(1);
        newAdressDTO.setUserList(new ArrayList<>());

        // Act
        ResponseEntity<AdressDTO> response = restTemplate.postForEntity("/api/adresses", newAdressDTO, AdressDTO.class);

        // Assert
        assertAll("Address API Response Validation",
            () -> assertNotNull(response, "Response should not be null"),
            () -> assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status should be 201 Created"),
            () -> assertNotNull(response.getBody(), "Response body should not be null"),
            () -> assertEquals("Test Save Adress", response.getBody().getAdressname(), "Address name should match"),
            () -> assertEquals("123", response.getBody().getStreetnumber(), "Street number should match")
        );
    }

    @Test
    public void test_delete_adress_route_with_id() {
        // Arrange
        Long idToDelete = 2L; // Assuming this ID exists in the database

        // Act
        ResponseEntity<Void> response = restTemplate.exchange("/api/adresses/" + idToDelete, HttpMethod.DELETE, null, Void.class);

        // Assert
        assertAll("Address API Response Validation for Delete",
            () -> assertNotNull(response, "Response should not be null"),
            () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Status should be 204 No Content")
        );
    }
}