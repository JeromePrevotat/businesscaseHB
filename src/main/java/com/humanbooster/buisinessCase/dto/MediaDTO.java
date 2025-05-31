package com.humanbooster.buisinessCase.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Medias's DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    private Long id;

    @NotBlank(message="Media name cannot be blank")
    private String mediaName;

    @NotBlank(message="Type cannot be blank")
    private String type;

    @NotBlank(message="URL cannot be blank")
    private String url;

    private String description;
    private LocalDate creationDate;
}
