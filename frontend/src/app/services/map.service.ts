import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MapService {
  private coords = new BehaviorSubject<{ latitude: number; longitude: number } | null>(null);
  coords$ = this.coords.asObservable();

  private radius = new BehaviorSubject<number | null>(null);
  radius$ = this.radius.asObservable();

  updateCoords(coords: { latitude: number; longitude: number }) {
    this.coords.next(coords);
  }

  updateRadius(radius: number) {
    this.radius.next(radius);
  }

  setMapLocation(map: any, latitude: number, longitude: number, zoom: number = 13) {
    if (latitude && longitude) {
        map.setView([latitude, longitude], zoom);
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
    return c;
  }

  async addMarkersToMap(L: any, map: any, latitude: number, longitude: number) {
    L.marker([latitude, longitude]).addTo(map);
    console.log(`Marker added at Latitude: ${latitude}, Longitude: ${longitude}`);
  }
}