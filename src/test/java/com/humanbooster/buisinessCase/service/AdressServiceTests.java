package com.humanbooster.buisinessCase.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.humanbooster.buisinessCase.model.Adress;
import com.humanbooster.buisinessCase.repository.AdressRepository;

@ExtendWith(MockitoExtension.class)
public class AdressServiceTests {
    @Mock
    private AdressRepository adressRepositoryMock;
    @InjectMocks   
    private AdressService adressService;

    @Test
    void test_save_adress_service(){
        // Arrange
        Adress newAdress = new Adress();
        when(adressRepositoryMock.save(newAdress)).thenReturn(newAdress);
        // Act
        adressService.saveAdress(newAdress);
        // Assert
        verify(adressRepositoryMock).save(newAdress);
    }
}
