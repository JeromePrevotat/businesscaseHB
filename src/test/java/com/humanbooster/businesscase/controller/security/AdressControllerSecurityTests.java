package com.humanbooster.businesscase.controller.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanbooster.businesscase.controller.AdressController;
import com.humanbooster.businesscase.mapper.AdressMapper;
import com.humanbooster.businesscase.mapper.StationMapper;
import com.humanbooster.businesscase.mapper.UserMapper;
import com.humanbooster.businesscase.repository.UserRepository;
import com.humanbooster.businesscase.security.JpaUserDetailsService;
import com.humanbooster.businesscase.security.JwtAuthFilter;
import com.humanbooster.businesscase.security.SecurityConfig;
import com.humanbooster.businesscase.service.AdressService;
import com.humanbooster.businesscase.service.RefreshTokenService;
import com.humanbooster.businesscase.service.StationService;
import com.humanbooster.businesscase.service.UserService;

@WebMvcTest(controllers=AdressController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class AdressControllerSecurityTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdressService adressService;
    @MockitoBean
    private RefreshTokenService refreshTokenService;
    @MockitoBean
    private AdressMapper adressMapper;
    @MockitoBean
    JwtAuthFilter jwtAuthFilter;
    @MockitoBean
    private JpaUserDetailsService jwtUserDetailsService;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private StationService stationService;
    @MockitoBean
    private UserMapper userMapper;
    @MockitoBean
    private StationMapper stationMapper;

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
