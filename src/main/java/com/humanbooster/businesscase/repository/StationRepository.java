package com.humanbooster.businesscase.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.businesscase.model.PlugType;
import com.humanbooster.businesscase.model.Spot;
import com.humanbooster.businesscase.model.Station;
import com.humanbooster.businesscase.model.StationState;

/**
 * Station's Repository. Provides basic CRUD operations for Station entities.
 */
@Repository
public interface StationRepository extends JpaRepository<Station, Long>{
    List<Station> findByPriceRate(BigDecimal priceRate);

    List<Station> findByPlugType(PlugType plugType);

    List<Station> findBySpot(Spot spot);
    
    List<Station> findByState(StationState state);
    
    List<Station> findByBusy(Boolean busy);
   
}
