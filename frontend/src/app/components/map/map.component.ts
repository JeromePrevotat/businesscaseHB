import { AfterViewInit, Component } from '@angular/core';
import { LeafletService } from '../../services/leaflet.service';
import { getLocation } from '../../utils/mapUtils';
import { SsrService } from '../../services/ssr.service';
import { addCircleToMap } from '../../utils/mapUtils';

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

  constructor(
    private leafletService: LeafletService,
    private ssrService: SsrService) { 
    this.map = null;
    this.L = null;
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

      this.map = this.L.map('map').setView([coords.latitude, coords.longitude], 13);
      await addCircleToMap(this.L, this.map, coords.latitude, coords.longitude);

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
