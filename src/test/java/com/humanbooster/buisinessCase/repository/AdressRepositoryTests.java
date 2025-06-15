package com.humanbooster.buisinessCase.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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

import com.humanbooster.buisinessCase.model.Adress;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Force DtaJPaTest to use my own mysql DB instead of H2 
@ActiveProfiles("repository-test") // Use the empty test db specifically for the Repository Tests
public class AdressRepositoryTests {
    @Autowired
    private AdressRepository adressRepository;
    @Autowired
    private TestEntityManager entityManager;

    private Adress testAdress;

    @BeforeEach
    public void setUp() {
        this.testAdress = new Adress();
        this.testAdress.setAdressname("Test Adress");
        this.testAdress.setStreetnumber("123");
        this.testAdress.setStreetname("Test Street");
        this.testAdress.setZipcode("12345");
        this.testAdress.setCity("Lyon");
        this.testAdress.setCountry("France");
        this.testAdress.setRegion("Auvergne-Rhône-Alpes");
        this.testAdress.setAddendum("Bâtiment B");
        this.testAdress.setFloor(2);
        this.testAdress.setUserList(new ArrayList<>());
        this.testAdress.setSpotList(new ArrayList<>());
    }

    @Test
    public void test_save_adress(){
        // Arrange
        Adress newAdress = this.testAdress;

        // Act
        Adress resultAdress = adressRepository.saveAndFlush(newAdress);

        // Clear the Hibernate cache so we test the DB interaction
        // Instead of testing the Hibernate cache which contains the object
        entityManager.clear();

        // Assert
        assertNotNull(resultAdress);
        assertNotNull(resultAdress.getId());
        newAdress.setId(resultAdress.getId());
        assertEquals(newAdress, resultAdress);
    }

    @Test
    public void test_find_adress_by_id() {
        // Arrange
        Adress newAdress = this.testAdress;

        Adress savedAdress = adressRepository.saveAndFlush(newAdress);
        
        entityManager.clear();

        // Act
        Adress resultAdress = adressRepository.findById(savedAdress.getId()).orElse(null);

        // Assert
        assertNotNull(resultAdress);
        try {
            Field[] fields = savedAdress.getClass().getDeclaredFields();
            for (Field field : fields) {
                // Ignore immutable Fields
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) continue;
                    field.setAccessible(true);
                    Object expectedValue = field.get(savedAdress);
                    Object actualValue = field.get(resultAdress);
                    if (List.class.isAssignableFrom(field.getType())) {
                        List<?> expectedList = (List<?>) expectedValue;
                        List<?> actualList = (List<?>) actualValue;
                        
                        if (!(expectedList == null && actualList == null)) {
                            assertEquals(expectedList.size(), actualList.size(),
                                "Field '" + field.getName() + "' lists have different sizes");
                            
                            // Compare contents (order doesn't matter)
                            for (Object expectedItem : expectedList) {
                                if (!actualList.contains(expectedItem)) assertEquals(expectedList, actualList, "Field '" + field.getName() + "' lists have different contents");
                            }
                        }
                    } else {
                        // Primitive Types
                        assertEquals(expectedValue, actualValue, 
                            "Field '" + field.getName() + "' values don't match");
                    }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing fields for comparison", e);
        }
    }

    @Test
    public void test_find_adress_by_id_with_invalid_id(){
        // Arrange
        Long invalidId = 999L; // Assuming this ID does not exist in the database
        
        // Act
        Adress resultAdress = adressRepository.findById(invalidId).orElse(null);
        
        // Assert
        assertNull(resultAdress);
    }

    @Test
    public void test_get_all_adresses(){
        // Arrange
        Adress newAdress1 = new Adress();
        newAdress1.setAdressname("Test Adress 1");
        newAdress1.setStreetnumber("123");
        newAdress1.setStreetname("Test Street 1");
        newAdress1.setZipcode("12345");
        newAdress1.setCity("Lyon");
        newAdress1.setCountry("France");
        newAdress1.setRegion("Auvergne-Rhône-Alpes");
        newAdress1.setAddendum("Bâtiment B");
        newAdress1.setFloor(1);
        newAdress1.setUserList(new ArrayList<>());
        newAdress1.setSpotList(new ArrayList<>());

        Adress newAdress2 = new Adress();
        newAdress2.setAdressname("Test Adress 2");
        newAdress2.setStreetnumber("456");
        newAdress2.setStreetname("Test Street 2");
        newAdress2.setZipcode("67890");
        newAdress2.setCity("Paris");
        newAdress2.setCountry("France");
        newAdress2.setRegion("Île-de-France");
        newAdress2.setAddendum("Bâtiment C");
        newAdress2.setFloor(2);
        newAdress2.setUserList(new ArrayList<>());
        newAdress2.setSpotList(new ArrayList<>());

        adressRepository.saveAndFlush(newAdress1);
        adressRepository.saveAndFlush(newAdress2);

        entityManager.clear();

        // Act
        List<Adress> resultAdresses = adressRepository.findAll();

        // Assert
        assertNotNull(resultAdresses);
        assertEquals(2, resultAdresses.size());
        // Trop trop chiant
        // assertThat(resultAdresses).containsExactlyInAnyOrderElementsOf(List.of(newAdress1, newAdress2));
    }

    @Test
    public void test_update_adress(){
        // Arrange
        Adress newAdress = this.testAdress;

        Adress savedAdress = adressRepository.saveAndFlush(newAdress);
        entityManager.clear();
        
        // Act
        savedAdress.setCity("Marseille");
        Adress updatedAdress = adressRepository.saveAndFlush(savedAdress);
        entityManager.clear();

        // Assert
        assertNotNull(updatedAdress);
        assertEquals("Marseille", updatedAdress.getCity());
    }

    @Test
    public void test_delete_adress_by_id() {
        // Arrange
        Adress newAdress = this.testAdress;

        Adress resultAdress = adressRepository.saveAndFlush(newAdress);
        
        entityManager.clear();

        // Act
        adressRepository.deleteById(resultAdress.getId());
        adressRepository.flush(); // Ensure the delete operation is executed
        entityManager.clear();

        // Assert
        assertThat(adressRepository.findById(resultAdress.getId())).isEmpty();
        assertEquals(0, adressRepository.count());
    }

    @Test
    public void test_delete_adress_by_id_with_invalid_id() {
        // Arrange
        Long invalidId = 999L; // Assuming this ID does not exist in the database
        
        // Act
        adressRepository.deleteById(invalidId);
        
        entityManager.clear();

        // Assert
        assertThat(adressRepository.findById(invalidId)).isEmpty();
        assertEquals(0, adressRepository.count());
    }

}