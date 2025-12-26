import { Injectable } from '@angular/core';
import { SsrService } from './ssr.service';

@Injectable({
  providedIn: 'root'
})
export class LeafletService {
  private leaflet: any | null = null;

  constructor(private ssr: SsrService) {}

  async load(): Promise<any | null> {
    // SSR dont try to load leaflet to avoid error : "window is not defined"
    if (!this.ssr.isClientSide) {
      return null;
    }

    // Client side: dynamically import Leaflet
    if (!this.leaflet) {
      this.leaflet = await import('leaflet');
    }

    return this.leaflet;
  }
}