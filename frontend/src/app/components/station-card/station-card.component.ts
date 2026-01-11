import { Component, inject, Input } from '@angular/core';
import { Station } from '../../models/station';
import { SsrService } from '../../services/ssr.service';
import { RouterLink } from "@angular/router";
import { ROUTE_PATHS } from '../../utils/routeMapping';
import { Router } from '@angular/router';
import { StationService } from '../../services/station.service';
import { UserService } from '../../services/user.service';

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
  userService = inject(UserService);
  stationService = inject(StationService);
  @Input() item: Station | undefined;
  readonly ROUTE_PATHS = ROUTE_PATHS;

  navigateToEdit(station: Station): void {
  this.router.navigate(['/stations', station.id, 'edit'], {
    state: { station: station }
  });
  }

  deleteStation(station: Station): void {
    this.stationService.deleteStation(station.id!).subscribe({
      next: (response) => {
        // Should be in a confirmation modal
        this.userService.refreshUserStations();
        console.log(`Station with ID ${station.id} deleted successfully.`);
      },
      error: (err) => {
        console.error('Error deleting station', err);
      }
    });
  }
}