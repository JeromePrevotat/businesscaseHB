package com.humanbooster.buisinessCase.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(classes = com.humanbooster.buisinessCase.BuisinessCaseApplication.class,  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class PlugTypeTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testPlugType() {
        // Arrange & Act
        ResponseEntity<String> response = restTemplate.getForEntity("/api/plug-types", String.class);

        // Assert
        assertAll("Plug Type API Response Validation",
            () -> assertNotNull(response, "Response should not be null"),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be 200 OK"),
            () -> assertNotNull(response.getBody(), "Response body should not be null")
        );
    }
}