package com.humanbooster.businesscase.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.humanbooster.businesscase.model.Adress;
import com.humanbooster.businesscase.model.Media;
import com.humanbooster.businesscase.model.Role;
import com.humanbooster.businesscase.model.Spot;
import com.humanbooster.businesscase.model.User;
import com.humanbooster.businesscase.model.UserRole;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Force DtaJPaTest to use my own mysql DB instead of H2 
@ActiveProfiles("repository-test") // Use the empty test db specifically for the Repository Tests
public class MediaRepositoryTests {
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RoleRepository roleRepository;

    private Media testMedia;
    private Media testMedia2;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        Role role = new Role();
        role.setName(UserRole.ROLE_ADMIN);
        role = roleRepository.saveAndFlush(role);

        User user1 = new User();
        user1.setUsername("testuser");
        user1.setFirstname("Jean");
        user1.setLastname("Dupont");
        user1.setPassword("MotDePasse123!");
        user1.setEmail("jean.dupont@test.com");
        user1.setBirthDate(LocalDate.of(1990, 5, 15));
        user1.setInscriptionDate(now);
        user1.setAccountValid(true);
        user1.setBanned(false);
        user1.setRoleList(List.of(role));
        user1.setVehiculeList(new HashSet<>());
        user1.setAdressList(new HashSet<>());
        User persistedUser1 = entityManager.persistAndFlush(user1);

        Adress adress = new Adress();
        adress.setAdressname("Station de test");
        adress.setStreetnumber("123");
        adress.setStreetname("Rue de la Paix");
        adress.setZipcode("75001");
        adress.setCity("Paris");
        adress.setCountry("France");
        adress.setRegion("Île-de-France");
        adress.setAddendum("Bâtiment A");
        adress.setFloor(0);
        adress.setUserList(new ArrayList<>());

        Adress persistedAdress = entityManager.persistAndFlush(adress);

        Spot spot = new Spot();
        spot.setInstruction("Instructions pour utiliser cette borne");
        spot.setAdress(persistedAdress);
        spot.setMediaList(new ArrayList<>());

        Spot persistedSpot = entityManager.persistAndFlush(spot);

        this.testMedia = new Media();
        this.testMedia.setUrl("https://example.com/test-media.jpg");
        this.testMedia.setType("image/jpeg");
        this.testMedia.setMediaName("Test Media File");
        this.testMedia.setSize(1048576L);
        this.testMedia.setUser(persistedUser1);
        this.testMedia.setSpot(null);
        this.testMedia.setStation(null);
        
        this.testMedia2 = new Media();
        this.testMedia2.setUrl("https://example.com/test-media2.jpg");
        this.testMedia2.setType("image/jpeg");
        this.testMedia2.setMediaName("Test Media File 2");
        this.testMedia2.setSize(1048576L);
        this.testMedia2.setUser(null);
        this.testMedia2.setSpot(persistedSpot);
        this.testMedia2.setStation(null);

        entityManager.clear();

    }

    @Test
    public void test_save_media(){
        // Arrange
        Media newMedia = this.testMedia;

        // Act
        Media resultMedia = mediaRepository.saveAndFlush(newMedia);

        // Clear the Hibernate cache so we test the DB interaction
        // Instead of testing the Hibernate cache which contains the object
        entityManager.clear();

        // Assert
        assertNotNull(resultMedia);
        assertNotNull(resultMedia.getId());
        newMedia.setId(resultMedia.getId());
        assertEquals(newMedia, resultMedia);
    }

    @Test
    public void test_find_media_by_id() {
        // Arrange
        Media newMedia = this.testMedia;

        Media savedMedia = mediaRepository.saveAndFlush(newMedia);

        entityManager.clear();

        // Act
        Media resultMedia = mediaRepository.findById(savedMedia.getId()).orElse(null);

        // Assert
        assertNotNull(resultMedia);
        assertEquals(savedMedia.getId(), resultMedia.getId());
    }

    @Test
    public void test_find_media_by_id_with_invalid_id(){
        // Arrange
        Long invalidId = 999L; // Assuming this ID does not exist in the database
        
        // Act
        Media resultMedia = mediaRepository.findById(invalidId).orElse(null);

        // Assert
        assertNull(resultMedia);
    }

    @Test
    public void test_get_all_medias(){
        // Arrange
        Media newMedia1 = this.testMedia;
        Media newMedia2 = this.testMedia2;

        mediaRepository.saveAndFlush(newMedia1);
        mediaRepository.saveAndFlush(newMedia2);

        entityManager.clear();

        // Act
        List<Media> resultMedias = mediaRepository.findAll();

        // Assert
        assertNotNull(resultMedias);
        assertEquals(2, resultMedias.size());
        // Trop trop chiant
        // assertThat(resultAdresses).containsExactlyInAnyOrderElementsOf(List.of(newAdress1, newAdress2));
    }

    @Test
    public void test_update_media(){
        // Arrange
        Media newMedia = this.testMedia;

        Media savedMedia = mediaRepository.saveAndFlush(newMedia);
        entityManager.clear();
        
        // Act
        savedMedia.setMediaName("Updated Media");
        Media updatedMedia = mediaRepository.saveAndFlush(savedMedia);
        entityManager.clear();

        // Assert
        assertNotNull(updatedMedia);
        assertEquals("Updated Media", updatedMedia.getMediaName());
    }

    @Test
    public void test_delete_media_by_id() {
        // Arrange
        Media newMedia = this.testMedia;

        Media resultMedia = mediaRepository.saveAndFlush(newMedia);

        entityManager.clear();

        // Act
        // Remove associations to avoid foreign key constraint issues
        resultMedia = mediaRepository.findById(resultMedia.getId()).orElse(null);
        if (resultMedia != null){
            resultMedia.setSpot(null);
            resultMedia.setStation(null);
            resultMedia.setUser(null);
            mediaRepository.saveAndFlush(resultMedia);
            entityManager.clear();
        }
        // Now delete the media
        mediaRepository.deleteById(resultMedia.getId());
        mediaRepository.flush();
        entityManager.clear();
        // Assert
        assertThat(mediaRepository.findById(resultMedia.getId())).isEmpty();
        assertEquals(0, mediaRepository.count());
    }

    @Test
    public void test_delete_media_by_id_with_invalid_id() {
        // Arrange
        Long invalidId = 999L; // Assuming this ID does not exist in the database
        
        // Act
        mediaRepository.deleteById(invalidId);

        entityManager.clear();

        // Assert
        assertThat(mediaRepository.findById(invalidId)).isEmpty();
        assertEquals(0, mediaRepository.count());
    }

}