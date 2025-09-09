package com.humanbooster.businesscase.dto;


import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Station's DTO
 * Prevents circular references
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlugTypeDTO {
    private Long id;

    @NotBlank(message="Plug type cannot be blank")
    private String plugname;
    private List<Long> vehicule_id;
    private List<Long> station_id;
}
