import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MapService {
  private coords = new BehaviorSubject<{ latitude: number; longitude: number } | null>(null);
  coords$ = this.coords.asObservable();

  updateCoords(coords: { latitude: number; longitude: number }) {
    this.coords.next(coords);
  }

  setMapLocation(map: any, latitude: number, longitude: number) {
    if (latitude && longitude) {
        map.setView([latitude, longitude], 14);
    } else {
        console.error("Invalid latitude or longitude provided.");
    }
  }

  async addCircleToMap(L: any, map: any, latitude: number, longitude: number, radius = 5000) {
    const c = L.circle([latitude, longitude], {
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.1,
        radius: radius
    }).addTo(map);
  }

  async addMarkersToMap(L: any, map: any, latitude: number, longitude: number) {
    L.marker([latitude, longitude]).addTo(map);
    console.log(`Marker added at Latitude: ${latitude}, Longitude: ${longitude}`);
  }
}