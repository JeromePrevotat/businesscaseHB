package com.humanbooster.buisinessCase.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing Adress entities.
 * Provides endpoints for creating, retrieving, updating, and deleting adresses.
 */
@RestController
@RequestMapping("/api/healthy")
@RequiredArgsConstructor
public class HealthyController {
    /**
     * Get a healthcheck.
     * GET /api/healthy
     * @return ResponseEntity with the health status
     */
    @GetMapping
    public ResponseEntity<Boolean> getHealthCheck(){
        return ResponseEntity.ok(true);
    }
}


