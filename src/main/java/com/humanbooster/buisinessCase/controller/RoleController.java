package com.humanbooster.buisinessCase.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humanbooster.buisinessCase.dto.RoleDTO;
import com.humanbooster.buisinessCase.mapper.RoleMapper;
import com.humanbooster.buisinessCase.model.Role;
import com.humanbooster.buisinessCase.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing Role entities.
 * Provides endpoints for creating, retrieving, updating, and deleting roles.
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    private final RoleMapper mapper;


    /**
     * Get all roles.
     * GET /api/roles
     * @return ResponseEntity with the list of roles
     */
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles(){
        List<RoleDTO> roleDTOs = roleService.getAllRoles().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roleDTOs);
    }

    /**
     * Get a role by ID.
     * GET /api/roles/{id}
     * @param id The ID of the role to retrieve
     * @return ResponseEntity with the role if found, or 404 Not Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id){
        return roleService.getRoleById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save a new role.
     * POST /api/roles
     * @param role The role entity to be saved
     * @return ResponseEntity with the saved role and 201 Created status
     */
    @PostMapping
    public ResponseEntity<RoleDTO> saveRole(@Valid @RequestBody RoleDTO roleDTO){
        Role newRole = mapper.toEntity(roleDTO);
        Role savedRole = roleService.saveRole(newRole);
        RoleDTO savedRoleDTO = mapper.toDTO(savedRole);
        // Conform RESTful practices, we should return a URI to the created resource.
        // URI location = URI.create("/api/roles/" + savedRole.getId());
        // return ResponseEntity.created(location).body(savedRoleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoleDTO);
    }

    /**
     * Delete a role by ID.
     * DELETE /api/roles/{id}
     * @param id The ID of the role to delete
     * @return ResponseEntity with the 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleById(@PathVariable Long id){
        return roleService.deleteRoleById(id).isPresent() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Update a role by ID.
     * PUT /api/roles/{id}
     * @param id The ID of the role to update
     * @param newRole The updated role entity
     * @return ResponseEntity with the updated role if found, or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO newRoleDTO){
        return roleService.updateRole(id, mapper.toEntity(newRoleDTO))
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

