import { Component, inject, Input } from '@angular/core';
import { Station } from '../../models/station';
import { SsrService } from '../../services/ssr.service';
import { RouterLink } from "@angular/router";
import { ROUTE_PATHS } from '../../utils/routeMapping';
import { Router } from '@angular/router';

@Component({
  selector: 'app-station-card',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './station-card.component.html',
  styleUrls: ['./station-card.component.css']
})

export class StationCardComponent {
  router = inject(Router);
  ssrService = inject(SsrService);
  @Input() station: Station | undefined;
  readonly ROUTE_PATHS = ROUTE_PATHS;

  navigateToEdit(station: Station): void {
    if (!station.id) {
    console.error('Station ID is missing');
    return;
  }
  console.log('Navigating to edit station:', station.id);
  this.router.navigate(['/stations', station.id, 'edit'], {
    state: { station: station }
  });
}
}