package com.humanbooster.buisinessCase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.PlugType;

@Repository
public interface PlugTypeRepository extends JpaRepository<PlugType, Long>{

}
