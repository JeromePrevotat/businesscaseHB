package com.humanbooster.businesscase.repository;

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

import com.humanbooster.businesscase.model.PlugType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Force DtaJPaTest to use my own mysql DB instead of H2 
@ActiveProfiles("repository-test") // Use the empty test db specifically for the Repository Tests
public class PlugTypeRepositoryTests {
    @Autowired
    private PlugTypeRepository plugTypeRepository;
    @Autowired
    private TestEntityManager entityManager;

    private PlugType testPlugType;
    private PlugType testPlugType2;

    @BeforeEach
    public void setUp() {
        this.testPlugType = new PlugType();
        testPlugType.setPlugname("Type S");
        testPlugType.setVehiculeList(new HashSet<>());
        testPlugType.setStationList(new HashSet<>());

        this.testPlugType2 = new PlugType();
        testPlugType2.setPlugname("Type M");
        testPlugType2.setVehiculeList(new HashSet<>());
        testPlugType2.setStationList(new HashSet<>());
    }

    @Test
    public void test_save_plug_type(){
        // Arrange
        PlugType newPlugType = this.testPlugType;

        // Act
        PlugType resultPlugType = plugTypeRepository.saveAndFlush(newPlugType);

        // Clear the Hibernate cache so we test the DB interaction
        // Instead of testing the Hibernate cache which contains the object
        entityManager.clear();

        // Assert
        assertNotNull(resultPlugType);
        assertNotNull(resultPlugType.getId());
        newPlugType.setId(resultPlugType.getId());
        assertEquals(newPlugType, resultPlugType);
    }

    @Test
    public void test_find_plug_type_by_id() {
        // Arrange
        PlugType newPlugType = this.testPlugType;

        PlugType savedPlugType = plugTypeRepository.saveAndFlush(newPlugType);

        entityManager.clear();

        // Act
        PlugType resultPlugType = plugTypeRepository.findById(savedPlugType.getId()).orElse(null);

        // Assert
        assertNotNull(resultPlugType);
        assertEquals(savedPlugType.getId(), resultPlugType.getId());
    }

    @Test
    public void test_find_plug_type_by_id_with_invalid_id(){
        // Arrange
        Long invalidId = 999L; // Assuming this ID does not exist in the database
        
        // Act
        PlugType resultPlugType = plugTypeRepository.findById(invalidId).orElse(null);

        // Assert
        assertNull(resultPlugType);
    }

    @Test
    public void test_get_all_plug_types(){
        // Arrange
        PlugType newPlugType1 = this.testPlugType;
        PlugType newPlugType2 = this.testPlugType2;

        plugTypeRepository.saveAndFlush(newPlugType1);
        plugTypeRepository.saveAndFlush(newPlugType2);

        entityManager.clear();

        // Act
        List<PlugType> resultPlugTypes = plugTypeRepository.findAll();

        // Assert
        assertNotNull(resultPlugTypes);
        assertEquals(2, resultPlugTypes.size());
        // Trop trop chiant
        // assertThat(resultAdresses).containsExactlyInAnyOrderElementsOf(List.of(newAdress1, newAdress2));
    }

    @Test
    public void test_update_plug_type(){
        // Arrange
        PlugType newPlugType = this.testPlugType;

        PlugType savedPlugType = plugTypeRepository.saveAndFlush(newPlugType);
        entityManager.clear();
        
        // Act
        savedPlugType.setPlugname("Updated Plug Type");
        PlugType updatedPlugType = plugTypeRepository.saveAndFlush(savedPlugType);
        entityManager.clear();

        // Assert
        assertNotNull(updatedPlugType);
        assertEquals("Updated Plug Type", updatedPlugType.getPlugname());
    }

    @Test
    public void test_delete_plug_type_by_id() {
        // Arrange
        PlugType newPlugType = this.testPlugType;

        PlugType resultPlugType = plugTypeRepository.saveAndFlush(newPlugType);

        entityManager.clear();

        // Act
        plugTypeRepository.deleteById(resultPlugType.getId());
        plugTypeRepository.flush();
        entityManager.clear();
        // Assert
        assertThat(plugTypeRepository.findById(resultPlugType.getId())).isEmpty();
        assertEquals(0, plugTypeRepository.count());
    }

    @Test
    public void test_delete_plug_type_by_id_with_invalid_id() {
        // Arrange
        Long invalidId = 999L; // Assuming this ID does not exist in the database
        
        // Act
        plugTypeRepository.deleteById(invalidId);

        entityManager.clear();

        // Assert
        assertThat(plugTypeRepository.findById(invalidId)).isEmpty();
        assertEquals(0, plugTypeRepository.count());
    }

}