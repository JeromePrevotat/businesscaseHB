package com.humanbooster.buisinessCase.dto;


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
}
