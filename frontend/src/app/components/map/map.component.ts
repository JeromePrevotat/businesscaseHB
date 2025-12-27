import { AfterViewInit, Component } from '@angular/core';
import { LeafletService } from '../../services/leaflet.service';
import { getLocation } from '../../utils/mapUtils';
import { SsrService } from '../../services/ssr.service';
import { MapService } from '../../services/map.service';
import { distinctUntilChanged } from 'rxjs/internal/operators/distinctUntilChanged';

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

  constructor(
    private leafletService: LeafletService,
    private ssrService: SsrService,
    private mapService: MapService
  ) { 
    this.map = null;
    this.L = null;
    this.currentCoords = null;
    this.currentCircle = null;
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
      this.currentCoords = coords;
      this.mapService.updateCoords(coords);
      this.map = this.L.map('map');
      this.mapService.setMapLocation(this.map, this.currentCoords.latitude, this.currentCoords.longitude);

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
