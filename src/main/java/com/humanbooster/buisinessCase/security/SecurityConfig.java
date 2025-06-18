package com.humanbooster.buisinessCase.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.humanbooster.buisinessCase.model.UserRole;
import com.humanbooster.buisinessCase.repository.UserRepository;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
            .authorizeHttpRequests(authz -> authz
                // Sping Boot Actuator monitoring endpoints
                .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                .requestMatchers("/actuator/info").permitAll()
                // Monitoring via Spring Boot Actuator
                .requestMatchers("/actuator/**").hasRole(UserRole.ADMIN.name())
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults());
            return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
