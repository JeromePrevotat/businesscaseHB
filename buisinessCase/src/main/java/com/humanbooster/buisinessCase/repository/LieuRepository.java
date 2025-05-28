package com.humanbooster.buisinessCase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humanbooster.buisinessCase.model.Lieu;

@Repository
public interface LieuRepository extends JpaRepository<Lieu, Integer>{

}
