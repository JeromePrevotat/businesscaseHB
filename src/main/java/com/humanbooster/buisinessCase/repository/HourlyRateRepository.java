package com.humanbooster.buisinessCase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.HourlyRate;
import com.humanbooster.buisinessCase.model.Station;

import java.util.List;

/**
 * Hourlyrate's Repository
 * Provides CRUD operations and search methods by Station.
 */
@Repository
public interface HourlyRateRepository extends JpaRepository<HourlyRate, Long> {

    List<HourlyRate> findByStation(Station station);

    List<HourlyRate> findByStationAndActive(Station station, Boolean active);
}