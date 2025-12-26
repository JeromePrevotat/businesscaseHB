import { AfterViewInit, Component } from '@angular/core';
import { LeafletService } from '../../services/leaflet.service';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [],
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit{
  private map: any;

  constructor(private leafletService: LeafletService) { 
    this.map = null;
  }

  async ngAfterViewInit() {
    try {
      const L = await this.leafletService.load();

      if (!L) {
        return;
      }

      this.map = L.map('map').setView([48.8566, 2.3522], 13);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
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
}
