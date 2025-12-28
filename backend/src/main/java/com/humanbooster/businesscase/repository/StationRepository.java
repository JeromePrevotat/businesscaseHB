package com.humanbooster.businesscase.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = """
    SELECT *
    FROM stations s
    WHERE (:priceRate IS NULL OR s.price_rate <= :priceRate)
    AND (
        (:startTime IS NULL AND :endTime IS NULL)
        OR NOT EXISTS (
            SELECT 1
            FROM reservations r
            WHERE r.station = s.id
            AND (
                (:startTime IS NOT NULL AND :endTime IS NOT NULL
                AND TIME(r.start_date) < :endTime
                AND TIME(r.end_date) > :startTime)
                OR
                (:searchStart IS NOT NULL AND :searchEnd IS NOT NULL
                AND r.start_date < :searchEnd
                AND r.end_date > :searchStart)
            )
        )
    )
    """, nativeQuery = true)
    List<Station> searchStations(
        @Param("priceRate") BigDecimal priceRate,
        @Param("searchStart") LocalDateTime searchStart,
        @Param("searchEnd") LocalDateTime searchEnd,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime
    );
   
}
