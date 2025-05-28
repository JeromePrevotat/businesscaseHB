package com.humanbooster.buisinessCase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.Utilisateur;

@Repository
public interface AdresseRepository extends JpaRepository<Utilisateur, Integer>{
    
}
