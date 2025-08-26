package com.humanbooster.businesscase.dto;

import java.util.List;

import com.humanbooster.businesscase.model.UserRole;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for UserRole entity.
 * Includes a simple reference without circular relations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    @NotNull(message = "Role name cannot be null")
    private UserRole name;

    private List<Long> userList;
}
