import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Station } from '../models/station';
import { API_URL } from '../utils/apiUrl';

@Injectable({
  providedIn: 'root'
})
export class StationService {
  private http = inject(HttpClient);

  getAllStations(): Observable<Station[]> {
    return this.http.get<Station[]>(API_URL.STATIONS);
  }

  getStation(id: number): Observable<Station> {
    return this.http.get<Station>(`${API_URL.STATIONS}/${id}`);
  }

  createStation(station: Partial<Station>): Observable<Station> {
    return this.http.post<Station>(API_URL.STATIONS, station);
  }

  editStation(id: number, station: Partial<Station>): Observable<Station> {
    return this.http.put<Station>(`${API_URL.STATIONS}/${id}`, station);
  }

  deleteStation(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL.STATIONS}/${id}`);
  }
}