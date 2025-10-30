import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Reservation } from '../models/reservation';
import { API_URL } from '../utils/apiUrl';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private http = inject(HttpClient);

  getAllReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(API_URL.RESERVATIONS);
  }

  getReservation(id: number): Observable<Reservation> {
    return this.http.get<Reservation>(`${API_URL.RESERVATIONS}/${id}`);
  }

  createReservation(Reservation: Partial<Reservation>): Observable<Reservation> {
    const newReservation = this.http.post<Reservation>(API_URL.RESERVATIONS, Reservation);
    return newReservation;
  }

  editReservation(id: number, Reservation: Partial<Reservation>): Observable<Reservation> {
    return this.http.put<Reservation>(`${API_URL.RESERVATIONS}/${id}`, Reservation);
  }

  deleteReservation(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL.RESERVATIONS}/${id}`);
  }
}