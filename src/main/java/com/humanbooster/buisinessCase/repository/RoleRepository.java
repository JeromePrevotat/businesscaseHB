package com.humanbooster.buisinessCase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.humanbooster.buisinessCase.model.Role;
import com.humanbooster.buisinessCase.model.UserRole;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(UserRole name);
}
