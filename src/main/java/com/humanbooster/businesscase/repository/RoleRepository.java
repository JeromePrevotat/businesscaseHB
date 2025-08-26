package com.humanbooster.businesscase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.humanbooster.businesscase.model.Role;
import com.humanbooster.businesscase.model.UserRole;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(UserRole name);
}
