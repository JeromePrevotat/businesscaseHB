package com.humanbooster.businesscase.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
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
import com.humanbooster.businesscase.dto.AdressDTO;
import com.humanbooster.businesscase.mapper.AdressMapper;
import com.humanbooster.businesscase.mapper.StationMapper;
import com.humanbooster.businesscase.mapper.UserMapper;
import com.humanbooster.businesscase.model.Adress;
import com.humanbooster.businesscase.service.AdressService;
import com.humanbooster.businesscase.service.RefreshTokenService;
import com.humanbooster.businesscase.service.StationService;
import com.humanbooster.businesscase.service.UserService;

@WebMvcTest(controllers = AdressController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class,
                                SecurityFilterAutoConfiguration.class},
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.humanbooster\\.businesscase\\.security\\..*"))
// Disable security filters for testing
@AutoConfigureMockMvc(addFilters = false)
public class AdressControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdressService adressService;
    @MockitoBean
    private RefreshTokenService refreshTokenService;
    @MockitoBean
    private AdressMapper adressMapper;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserMapper userMapper;
    @MockitoBean
    private StationService stationService;
    @MockitoBean
    private StationMapper stationMapper;
    
    @Autowired
    private ObjectMapper objectMapper;

    private Adress mockTemplateAdress;
    private AdressDTO mockTemplateAdressDTO;

    @BeforeEach
    public void setUp() {
        this.mockTemplateAdress = new Adress();
        this.mockTemplateAdress.setId(1L);
        this.mockTemplateAdress.setAdressname("Test Adress");
        this.mockTemplateAdress.setStreetnumber("123");
        this.mockTemplateAdress.setStreetname("Test Street");
        this.mockTemplateAdress.setZipcode("75000");
        this.mockTemplateAdress.setCity("Paris");
        this.mockTemplateAdress.setCountry("France");
        this.mockTemplateAdress.setRegion("Île-de-France");
        this.mockTemplateAdress.setAddendum("Bâtiment A");
        this.mockTemplateAdress.setFloor(1);
        this.mockTemplateAdress.setUserList(new ArrayList<>());

        this.mockTemplateAdressDTO = new AdressDTO();
        this.mockTemplateAdressDTO.setId(1L);
        this.mockTemplateAdressDTO.setAdressname("Test Adress");
        this.mockTemplateAdressDTO.setStreetnumber("123");
        this.mockTemplateAdressDTO.setStreetname("Test Street");
        this.mockTemplateAdressDTO.setZipcode("75000");
        this.mockTemplateAdressDTO.setCity("Paris");
        this.mockTemplateAdressDTO.setCountry("France");
        this.mockTemplateAdressDTO.setRegion("Île-de-France");
        this.mockTemplateAdressDTO.setAddendum("Bâtiment A");
        this.mockTemplateAdressDTO.setFloor(1);
        this.mockTemplateAdressDTO.setUserList(new ArrayList<>());
    }

    @Test
    public void test_get_all_adress_route() throws Exception {
        // Arrange == set up mock result
        List<Adress> mockAdresses = new ArrayList<>();
        Adress mockAdress = this.mockTemplateAdress;
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
        Long idToGet = 1L;
        Adress mockAdress = this.mockTemplateAdress;
        AdressDTO mockAdressDTO = this.mockTemplateAdressDTO;
        given(adressService.getAdressById(idToGet)).willReturn(Optional.of(mockAdress));
        given(adressMapper.toDTO(mockAdress)).willReturn(mockAdressDTO);

        // Act & Assert
        MvcResult mvcResult = mockMvc.perform(get("/api/adresses/" + idToGet))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        AdressDTO responseAdressDTO = objectMapper.readValue(content, AdressDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

        AdressDTO expectedAdressDTO = adressMapper.toDTO(mockAdress);

        // Check all Fields match
        Field[] responseFields = responseAdressDTO.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedAdressDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedAdressDTO), responseField.get(responseAdressDTO),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_get_adress_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToGet = 999L;
        given(adressService.getAdressById(idToGet)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/adresses/" + idToGet))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_save_adress_route() throws Exception   {
        // Arrange
        AdressDTO newAdressDTO = this.mockTemplateAdressDTO;

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
        given(adressMapper.toDTO(mockAdressService)).willReturn(new AdressDTO(1L, "Test Save Adress", "123", "Test Save Street", "75000", "Paris", "France", "Île-de-France", "Bâtiment A", 1, new ArrayList<>(), new ArrayList<>()));
        given(adressMapper.toEntity(newAdressDTO)).willReturn(this.mockTemplateAdress);

        // ACT
        MvcResult mvcResult = mockMvc.perform(post("/api/adresses")
                .content(objectMapper.writeValueAsString(newAdressDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String content = mvcResult.getResponse().getContentAsString();
        AdressDTO responseAdress = objectMapper.readValue(content, AdressDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        assertNotNull(content, "Response body should not be null");
        assertEquals("Test Save Adress", responseAdress.getAdressname(), "Address name should match");
        assertEquals("123", responseAdress.getStreetnumber(), "Street number should match");
    }

    @Test
    public void test_delete_adress_route_with_id() throws Exception {
        // Arrange
        Long idToDelete = 1L;
        Adress mockAdress = this.mockTemplateAdress;
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
        given(adressService.deleteAdressById(idToDelete)).willReturn(Optional.empty());

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
        Adress mockAdress = this.mockTemplateAdress;

        given(adressService.updateAdress(any(Long.class), any(Adress.class))).willReturn(Optional.of(mockAdress));
        given(adressMapper.toDTO(mockAdress)).willReturn(new AdressDTO(1L, "Test Adress", "123", "Test Street", "75001", "Paris", "France", "Île-de-France", "Apt 1", 1, new ArrayList<>(), new ArrayList<>()));
        given(adressMapper.toEntity(any(AdressDTO.class))).willReturn(mockAdress);

        // Create AdressDTO to send in the request
        AdressDTO newAdressDTO = new AdressDTO();
        newAdressDTO.setId(idToUpdate);
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
                .content(objectMapper.writeValueAsString(newAdressDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        AdressDTO expectedAdressDTO = adressMapper.toDTO(mockAdress);
        String content = mvcResult.getResponse().getContentAsString();
        AdressDTO responseAdress = objectMapper.readValue(content, AdressDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

        // Verify specific field changes
        assertEquals(expectedAdressDTO.getId(), responseAdress.getId(), "ID should match");
        assertEquals(expectedAdressDTO.getAdressname(), responseAdress.getAdressname(), "Adress name should match");
        assertEquals(expectedAdressDTO.getStreetnumber(), responseAdress.getStreetnumber(), "Street number should match");
        assertEquals(expectedAdressDTO.getStreetname(), responseAdress.getStreetname(), "Street name should match");
        assertEquals(expectedAdressDTO.getZipcode(), responseAdress.getZipcode(), "Zip code should match");
        assertEquals(expectedAdressDTO.getCity(), responseAdress.getCity(), "City should match");
        assertEquals(expectedAdressDTO.getCountry(), responseAdress.getCountry(), "Country should match");
        assertEquals(expectedAdressDTO.getRegion(), responseAdress.getRegion(), "Region should match");
        assertEquals(expectedAdressDTO.getAddendum(), responseAdress.getAddendum(), "Addendum should match");
        assertEquals(expectedAdressDTO.getFloor(), responseAdress.getFloor(), "Floor should match");
        assertEquals(expectedAdressDTO.getUserList(), responseAdress.getUserList(), "User list should match");
    }

    @Test
    public void test_update_adress_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToUpdate = 999L;
        given(adressService.updateAdress(any(Long.class), any(Adress.class))).willReturn(Optional.empty());

        // Create AdressDTO to send in the request
        AdressDTO newAdressDTO = this.mockTemplateAdressDTO;
        newAdressDTO.setId(idToUpdate);
        
        // Act & Assert
        mockMvc.perform(put("/api/adresses/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newAdressDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }
}