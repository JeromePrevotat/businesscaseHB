import { AfterViewInit, Component } from '@angular/core';
import { LeafletService } from '../../services/leaflet.service';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [],
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})
export class MapComponent implements AfterViewInit{

  constructor(private leafletService: LeafletService) { }

  async ngAfterViewInit() {
    try {
      const L = await this.leafletService.load();
      const map = L.map('map').setView([48.8566, 2.3522], 13);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; OpenStreetMap'
      }).addTo(map);
    } catch (error) {
      console.error('Error loading Leaflet or initializing the map:', error);
    }
  }
}
