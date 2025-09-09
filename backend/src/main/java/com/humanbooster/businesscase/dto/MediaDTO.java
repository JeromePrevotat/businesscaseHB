package com.humanbooster.businesscase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Media's DTO
 * Prevents circular references
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    private Long id;

    @NotBlank(message="URL cannot be blank")
    private String url;

    @NotBlank(message="Type cannot be blank")
    private String type;

    @NotBlank(message="Media name cannot be blank")
    private String mediaName;

    private Long size;
    private Long user_id;
    private Long spot_id;
    private Long station_id;
}
