package com.humanbooster.buisinessCase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
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
}
