package com.humanbooster.businesscase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.AllArgsConstructor;

/**
 * Spot's DTO to prevent circular references
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotDTO {
        private Long id;
        private String instruction;
        private List<Long> stationList;
        private Long address_id;
        private List<Long> mediaList;
}
