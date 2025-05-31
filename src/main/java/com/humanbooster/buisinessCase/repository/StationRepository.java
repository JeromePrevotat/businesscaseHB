package com.humanbooster.buisinessCase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.Spot;
import com.humanbooster.buisinessCase.model.Station;
import com.humanbooster.buisinessCase.model.StationState;

/**
 * Station's Repository. Provides basic CRUD operations for Station entities.
 */
@Repository
public interface StationRepository extends JpaRepository<Station, Long>{
    List<Station> findBySpot(Spot spot);
    
    List<Station> findByState(StationState state);
    
    List<Station> findByBusy(Boolean busy);
    
    List<Station> findBySpotAndState(Spot spot, StationState state);
}
