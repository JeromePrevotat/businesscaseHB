package com.humanbooster.buisinessCase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.Vehicule;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Integer>{

}
