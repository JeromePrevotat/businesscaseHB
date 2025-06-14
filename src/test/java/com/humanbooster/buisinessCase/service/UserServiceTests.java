package com.humanbooster.buisinessCase.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.humanbooster.buisinessCase.model.User;
import com.humanbooster.buisinessCase.model.UserRole;
import com.humanbooster.buisinessCase.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private User mockTemplateUser;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();

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
        this.mockTemplateUser.setRole(UserRole.REGISTERED);
        this.mockTemplateUser.setIban("FR1420041010050500013M02606");
        this.mockTemplateUser.setBanned(false);
        this.mockTemplateUser.setVehiculeList(new HashSet<>());
        this.mockTemplateUser.setAdressList(new HashSet<>());
        this.mockTemplateUser.setReservationList(new ArrayList<>());
    }

    @Test
    void test_save_user_service(){
        // Arrange
        User newUser = new User();
        newUser.setId(mockTemplateUser.getId());
        newUser.setUsername(mockTemplateUser.getUsername());
        newUser.setFirstname(mockTemplateUser.getFirstname());
        newUser.setLastname(mockTemplateUser.getLastname());
        newUser.setPassword(mockTemplateUser.getPassword());
        newUser.setEmail(mockTemplateUser.getEmail());
        newUser.setBirthDate(mockTemplateUser.getBirthDate());
        newUser.setInscriptionDate(mockTemplateUser.getInscriptionDate());
        newUser.setAccountValid(mockTemplateUser.isAccountValid());
        newUser.setValidationCode(mockTemplateUser.getValidationCode());
        newUser.setRole(mockTemplateUser.getRole());
        newUser.setIban(mockTemplateUser.getIban());
        newUser.setBanned(mockTemplateUser.isBanned());
        newUser.setVehiculeList(mockTemplateUser.getVehiculeList());
        newUser.setAdressList(mockTemplateUser.getAdressList());
        newUser.setReservationList(mockTemplateUser.getReservationList());

        User mockUser = this.mockTemplateUser;
        when(userRepository.save(newUser)).thenReturn(mockUser);

        // Act
        User savedUser = userService.saveUser(newUser);

        // Assert
        assertAll(
            () -> assertNotNull(savedUser, "Saved user should not be null"),
            () -> assertEquals(mockUser.getId(), savedUser.getId(), "Saved user ID should match"),
            () -> assertEquals(mockUser.getUsername(), savedUser.getUsername(), "Saved user username should match"),
            () -> assertEquals(mockUser.getFirstname(), savedUser.getFirstname(), "Saved user firstname should match"),
            () -> assertEquals(mockUser.getLastname(), savedUser.getLastname(), "Saved user lastname should match"),
            () -> assertEquals(mockUser.getPassword(), savedUser.getPassword(), "Saved user password should match"),
            () -> assertEquals(mockUser.getEmail(), savedUser.getEmail(), "Saved user email should match"),
            () -> assertEquals(mockUser.getBirthDate(), savedUser.getBirthDate(), "Saved user birth date should match"),
            () -> assertEquals(mockUser.getInscriptionDate(), savedUser.getInscriptionDate(), "Saved user inscription date should match"),
            () -> assertEquals(mockUser.isAccountValid(), savedUser.isAccountValid(), "Saved user account validity should match"),
            () -> assertEquals(mockUser.getValidationCode(), savedUser.getValidationCode(), "Saved user validation code should match"),
            () -> assertEquals(mockUser.getRole(), savedUser.getRole(), "Saved user role should match"),
            () -> assertEquals(mockUser.getIban(), savedUser.getIban(), "Saved user IBAN should match"),
            () -> assertEquals(mockUser.isBanned(), savedUser.isBanned(), "Saved user banned status should match"),
            () -> assertEquals(mockUser.getVehiculeList(), savedUser.getVehiculeList(), "Saved user vehicle list should match"),
            () -> assertEquals(mockUser.getAdressList(), savedUser.getAdressList(), "Saved user address list should match"),
            () -> assertEquals(mockUser.getReservationList(), savedUser.getReservationList(), "Saved user reservation list should match")
        );
    }
    
    @Test
    public void test_get_all_users_service() throws IllegalAccessException {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        User user1 = new User();
        user1.setUsername("Test User 1");
        user1.setFirstname("Test");
        user1.setLastname("User 1");
        user1.setPassword("password1");
        user1.setEmail("test1@example.com");
        user1.setBirthDate(LocalDate.of(1990, 1, 1));
        user1.setInscriptionDate(now);
        user1.setAccountValid(true);
        user1.setValidationCode("code1");
        user1.setRole(UserRole.REGISTERED);
        user1.setIban("FR7612345678901234567890123");
        user1.setBanned(false);
        user1.setVehiculeList(new HashSet<>());
        user1.setAdressList(new HashSet<>());
        user1.setReservationList(new ArrayList<>());

        User user2 = new User();
        user2.setUsername("Test User 2");
        user2.setFirstname("Test");
        user2.setLastname("User 2");
        user2.setPassword("password2");
        user2.setEmail("test2@example.com");
        user2.setBirthDate(LocalDate.of(1991, 2, 2));
        user2.setInscriptionDate(now);
        user2.setAccountValid(true);
        user2.setValidationCode("code2");
        user2.setRole(UserRole.REGISTERED);
        user2.setIban("FR7612345678901234567890124");
        user2.setBanned(false);
        user2.setVehiculeList(new HashSet<>());
        user2.setAdressList(new HashSet<>());
        user2.setReservationList(new ArrayList<>());

        ArrayList<User> mockUsers = new ArrayList<>();
        mockUsers.add(user1);
        mockUsers.add(user2);

        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act
        List<User> results = userService.getAllUsers();

        // Assert
        assertNotNull(results, "Users list should not be null");
        assertEquals(2, results.size(), "Users list size should match");

        // Check Lists content without any order
        assertThat(results).containsExactlyInAnyOrderElementsOf(mockUsers);
    }

    @Test
    public void test_get_user_by_id_service() {
        // Arrange
        Long userId = 1L;
        User mockUser = this.mockTemplateUser;
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> resultUser = userService.getUserById(userId);

        // Assert
        assertTrue(resultUser.isPresent(), "User should be found");
        assertEquals(mockUser, resultUser.get(), "Result User should match the mock");
    }

    @Test
    public void test_get_user_by_id_service_with_invalid_id() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> resultUser = userService.getUserById(userId);

        // Assert
        assertTrue(resultUser.isEmpty(), "User should not be found");
    }

    @Test
    public void test_delete_user_by_id_service() {
        // Arrange
        Long userId = 1L;
        User mockUser = this.mockTemplateUser;
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        
        // Act
        Optional<User> resultUser = userService.deleteUserById(userId);

        // Assert
        assertTrue(resultUser.isPresent(), "Deleted User should be returned");
        assertEquals(mockUser, resultUser.get(), "Deleted User should match the mock");
    }

    @Test
    public void test_delete_user_by_id_service_with_invalid_id() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> resultUser = userService.deleteUserById(userId);

        // Assert
        assertTrue(resultUser.isEmpty(), "Deleted User should not be found");
    }

    @Test
    public void test_update_user_service() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("Old Username");
        existingUser.setFirstname("Old Firstname");
        existingUser.setLastname("Old Lastname");
        existingUser.setPassword("OldPassword");
        existingUser.setEmail("old@example.com");
        existingUser.setBirthDate(LocalDate.of(1990, 1, 1));
        existingUser.setInscriptionDate(this.mockTemplateUser.getInscriptionDate());
        existingUser.setAccountValid(true);
        existingUser.setValidationCode("OldCode");
        existingUser.setRole(UserRole.REGISTERED);
        existingUser.setIban("FR7612345678901234567890123");
        existingUser.setBanned(false);
        existingUser.setVehiculeList(new HashSet<>());
        existingUser.setAdressList(new HashSet<>());
        existingUser.setReservationList(new ArrayList<>());


        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("Updated Username");
        mockUser.setFirstname("Updated Firstname");
        mockUser.setLastname("Updated Lastname");
        mockUser.setPassword("UpdatedPassword");
        mockUser.setEmail("updated@example.com");
        mockUser.setBirthDate(LocalDate.of(1990, 1, 1));
        mockUser.setInscriptionDate(this.mockTemplateUser.getInscriptionDate());
        mockUser.setAccountValid(true);
        mockUser.setValidationCode("UpdatedCode");
        mockUser.setRole(UserRole.REGISTERED);
        mockUser.setIban("FR7612345678901234567890123");
        mockUser.setBanned(false);
        mockUser.setVehiculeList(new HashSet<>());
        mockUser.setAdressList(new HashSet<>());
        mockUser.setReservationList(new ArrayList<>());

        // userRepository calls the save method to update the User
        // save is the method to mock
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        Optional<User> result = userService.updateUser(userId, mockUser);

        // Assert
        assertTrue(result.isPresent(), "Updated User should be returned");

        User newUser = result.get();

        assertEquals(mockUser.getId(), newUser.getId(), "ID should remain the same after update");
        assertEquals(mockUser.getUsername(), newUser.getUsername(), "Updated username should match");
        assertEquals(mockUser.getFirstname(), newUser.getFirstname(), "Updated firstname should match");
        assertEquals(mockUser.getLastname(), newUser.getLastname(), "Updated lastname should match");
        assertEquals(mockUser.getPassword(), newUser.getPassword(), "Updated password should match");
        assertEquals(mockUser.getEmail(), newUser.getEmail(), "Updated email should match");
        assertEquals(mockUser.getBirthDate(), newUser.getBirthDate(), "Updated birth date should match");
        assertEquals(mockUser.getInscriptionDate(), newUser.getInscriptionDate(), "Updated inscription date should match");
        assertEquals(mockUser.isAccountValid(), newUser.isAccountValid(), "Updated account validity should match");
        assertEquals(mockUser.getValidationCode(), newUser.getValidationCode(), "Updated validation code should match");
        assertEquals(mockUser.getIban(), newUser.getIban(), "Updated IBAN should match");
        assertEquals(mockUser.isBanned(), newUser.isBanned(), "Updated banned status should match");
        assertEquals(mockUser.getRole(), newUser.getRole(), "Updated role should match");
        assertEquals(mockUser.getVehiculeList(), newUser.getVehiculeList(), "Updated vehicle list should match");
        assertEquals(mockUser.getAdressList(), newUser.getAdressList(), "Updated address list should match");
        assertEquals(mockUser.getReservationList(), newUser.getReservationList(), "Updated reservation list should match");
    }

    @Test
    public void test_update_user_service_with_invalid_id() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.updateUser(userId, mockUser);

        // Assert
        assertTrue(result.isEmpty(), "Updated User should not be returned");
    }
}