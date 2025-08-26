package com.humanbooster.businesscase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humanbooster.businesscase.model.Role;
import com.humanbooster.businesscase.repository.RoleRepository;
import com.humanbooster.businesscase.utils.ModelUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing Roles.
 * Provides methods to save, retrieve, update, and delete Roles.
 */

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService{
    private final RoleRepository roleRepository;

    /**
     * Saves a new Role.
     * @param role the Role to save
     * @return the newly saved Role
     */
    @Transactional
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    /**
     * Retrieves all Roles.
     * @return a list of all Roles
     */
    @Transactional(readOnly = true)
    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    /**
     * Retrieves a Role by its ID.
     * @param id the ID of the Role to retrieve
     * @return  an Optional containing the Role if found, or empty if not found
     */
    @Transactional(readOnly = true)
    public Optional<Role> getRoleById(Long id){
        return roleRepository.findById(id);
    }

    /**
     * Deletes a Role by its ID.
     * @param id the ID of the Role to delete
     * @return an Optional containing the deleted Role if found, or empty if not found
     */
    @Transactional
    public Optional<Role> deleteRoleById(Long id){
        Optional<Role> roleOpt = roleRepository.findById(id);
        roleOpt.ifPresent(roleRepository::delete);
        return roleOpt;
    }

    /**
     * Updates an existing Role.
     * @param id the ID of the Role to update
     * @param newRole the new Role data to update
     * @return an Optional containing the updated Role if found, or empty if not found
     */
    @Transactional
    public Optional<Role> updateRole(Long id, Role newRole){
        return roleRepository.findById(id)
                .map(existingRole -> {
                    ModelUtil.copyFields(newRole, existingRole);
                    return roleRepository.save(existingRole);
                });
    }

    /**
     * Checks if a Role exists by its ID.
     * @param id the ID of the Role to check
     * @return true if the Role exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }
}
