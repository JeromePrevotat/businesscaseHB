package com.humanbooster.buisinessCase.controller;

import com.humanbooster.buisinessCase.model.HourlyRate;
import com.humanbooster.buisinessCase.service.HourlyRateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing hourly rates.
 * Provides endpoints for CRUD operations on hourly rates.
 */
@RestController
@RequestMapping("/api/hourly-rates")
@RequiredArgsConstructor
public class HourlyRateController {

    private final HourlyRateService hourlyRateService;

    /**
     * Get all hourly rates.
     * GET /api/hourly-rates
     */
    @GetMapping
    public ResponseEntity<List<HourlyRate>> getAllHourlyRates() {
        List<HourlyRate> hourlyRates = hourlyRateService.findAll();
        return ResponseEntity.ok(hourlyRates);
    }

    /**
     * Get an hourly rate by its ID.
     * GET /api/hourly-rates/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<HourlyRate> getHourlyRateById(@PathVariable Long id) {
        return hourlyRateService.findById(id)
                .map(hourlyRate -> ResponseEntity.ok(hourlyRate))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new hourly rate.
     * POST /api/hourly-rates
     */
    @PostMapping
    public ResponseEntity<HourlyRate> createHourlyRate(@Valid @RequestBody HourlyRate hourlyRate) {
        HourlyRate savedHourlyRate = hourlyRateService.save(hourlyRate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHourlyRate);
    }

    /**
     * Update an existing hourly rate.
     * PUT /api/hourly-rates/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<HourlyRate> updateHourlyRate(@PathVariable Long id, @Valid @RequestBody HourlyRate hourlyRate) {
        if (!hourlyRateService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        HourlyRate updatedHourlyRate = hourlyRateService.update(id, hourlyRate);
        return ResponseEntity.ok(updatedHourlyRate);
    }

    /**
     * Delete an existing hourly rate.
     * DELETE /api/hourly-rates/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHourlyRate(@PathVariable Long id) {
        if (!hourlyRateService.existsById(id)) return ResponseEntity.notFound().build();
        hourlyRateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 