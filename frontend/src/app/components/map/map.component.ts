import { AfterViewInit, Component } from '@angular/core';
import { LeafletService } from '../../services/leaflet.service';
import { getLocation } from '../../utils/mapUtils';
import { SsrService } from '../../services/ssr.service';
import { MapService } from '../../services/map.service';
import { distinctUntilChanged } from 'rxjs/internal/operators/distinctUntilChanged';
import { Station } from '../../models/station';
import { inject } from '@angular/core';
import { StationService } from '../../services/station.service';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [],
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit{
  private map: any;
  private L: any;
  private currentCoords: any;
  private currentCircle: any;
  private currentMarkers: any[];
  private currentStations: Station[];
  private mapService = inject(MapService);
  private leafletService = inject(LeafletService);
  private ssrService = inject(SsrService);
  private stationService = inject(StationService);

  constructor() { 
    this.map = null;
    this.L = null;
    this.currentCoords = null;
    this.currentCircle = null;
    this.currentMarkers = [];
    this.currentStations = [];
    // Subscribe to coordinates to recenter on change
    this.mapService.coords$.pipe(
      distinctUntilChanged((prev, curr) => prev?.latitude === curr?.latitude && prev?.longitude === curr?.longitude))
      .subscribe(async coords => {
        if (coords && this.map && this.L) {
            this.currentCoords = coords;
            this.mapService.setMapLocation(this.map, coords.latitude, coords.longitude);
        }
      });
    // Subscribe to radius changes to update circle
    this.mapService.radius$.subscribe(async radius => {
      if (this.map && this.L && this.currentCoords) {
        if (this.currentCircle) this.map.removeLayer(this.currentCircle);
        this.currentCircle = await this.mapService.addCircleToMap(this.L, this.map, this.currentCoords.latitude, this.currentCoords.longitude, radius ?? 5000);
      }
    });
    // Subscribe to stations changes to update markers
    this.stationService.filteredStations$.subscribe(async stations => {
      this.currentStations = stations || [];
      if (this.map && this.L && this.currentMarkers) {
        // Remove existing markers
        for (const marker of this.currentMarkers) {
          this.map.removeLayer(marker);
        }
        this.currentMarkers = [];
        for (const station of stations || []) {
          if (station.latitude && station.longitude) {            
            const marker = this.mapService.addMarkersToMap(this.L, this.map, station.latitude, station.longitude);
            this.currentMarkers.push(marker);
          }
        }
      }
    });
  }

  async ngAfterViewInit() {
    try {
      let coords;
      if (this.ssrService.isClientSide) {
        coords = await getLocation();
      }
      this.L = await this.leafletService.load();

      if (!this.L) {
        console.error("Leaflet library could not be loaded.");
        return;
      }
      if (!coords) {
        console.error("Could not get user location.");
        return;
      }
      // this.currentCoords = coords;
      this.currentCoords = {latitude: 45.7774551, longitude: 3.0819427};
      this.mapService.updateCoords(coords);
      this.map = this.L.map('map');
      this.mapService.setMapLocation(this.map, this.currentCoords.latitude, this.currentCoords.longitude);
      if (this.currentCircle) {
        this.map.removeLayer(this.currentCircle);
        this.currentCircle = null;
      }
      this.currentCircle = await this.mapService.addCircleToMap(this.L, this.map, this.currentCoords.latitude, this.currentCoords.longitude, 5000);

      if (this.map && this.L) {
        if (this.currentMarkers) {
          // Remove existing markers
          for (const marker of this.currentMarkers) {
            this.map.removeLayer(marker);
          }
          this.currentMarkers = [];
        }
        if (this.currentCoords) {
          // Call station service to search stations
          this.stationService.searchStation(
            5000,
            this.currentCoords ? this.currentCoords.latitude : 0,
            this.currentCoords ? this.currentCoords.longitude : 0,
            20
          ).subscribe({
            next: (stations) => {
              this.stationService.updateFilteredStations(stations);
              this.currentStations = stations;
            },
            error: (err) => {
              console.error('Error during station search:', err);
            }
          });
          for (const station of this.currentStations || []) {
            if (station.latitude && station.longitude) {            
              const marker = this.mapService.addMarkersToMap(this.L, this.map, station.latitude, station.longitude);
              this.currentMarkers.push(marker);
            }
          }
        }
      }

      this.L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; OpenStreetMap'
      }).addTo(this.map);
    } catch (error) {
      console.error('Error loading Leaflet or initializing the map:', error);
    }
  }

  ngOnDestroy() {
    if (this.map) {
      this.map.remove();
      this.map = null;
    }
  }

  getMapInstance() { return { L: this.L, map: this.map }; }
}
