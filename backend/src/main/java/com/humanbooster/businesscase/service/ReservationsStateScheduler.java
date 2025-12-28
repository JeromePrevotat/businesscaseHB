package com.humanbooster.businesscase.service;

import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.humanbooster.businesscase.model.ReservationState;
import com.humanbooster.businesscase.repository.ReservationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationsStateScheduler {
  private final ReservationRepository reservationRepository;

  @Scheduled(fixedRate = 60000) // toutes les 60 secondes
  @Transactional
  public void updateExpiredReservations() {
    reservationRepository.markExpiredReservations(
      ReservationState.PENDING,
      ReservationState.PAST,
      LocalDateTime.now()
    );
  }
}
