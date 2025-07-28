package com.humanbooster.buisinessCase.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
import com.humanbooster.buisinessCase.dto.VehiculeDTO;
import com.humanbooster.buisinessCase.mapper.StationMapper;
import com.humanbooster.buisinessCase.mapper.UserMapper;
import com.humanbooster.buisinessCase.mapper.VehiculeMapper;
import com.humanbooster.buisinessCase.model.PlugType;
import com.humanbooster.buisinessCase.model.Role;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.model.UserRole;
import com.humanbooster.buisinessCase.model.Vehicule;
import com.humanbooster.buisinessCase.repository.PlugTypeRepository;
import com.humanbooster.buisinessCase.repository.UserRepository;
import com.humanbooster.buisinessCase.security.JwtAuthFilter;
import com.humanbooster.buisinessCase.security.SecurityConfig;
import com.humanbooster.buisinessCase.service.StationService;
import com.humanbooster.buisinessCase.service.UserService;
import com.humanbooster.buisinessCase.service.VehiculeService;

@WebMvcTest(controllers = VehiculeController.class, 
           excludeAutoConfiguration = {SecurityAutoConfiguration.class},
           excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
                            classes = {JwtAuthFilter.class,
                                     SecurityConfig.class})})
public class VehiculeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehiculeService vehiculeService;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private PlugTypeRepository plugTypeRepository;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserMapper userMapper;
    @MockitoBean
    private StationService stationService;
    @MockitoBean
    private StationMapper stationMapper;

    @MockitoBean
    private VehiculeMapper vehiculeMapper;
    @Autowired
    private ObjectMapper objectMapper;

    private Vehicule mockTemplateVehicule;
    private VehiculeDTO mockTemplateVehiculeDTO;

    @BeforeEach
    public void setUp() {
        this.mockTemplateVehicule = new Vehicule();
        this.mockTemplateVehicule.setId(1L);
        this.mockTemplateVehicule.setPlate("WX-098-YZ");
        this.mockTemplateVehicule.setBrand("Tesla");
        this.mockTemplateVehicule.setBatteryCapacity(75);
        Role role = new Role();
        role.setId(1L);
        role.setName(UserRole.ADMIN);
        role.setUserList(new ArrayList<>());
        User user1 = new User();
        user1.setId(1L);
        user1.setRoleList(new ArrayList<>(Arrays.asList(role)));
        this.mockTemplateVehicule.setUser(new HashSet<>(Arrays.asList(user1)));
        PlugType plugType1 = new PlugType();
        plugType1.setId(1L);
        PlugType plugType2 = new PlugType();
        plugType2.setId(2L);
        this.mockTemplateVehicule.setPlugType(new HashSet<>(Arrays.asList(plugType1, plugType2)));

        this.mockTemplateVehiculeDTO = new VehiculeDTO();
        this.mockTemplateVehiculeDTO.setId(1L);
        this.mockTemplateVehiculeDTO.setPlate("AB-123-CD");
        this.mockTemplateVehiculeDTO.setBrand("Tesla");
        this.mockTemplateVehiculeDTO.setBatteryCapacity(75);
        this.mockTemplateVehiculeDTO.setUserList(Arrays.asList(1L, 2L));
        this.mockTemplateVehiculeDTO.setPlugTypeList(Arrays.asList(1L, 2L));
        
        // Configure mapper mocks for all tests
        given(vehiculeMapper.toDTO(this.mockTemplateVehicule)).willReturn(this.mockTemplateVehiculeDTO);
    }

    @Test
    public void test_get_all_vehicule_route() throws Exception {
        // Arrange == set up mock result
        List<Vehicule> mockVehiculeList = new ArrayList<>();
        Vehicule mockVehicule = this.mockTemplateVehicule;
        mockVehiculeList.add(mockVehicule);
        // Mock Behaviour
        given(vehiculeService.getAllVehicules()).willReturn(mockVehiculeList);

        // Act & Assert
        mockMvc.perform(get("/api/vehicules"))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Status should be 200 OK"))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString(), "Response body should not be null"))
                .andExpect(result -> assertTrue(!result.getResponse().getContentAsString().isEmpty(), "Response body should not be empty"));
    }

    @Test
    public void test_get_vehicule_route_with_id() throws Exception {
        // Arrange
        Long idToGet = 1L;
        Vehicule mockVehicule = this.mockTemplateVehicule;
        given(vehiculeService.getVehiculeById(idToGet)).willReturn(Optional.of(mockVehicule));
        // Act & Assert
        MvcResult mvcResult = mockMvc.perform(get("/api/vehicules/" + idToGet))
                .andReturn();
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content, "Response body should not be null");
        VehiculeDTO responseVehiculeDTO = objectMapper.readValue(content, VehiculeDTO.class);

        VehiculeDTO expectedVehiculeDTO = mockTemplateVehiculeDTO;

        // Check all Fields match
        Field[] responseFields = responseVehiculeDTO.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedVehiculeDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedVehiculeDTO), responseField.get(responseVehiculeDTO),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_get_vehicule_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToGet = 999L;
        given(vehiculeService.getVehiculeById(idToGet)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/vehicules/" + idToGet))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_save_vehicule_route() throws Exception   {
        // Arrange
        VehiculeDTO newVehiculeDTO = this.mockTemplateVehiculeDTO;

        Vehicule mockVehicule = new Vehicule();
        mockVehicule.setId(1L);

        given(vehiculeService.saveVehicule(any(Vehicule.class))).willReturn(mockVehicule);
        given(vehiculeMapper.toDTO(mockVehicule)).willReturn(newVehiculeDTO);
        given(vehiculeMapper.toEntity(newVehiculeDTO)).willReturn(this.mockTemplateVehicule);
        // ACT
        MvcResult mvcResult = mockMvc.perform(post("/api/vehicules")
                .content(objectMapper.writeValueAsString(newVehiculeDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content, "Response body should not be null");
        VehiculeDTO responseVehicule = objectMapper.readValue(content, VehiculeDTO.class);
        assertEquals(newVehiculeDTO.getId(), responseVehicule.getId(), "ID should match the mock value");
    }

    @Test
    public void test_delete_vehicule_route_with_id() throws Exception {
        // Arrange
        Long idToDelete = 1L;
        Vehicule mockVehicule = this.mockTemplateVehicule;
        mockVehicule.setId(idToDelete);
        given(vehiculeService.deleteVehiculeById(idToDelete)).willReturn(Optional.of(mockVehicule));

        // Act & Assert
        mockMvc.perform(delete("/api/vehicules/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Status should be 204 No Content"));
    }

    @Test
    public void test_delete_vehicule_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToDelete = 999L;
        given(vehiculeService.deleteVehiculeById(idToDelete)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/vehicules/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_update_vehicule_route() throws Exception {
        // Arrange
        Long idToUpdate = 1L;
        Vehicule mockVehicule = this.mockTemplateVehicule;

        given(vehiculeService.updateVehicule(any(Long.class), any(Vehicule.class))).willReturn(Optional.of(mockVehicule));
        given(vehiculeMapper.toDTO(mockVehicule)).willReturn(this.mockTemplateVehiculeDTO);
        given(vehiculeMapper.toEntity(any(VehiculeDTO.class))).willReturn(mockVehicule);
        
        // Create StationDTO to send in the request
        VehiculeDTO newVehiculeDTO = new VehiculeDTO();
        newVehiculeDTO.setId(1L);
        newVehiculeDTO.setPlate("AB-123-CD");
        newVehiculeDTO.setBrand("Tesla");
        newVehiculeDTO.setBatteryCapacity(75);
        User user1 = new User();
        user1.setId(1L);
        newVehiculeDTO.setUserList(List.of(user1.getId()));
        PlugType plugType1 = new PlugType();
        plugType1.setId(1L);
        PlugType plugType2 = new PlugType();
        plugType2.setId(2L);
        newVehiculeDTO.setPlugTypeList(List.of(plugType1.getId(), plugType2.getId()));

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/api/vehicules/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newVehiculeDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();        // Assert
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content, "Response body should not be null");
        VehiculeDTO responseVehicule = objectMapper.readValue(content, VehiculeDTO.class);
        VehiculeDTO expectedVehiculeDTO = this.mockTemplateVehiculeDTO;

        // Verify specific field changes
        assertEquals(expectedVehiculeDTO.getId(), responseVehicule.getId(), "ID should match");
        assertEquals(expectedVehiculeDTO.getPlate(), responseVehicule.getPlate(), "Plate should match");
        assertEquals(expectedVehiculeDTO.getBrand(), responseVehicule.getBrand(), "Brand should match");
        assertEquals(expectedVehiculeDTO.getBatteryCapacity(), responseVehicule.getBatteryCapacity(), "Battery capacity should match");
        assertEquals(expectedVehiculeDTO.getUserList(), responseVehicule.getUserList(), "User list should match");
        assertEquals(expectedVehiculeDTO.getPlugTypeList(), responseVehicule.getPlugTypeList(), "Plug type list should match");
    }

    @Test
    public void test_update_vehicule_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToUpdate = 999L;
        given(vehiculeService.updateVehicule(any(Long.class), any(Vehicule.class))).willReturn(Optional.empty());

        // Create VehiculeDTO to send in the request
        VehiculeDTO newVehiculeDTO = this.mockTemplateVehiculeDTO;
        newVehiculeDTO.setId(idToUpdate);

        // Act & Assert
        mockMvc.perform(put("/api/vehicules/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newVehiculeDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }
}