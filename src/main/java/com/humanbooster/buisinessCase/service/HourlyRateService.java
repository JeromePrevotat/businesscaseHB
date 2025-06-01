package com.humanbooster.buisinessCase.service;

import com.humanbooster.buisinessCase.model.HourlyRate;
import com.humanbooster.buisinessCase.repository.HourlyRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * HourlyRate's Service
 * Provides business logic for managing hourly rates.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class HourlyRateService {

    private final HourlyRateRepository hourlyRateRepository;

    /**
     * Get all Hourly Rate
     */
    @Transactional(readOnly = true)
    public List<HourlyRate> findAll() {
        return hourlyRateRepository.findAll();
    }

    /**
     * Get an Hourly Rate by its ID.
     */
    @Transactional(readOnly = true)
    public Optional<HourlyRate> findById(Long id) {
        return hourlyRateRepository.findById(id);
    }

    /**
     * Create a new Hourly Rate.
     */
    public HourlyRate save(HourlyRate hourlyRate) {
        return hourlyRateRepository.save(hourlyRate);
    }

    /**
     * Update an existing Hourly Rate.
     */
    public HourlyRate update(Long id, HourlyRate hourlyRate) {
        hourlyRate.setId(id);
        return hourlyRateRepository.save(hourlyRate);
    }

    /**
     * Delete an Hourly Rate.
     */
    public void deleteById(Long id) {
        hourlyRateRepository.deleteById(id);
    }

    /**
     * Check if an Hourly Rate exists.
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return hourlyRateRepository.existsById(id);
    }
}