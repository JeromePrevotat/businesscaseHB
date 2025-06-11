package com.humanbooster.buisinessCase.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanbooster.buisinessCase.dto.AdressDTO;
import com.humanbooster.buisinessCase.mapper.AdressMapper;
import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.repository.AdressRepository;
import com.humanbooster.buisinessCase.service.AdressService;

// @SpringBootTest(classes = com.humanbooster.buisinessCase.BuisinessCaseApplication.class,  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// public class AdressTests {
//     @Autowired
//     private TestRestTemplate restTemplate;
    
//     @Test
//     public void test_adress_route() {
//         // Arrange & Act
//         ResponseEntity<String> response = restTemplate.getForEntity("/api/adresses", String.class);

//         // Assert
//         assertAll("Address API Response Validation",
//             () -> assertNotNull(response, "Response should not be null"),
//             () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be 200 OK"),
//             () -> assertNotNull(response.getBody(), "Response body should not be null")
//         );
//     }

//     @Test
//     public void test_get_adress_route_with_id() {
//         // Arrange & Act
//         Long idToGet = 1L; // Assuming this ID exists in the database
//         ResponseEntity<String> response = restTemplate.getForEntity("/api/adresses/" + idToGet, String.class);

//         // Assert
//         assertAll("Address API Response Validation",
//             () -> assertNotNull(response, "Response should not be null"),
//             () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status should be 200 OK"),
//             () -> assertNotNull(response.getBody(), "Response body should not be null")
//         );
//     }

//     @Test
//     public void test_get_adress_route_with_invalid_id() {
//         // Arrange & Act
//         ResponseEntity<AdressDTO> response = restTemplate.getForEntity("/api/adresses/999", AdressDTO.class);

//         // Assert
//         assertAll("Address API Response Validation for Invalid ID",
//             () -> assertNotNull(response, "Response should not be null"),
//             () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Status should be 404 Not Found"),
//             () -> assertTrue(response.getBody() == null, "Response body should indicate not found")
//         );

//     }

//     @Test
//     public void test_save_adress_route(){
//         // Arrange
//         AdressDTO newAdressDTO = new AdressDTO();
//         newAdressDTO.setAdressname("Test Save Adress");
//         newAdressDTO.setStreetnumber("123");
//         newAdressDTO.setStreetname("Test Save Street");
//         newAdressDTO.setZipcode("75000");
//         newAdressDTO.setCity("Paris");
//         newAdressDTO.setCountry("France");
//         newAdressDTO.setRegion("Île-de-France");
//         newAdressDTO.setAddendum("Bâtiment A");
//         newAdressDTO.setFloor(1);
//         newAdressDTO.setUserList(new ArrayList<>());

//         // Act
//         ResponseEntity<AdressDTO> response = restTemplate.postForEntity("/api/adresses", newAdressDTO, AdressDTO.class);

//         // Assert
//         assertAll("Address API Response Validation",
//             () -> assertNotNull(response, "Response should not be null"),
//             () -> assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status should be 201 Created"),
//             () -> assertNotNull(response.getBody(), "Response body should not be null"),
//             () -> assertEquals("Test Save Adress", response.getBody().getAdressname(), "Address name should match"),
//             () -> assertEquals("123", response.getBody().getStreetnumber(), "Street number should match")
//         );
//     }

//     @Test
//     public void test_delete_adress_route_with_id() {
//         // Arrange
//         Long idToDelete = 2L; // Assuming this ID exists in the database

//         // Act
//         ResponseEntity<Void> response = restTemplate.exchange("/api/adresses/" + idToDelete, HttpMethod.DELETE, null, Void.class);

//         // Assert
//         assertAll("Address API Response Validation for Delete",
//             () -> assertNotNull(response, "Response should not be null"),
//             () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Status should be 204 No Content")
//         );
//     }
// }


