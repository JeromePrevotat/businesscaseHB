package com.humanbooster.buisinessCase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humanbooster.buisinessCase.model.Reservation;
import com.humanbooster.buisinessCase.repository.ReservationRepository;
import com.humanbooster.buisinessCase.utils.ModelUtil;

import jakarta.transaction.Transactional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public void saveReservation(Reservation reservation){
        reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservation(){
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(long id){
        return reservationRepository.findById(id);
    }

    @Transactional
    public Optional<Reservation> deleteReservationById(long id){
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        reservationOpt.ifPresent(reservationRepository::delete);
        return reservationOpt;
    }

    @Transactional
    public Optional<Reservation> updateReservation(Reservation newReservation, long id){
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    ModelUtil.copyFields(newReservation, existingReservation);
                    return reservationRepository.save(existingReservation);
                });
    }

}
