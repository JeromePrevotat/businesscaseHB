import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Station } from '../models/station';
import { API_URL } from '../utils/apiUrl';

@Injectable({
  providedIn: 'root'
})
export class StationService {
  private http = inject(HttpClient);
  public stationList: BehaviorSubject<Station[]> = new BehaviorSubject<Station[]>([]);

  constructor() {
    this.refreshStationList();
  }

  getStationList(): Observable<Station[]> {
    return this.stationList.asObservable();
  }

  refreshStationList(): void {
    this.getAllStations().subscribe({
      next: (stations) => {
        this.stationList.next(stations)
      },
      error: (err) => console.error('Erreur refresh stations', err)
    });
    console.log("REFRESHED STATION LIST", this.stationList.value);
  }

  getAllStations(): Observable<Station[]> {
    return this.http.get<Station[]>(API_URL.STATIONS);
  }

  getStation(id: number): Observable<Station> {
    return this.http.get<Station>(`${API_URL.STATIONS}/${id}`);
  }

  createStation(station: Partial<Station>): Observable<Station> {
    const newStation = this.http.post<Station>(API_URL.STATIONS, station)
    // Update the station list after creation
    .pipe(
      tap(() => this.refreshStationList())
    );
    return newStation;
  }

  editStation(id: number, station: Partial<Station>): Observable<Station> {
    return this.http.put<Station>(`${API_URL.STATIONS}/${id}`, station);
  }

  deleteStation(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL.STATIONS}/${id}`);
  }
}