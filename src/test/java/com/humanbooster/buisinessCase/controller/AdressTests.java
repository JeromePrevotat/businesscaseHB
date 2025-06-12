package com.humanbooster.buisinessCase.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanbooster.buisinessCase.dto.AdressDTO;
import com.humanbooster.buisinessCase.mapper.AdressMapper;
import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.repository.AdressRepository;
import com.humanbooster.buisinessCase.service.AdressService;


@WebMvcTest(AdressController.class)
@Import(AdressMapper.class)
public class AdressTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdressService adressService;
    @MockitoBean
    private AdressRepository adressRepository;
    @Autowired
    private AdressMapper adressMapper;

    @Test
    public void test_get_all_adress_route() throws Exception {
        // Arrange == set up mock result
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
        // Mock Behaviour
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
        given(adressService.getAdressById(idToGet)).willReturn(Optional.of(mockAdress));

        // Act & Assert
        MvcResult mvcResult = mockMvc.perform(get("/api/adresses/" + idToGet))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Adress responseAdress = new ObjectMapper().readValue(content, Adress.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

        // Check all Fields match
        Field[] responseFields = responseAdress.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field mockField = mockAdress.getClass().getDeclaredField(responseField.getName());
            mockField.setAccessible(true);
            assertEquals(mockField.get(mockAdress), responseField.get(responseAdress),
                         "Field " + responseField.getName() + " should match the mock value");
        }
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
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
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
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        assertNotNull(content, "Response body should not be null");
        assertEquals("Test Save Adress", responseAdress.getAdressname(), "Address name should match");
        assertEquals("123", responseAdress.getStreetnumber(), "Street number should match");
    }

    @Test
    public void test_delete_adress_route_with_id() throws Exception {
        // Arrange
        Long idToDelete = 2L;
        Adress mockAdress = new Adress();
        mockAdress.setId(idToDelete);
        given(adressService.deleteAdressById(idToDelete)).willReturn(Optional.of(mockAdress));

        // Act & Assert
        mockMvc.perform(delete("/api/adresses/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Status should be 204 No Content"));
    }

    @Test
    public void test_delete_adress_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToDelete = 999L;
        given(adressService.deleteAdressById(idToDelete)).willReturn(java.util.Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/adresses/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_update_adress_route() throws Exception {
        // Arrange
        Long idToUpdate = 5L;
        Adress mockAdress = new Adress();
        mockAdress.setId(idToUpdate);
        mockAdress.setAdressname("Updated Adress");
        mockAdress.setStreetnumber("456");
        mockAdress.setStreetname("Updated Street");
        mockAdress.setZipcode("12345");
        mockAdress.setCity("Lyon");
        mockAdress.setCountry("France");
        mockAdress.setRegion("Auvergne-Rhône-Alpes");
        mockAdress.setAddendum("Bâtiment B");
        mockAdress.setFloor(2);
        mockAdress.setUserList(new ArrayList<>());

        given(adressService.updateAdress(any(Long.class), any(Adress.class))).willReturn(Optional.of(mockAdress));

        // Create AdressDTO to send in the request
        AdressDTO newAdressDTO = new AdressDTO();
        newAdressDTO.setAdressname("Updated Adress");
        newAdressDTO.setStreetnumber("456");
        newAdressDTO.setStreetname("Updated Street");
        newAdressDTO.setZipcode("12345");
        newAdressDTO.setCity("Lyon");
        newAdressDTO.setCountry("France");
        newAdressDTO.setRegion("Auvergne-Rhône-Alpes");
        newAdressDTO.setAddendum("Bâtiment B");
        newAdressDTO.setFloor(2);
        newAdressDTO.setUserList(new ArrayList<>());

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/api/adresses/" + idToUpdate)
                .content(new ObjectMapper().writeValueAsString(newAdressDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        String content = mvcResult.getResponse().getContentAsString();
        AdressDTO responseAdress = adressMapper.toDTO(new ObjectMapper().readValue(content, Adress.class));
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");
        // Check all Fields match
        Field[] responseFields = responseAdress.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field mockField = mockAdress.getClass().getDeclaredField(responseField.getName());
            mockField.setAccessible(true);
            assertEquals(mockField.get(mockAdress), responseField.get(responseAdress),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_update_adress_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToUpdate = 999L;
        Adress mockAdress = new Adress();
        mockAdress.setId(idToUpdate);
        mockAdress.setAdressname("Updated Adress");
        mockAdress.setStreetnumber("456");
        mockAdress.setStreetname("Updated Street");
        mockAdress.setZipcode("12345");
        mockAdress.setCity("Lyon");
        mockAdress.setCountry("France");
        mockAdress.setRegion("Auvergne-Rhône-Alpes");
        mockAdress.setAddendum("Bâtiment B");
        mockAdress.setFloor(2);
        mockAdress.setUserList(new ArrayList<>());

        given(adressService.updateAdress(any(Long.class), any(Adress.class))).willReturn(Optional.empty());

        // Create AdressDTO to send in the request
        AdressDTO newAdressDTO = new AdressDTO();
        newAdressDTO.setAdressname("Updated Adress");
        newAdressDTO.setStreetnumber("456");
        newAdressDTO.setStreetname("Updated Street");
        newAdressDTO.setZipcode("12345");
        newAdressDTO.setCity("Lyon");
        newAdressDTO.setCountry("France");
        newAdressDTO.setRegion("Auvergne-Rhône-Alpes");
        newAdressDTO.setAddendum("Bâtiment B");
        newAdressDTO.setFloor(2);
        newAdressDTO.setUserList(new ArrayList<>());
        
        // Act & Assert
        mockMvc.perform(put("/api/adresses/" + idToUpdate)
                .content(new ObjectMapper().writeValueAsString(newAdressDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }
}