@WebMvcTest(AdressController.class)
@Import(AdressMapper.class)
public class AdressTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdressService adressService;
    @MockitoBean
    private AdressRepository adressRepository;

    @Test
    public void test_get_all_adress_route() throws Exception {
        // Arrange
        List<Adress> mockAdresses = new ArrayList<>();
        Adress mockAdress = new Adress();
        mockAdress.setId(1L);
        mockAdress.setAdressname("Test Adress");
        mockAdress.setStreetnumber("123");
        mockAdress.setStreetname("Test Street");
        mockAdress.setZipcode("75000");
        mockAdress.setCity("Paris");
        mockAdress.setCountry("France");
        mockAdress.setRegion("Île-de-France");
        mockAdress.setAddendum("Bâtiment A");
        mockAdress.setFloor(1);
        mockAdress.setUserList(new ArrayList<>());
        mockAdresses.add(mockAdress);
        given(adressService.getAllAdresses()).willReturn(mockAdresses);

        // Act & Assert
        mockMvc.perform(get("/api/adresses"))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Status should be 200 OK"))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString(), "Response body should not be null"))
                .andExpect(result -> assertTrue(!result.getResponse().getContentAsString().isEmpty(), "Response body should not be empty"));
    }

    @Test
    public void test_get_adress_route_with_id() throws Exception {
        // Arrange
        Long idToGet = 3L;
        Adress mockAdress = new Adress();
        mockAdress.setId(idToGet);
        mockAdress.setAdressname("Test Adress");
        mockAdress.setStreetnumber("123");
        mockAdress.setStreetname("Test Street");
        mockAdress.setZipcode("75000");
        mockAdress.setCity("Paris");
        mockAdress.setCountry("France");
        mockAdress.setRegion("Île-de-France");
        mockAdress.setAddendum("Bâtiment A");
        mockAdress.setFloor(1);
        mockAdress.setUserList(new ArrayList<>());
        given(adressService.getAdressById(idToGet)).willReturn(java.util.Optional.of(mockAdress));

        // Act & Assert
        mockMvc.perform(get("/api/adresses/" + idToGet))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Status should be 200 OK"))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString(), "Response body should not be null"));
    }

    @Test
    public void test_get_adress_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToGet = 999L;
        given(adressService.getAdressById(idToGet)).willReturn(java.util.Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/adresses/" + idToGet))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should indicate not found"));
    }

    @Test
    public void test_save_adress_route() throws Exception   {
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

        Adress mockAdressService = new Adress();
        mockAdressService.setId(1L);
        mockAdressService.setAdressname("Test Save Adress");
        mockAdressService.setStreetnumber("123");
        mockAdressService.setStreetname("Test Save Street");
        mockAdressService.setZipcode("75000");
        mockAdressService.setCity("Paris");
        mockAdressService.setCountry("France");
        mockAdressService.setRegion("Île-de-France");
        mockAdressService.setAddendum("Bâtiment A");
        mockAdressService.setFloor(1);
        mockAdressService.setUserList(new ArrayList<>());
        given(adressService.saveAdress(any(Adress.class))).willReturn(mockAdressService);

        // ACT
        MvcResult mvcResult = mockMvc.perform(post("/api/adresses")
                .content(new ObjectMapper().writeValueAsString(newAdressDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String content = mvcResult.getResponse().getContentAsString();
        AdressDTO responseAdress = new ObjectMapper().readValue(content, AdressDTO.class);
        assertNotNull(responseAdress, "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        assertNotNull(content, "Response body should not be null");
        assertTrue(responseAdress.getAdressname().equals("Test Save Adress"), "Address name should match");
        assertTrue(responseAdress.getStreetnumber().equals("123"), "Street number should match");
    }

    @Test
    public void test_delete_adress_route_with_id() throws Exception {
        // Arrange
        Long idToDelete = 2L;
        Adress mockAdress = new Adress();
        mockAdress.setId(idToDelete);
        given(adressService.deleteAdressById(idToDelete)).willReturn(java.util.Optional.of(mockAdress));

        // Act & Assert
        mockMvc.perform(delete("/api/adresses/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Status should be 204 No Content"));
    }

    @Test
    public void test_update_adress_route() throws Exception {
        // Arrange
        Long idToUpdate = 5L;
        Adress updatedAdress = new Adress();
        updatedAdress.setId(idToUpdate);
        updatedAdress.setAdressname("Updated Adress");
        updatedAdress.setStreetnumber("456");
        updatedAdress.setStreetname("Updated Street");
        updatedAdress.setZipcode("12345");
        updatedAdress.setCity("Lyon");
        updatedAdress.setCountry("France");
        updatedAdress.setRegion("Auvergne-Rhône-Alpes");
        updatedAdress.setAddendum("Bâtiment B");
        updatedAdress.setFloor(2);
        updatedAdress.setUserList(new ArrayList<>());

        given(adressService.updateAdress(any(Long.class), any(Adress.class))).willReturn(java.util.Optional.of(updatedAdress));

        // Création de l'objet à envoyer dans la requête
        Adress newAdress = new Adress();
        newAdress.setAdressname("Updated Adress");
        newAdress.setStreetnumber("456");
        newAdress.setStreetname("Updated Street");
        newAdress.setZipcode("12345");
        newAdress.setCity("Lyon");
        newAdress.setCountry("France");
        newAdress.setRegion("Auvergne-Rhône-Alpes");
        newAdress.setAddendum("Bâtiment B");
        newAdress.setFloor(2);
        newAdress.setUserList(new ArrayList<>());

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/api/adresses/" + idToUpdate)
                .content(new ObjectMapper().writeValueAsString(newAdress))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andReturn();

        // Assert
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");
        AdressDTO responseAdress = new ObjectMapper().readValue(content, AdressDTO.class);
        assertEquals("Updated Adress", responseAdress.getAdressname(), "Address name should match");
        assertEquals("456", responseAdress.getStreetnumber(), "Street number should match");
        assertEquals("Lyon", responseAdress.getCity(), "City should match");
    }
}