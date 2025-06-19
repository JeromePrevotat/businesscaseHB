package com.humanbooster.buisinessCase.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import com.humanbooster.buisinessCase.dto.UserDTO;
import com.humanbooster.buisinessCase.mapper.UserMapper;
import com.humanbooster.buisinessCase.model.Media;
import com.humanbooster.buisinessCase.model.Role;
import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.model.UserRole;
import com.humanbooster.buisinessCase.repository.AdressRepository;
import com.humanbooster.buisinessCase.repository.MediaRepository;
import com.humanbooster.buisinessCase.repository.ReservationRepository;
import com.humanbooster.buisinessCase.repository.RoleRepository;
import com.humanbooster.buisinessCase.repository.UserRepository;
import com.humanbooster.buisinessCase.repository.VehiculeRepository;
import com.humanbooster.buisinessCase.service.JwtService;
import com.humanbooster.buisinessCase.service.UserService;

@WebMvcTest(UserController.class)
@Import(UserMapper.class)
// Disable security filters for testing
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private MediaRepository mediaRepository;
    @MockitoBean
    private VehiculeRepository vehiculeRepository;
    @MockitoBean
    private AdressRepository adressRepository;
    @MockitoBean
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;

    private User mockTemplateUser;
    private UserDTO mockTemplateUserDTO;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        Role role = new Role();
        role.setId(1L);
        role.setName(UserRole.ADMIN);

        Media media = new Media();
        media.setId(1L);

        this.mockTemplateUser = new User();
        this.mockTemplateUser.setId(1L);
        this.mockTemplateUser.setUsername("testuser");
        this.mockTemplateUser.setFirstname("John");
        this.mockTemplateUser.setLastname("Doe");
        this.mockTemplateUser.setPassword("password123");
        this.mockTemplateUser.setEmail("john.doe@example.com");
        this.mockTemplateUser.setBirthDate(LocalDate.of(1990, 1, 1));
        this.mockTemplateUser.setInscriptionDate(now);
        this.mockTemplateUser.setAccountValid(true);
        this.mockTemplateUser.setValidationCode("ABC123");
        this.mockTemplateUser.setRoleList(List.of(role));
        this.mockTemplateUser.setIban("FR1420041010050500013M02606");
        this.mockTemplateUser.setBanned(false);
        this.mockTemplateUser.setVehiculeList(new HashSet<>());
        this.mockTemplateUser.setAdressList(new HashSet<>());
        this.mockTemplateUser.setReservationList(new ArrayList<>());
        this.mockTemplateUser.setMedia(media);

        this.mockTemplateUserDTO = new UserDTO();
        this.mockTemplateUserDTO.setId(1L);
        this.mockTemplateUserDTO.setUsername("testuser");
        this.mockTemplateUserDTO.setFirstname("John");
        this.mockTemplateUserDTO.setLastname("Doe");
        this.mockTemplateUserDTO.setEmail("john.doe@example.com");
        this.mockTemplateUserDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        this.mockTemplateUserDTO.setInscriptionDate(now);
        this.mockTemplateUserDTO.setAccountValid(true);
        this.mockTemplateUserDTO.setRoleList(List.of(role.getId()));
        this.mockTemplateUserDTO.setBanned(false);
        this.mockTemplateUserDTO.setVehiculeList(new ArrayList<>());
        this.mockTemplateUserDTO.setAdressList(new ArrayList<>());
        this.mockTemplateUserDTO.setReservationList(new ArrayList<>());
        this.mockTemplateUserDTO.setMedia_id(media.getId());
    }

    @Test
    public void test_get_all_user_route() throws Exception {
        // Arrange == set up mock result
        List<User> mockUserList = new ArrayList<>();
        User mockUser = this.mockTemplateUser;
        mockUserList.add(mockUser);
        // Mock Behaviour
        given(userService.getAllUsers()).willReturn(mockUserList);

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(result -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Status should be 200 OK"))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString(), "Response body should not be null"))
                .andExpect(result -> assertTrue(!result.getResponse().getContentAsString().isEmpty(), "Response body should not be empty"));
    }

    @Test
    public void test_get_user_route_with_id() throws Exception {
        // Arrange
        Long idToGet = 1L;
        User mockUser = this.mockTemplateUser;
        given(userService.getUserById(idToGet)).willReturn(Optional.of(mockUser));

        // Act & Assert
        MvcResult mvcResult = mockMvc.perform(get("/api/users/" + idToGet))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        UserDTO responseUserDTO = objectMapper.readValue(content, UserDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");

        UserDTO expectedUserDTO = userMapper.toDTO(mockUser);

        // Check all Fields match
        Field[] responseFields = responseUserDTO.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedUserDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedUserDTO), responseField.get(responseUserDTO),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_get_user_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToGet = 999L;
        given(userService.getUserById(idToGet)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/users/" + idToGet))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_save_user_route() throws Exception   {
        // Arrange
        UserDTO newUserDTO = this.mockTemplateUserDTO;

        User mockUserService = new User();
        mockUserService.setId(1L);

        given(userService.saveUser(any(User.class))).willReturn(mockUserService);

        // ACT
        MvcResult mvcResult = mockMvc.perform(post("/api/users")
                .content(objectMapper.writeValueAsString(newUserDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        String content = mvcResult.getResponse().getContentAsString();
        UserDTO responseUser = objectMapper.readValue(content, UserDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus(), "Status should be 201 Created");
        assertNotNull(content, "Response body should not be null");
        assertEquals(newUserDTO.getId(), responseUser.getId(), "ID should match the mock value");
    }

    @Test
    public void test_delete_user_route_with_id() throws Exception {
        // Arrange
        Long idToDelete = 1L;
        User mockUser = this.mockTemplateUser;
        mockUser.setId(idToDelete);
        given(userService.deleteUserById(idToDelete)).willReturn(Optional.of(mockUser));

        // Act & Assert
        mockMvc.perform(delete("/api/users/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Status should be 204 No Content"));
    }

    @Test
    public void test_delete_user_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToDelete = 999L;
        given(userService.deleteUserById(idToDelete)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/users/" + idToDelete))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }

    @Test
    public void test_update_user_route() throws Exception {
        // Arrange
        Long idToUpdate = 1L;
        User mockUser = this.mockTemplateUser;

        given(userService.updateUser(any(Long.class), any(User.class))).willReturn(Optional.of(mockUser));
        // Create UserDTO to send in the request
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setId(idToUpdate);
        newUserDTO.setUsername("Updated Username");
        newUserDTO.setFirstname(this.mockTemplateUserDTO.getFirstname());
        newUserDTO.setLastname(this.mockTemplateUserDTO.getLastname());
        newUserDTO.setEmail(this.mockTemplateUserDTO.getEmail());
        newUserDTO.setBirthDate(this.mockTemplateUserDTO.getBirthDate());
        newUserDTO.setInscriptionDate(this.mockTemplateUserDTO.getInscriptionDate());
        newUserDTO.setAccountValid(this.mockTemplateUserDTO.getAccountValid());
        newUserDTO.setRoleList(this.mockTemplateUserDTO.getRoleList());
        newUserDTO.setBanned(this.mockTemplateUserDTO.getBanned());
        newUserDTO.setVehiculeList(this.mockTemplateUserDTO.getVehiculeList());
        newUserDTO.setAdressList(this.mockTemplateUserDTO.getAdressList());
        newUserDTO.setReservationList(this.mockTemplateUserDTO.getReservationList());
        newUserDTO.setMedia_id(this.mockTemplateUserDTO.getMedia_id());

        // Act
        MvcResult mvcResult = mockMvc.perform(put("/api/users/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newUserDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        UserDTO expectedUserDTO = userMapper.toDTO(mockUser);
        String content = mvcResult.getResponse().getContentAsString();
        UserDTO responseUser = objectMapper.readValue(content, UserDTO.class);
        assertNotNull(mvcResult.getResponse(), "Response should not be null");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Status should be 200 OK");
        assertNotNull(content, "Response body should not be null");


        // Check all Fields match
        Field[] responseFields = responseUser.getClass().getDeclaredFields();
        for (Field responseField : responseFields) {
            // Ignore immutable Fields
            if (Modifier.isStatic(responseField.getModifiers()) || Modifier.isFinal(responseField.getModifiers())) continue;
            responseField.setAccessible(true);
            Field expectedField = expectedUserDTO.getClass().getDeclaredField(responseField.getName());
            expectedField.setAccessible(true);
            assertEquals(expectedField.get(expectedUserDTO), responseField.get(responseUser),
                         "Field " + responseField.getName() + " should match the mock value");
        }
    }

    @Test
    public void test_update_user_route_with_invalid_id() throws Exception {
        // Arrange
        Long idToUpdate = 999L;
        given(userService.updateUser(any(Long.class), any(User.class))).willReturn(Optional.empty());

        // Create UserDTO to send in the request
        UserDTO newUserDTO = this.mockTemplateUserDTO;
        newUserDTO.setId(idToUpdate);

        // Act & Assert
        mockMvc.perform(put("/api/users/" + idToUpdate)
                .content(objectMapper.writeValueAsString(newUserDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertNotNull(result.getResponse(), "Response should not be null"))
                .andExpect(result -> assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status should be 404 Not Found"))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().isEmpty(), "Response body should be empty"));
    }
}