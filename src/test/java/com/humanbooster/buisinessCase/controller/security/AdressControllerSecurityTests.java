package com.humanbooster.buisinessCase.controller.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanbooster.buisinessCase.mapper.AdressMapper;
import com.humanbooster.buisinessCase.service.AdressService;
import com.humanbooster.buisinessCase.service.JwtService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
public class AdressControllerSecurityTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdressService adressService;
    @MockitoBean
    private JwtService jwtService;
    @Autowired
    private AdressMapper adressMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username="admin", roles = {"ADMIN"})
    public void test_get_all_adress() throws Exception {
        try {
            mockMvc.perform(get("/api/adresses"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
